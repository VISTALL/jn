package part_readers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import com.jds.jn.Jn;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * @author Ulysses R. Ribeiro
 */
public class HTMLReader implements ValueReader
{
	public String read(ValuePart part)
	{
		if (part instanceof VisualValuePart)
		{
			return part.getValueAsString();
		}
		Jn.getForm().warn("ERROR: HTML ValueReader set on a non String part: " + part.getModelPart().getName());
		return "";
	}

	public JComponent readToComponent(ValuePart part)
	{
		JButton view = new JButton("View");
		view.addActionListener(new ButtonActionListener(read(part)));
		return view;
	}

	class ButtonActionListener implements ActionListener
	{
		private String _html;

		public ButtonActionListener(String html)
		{
			_html = html;
		}

		public void actionPerformed(ActionEvent e)
		{
			final JDialog dlg = new JDialog();
			dlg.setTitle("HTML");
			dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dlg.setSize(350, 400);
			dlg.setLocationRelativeTo(MainForm.getInstance());

			JTabbedPane tabPane = new JTabbedPane();
			tabPane.registerKeyboardAction(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					dlg.dispose();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

			// HTML
			JEditorPane htmlDisplay = new JEditorPane();
			htmlDisplay.setEditable(false);
			htmlDisplay.setContentType("text/html");
			htmlDisplay.setText(_html);

			// Source
			JEditorPane sourceDisplay = new JEditorPane();
			sourceDisplay.setEditable(false);
			sourceDisplay.setContentType("text/plain");
			sourceDisplay.setText(_html);

			tabPane.add(new JScrollPane(htmlDisplay), "HTML");
			tabPane.add(new JScrollPane(sourceDisplay), "Source");

			dlg.add(tabPane);
			dlg.setVisible(true);
		}
	}
}
