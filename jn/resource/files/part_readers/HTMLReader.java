package part_readers;

import org.w3c.dom.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jds.jn.Jn;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * @author Ulysses R. Ribeiro
 */
public class HTMLReader implements ValueReader
{

	public <T extends Enum<T>> T getEnum(ValuePart part)
	{
		return null;
	}

	public boolean loadReaderFromXML(Node n)
	{
		return true;
	}

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
		view.addActionListener(new ButtonActionListener(this.read(part)));
		view.setActionCommand("clicked");
		return view;
	}

	public boolean saveReaderToXML(Element element, Document doc)
	{
		return true;
	}

	public boolean supportsEnum()
	{
		return false;
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
			JDialog dlg = new JDialog(Jn.getForm(), "HTML");
			dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dlg.setSize(350, 400);
			dlg.setLocationRelativeTo(Jn.getForm());

			JTabbedPane tabPane = new JTabbedPane();

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
