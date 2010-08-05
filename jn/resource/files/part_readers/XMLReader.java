package part_readers;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jds.jn.Jn;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * Author: VISTALL
 * Date: 24.07.2009
 * Time: 5:08:10
 */
public class XMLReader implements ValueReader
{
	public String read(ValuePart part)
	{
		if (part instanceof VisualValuePart)
		{
			return  part.getValueAsString();
		}
		Jn.getForm().warn("XML ValueReader set on a non String part: " + part.getModelPart().getName());
		return "";
	}

	public JComponent readToComponent(ValuePart part)
	{
		JButton view = new JButton("View");
		view.addActionListener(new ButtonActionListener(this.read(part)));
		view.setActionCommand("clicked");
		return view;
	}

	class ButtonActionListener implements ActionListener
	{
		private String _xml;

		public ButtonActionListener(String html)
		{
			_xml = html;
		}

		public void actionPerformed(ActionEvent e)
		{
			JDialog dlg = new JDialog(Jn.getForm(), "HTML");
			dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dlg.setSize(350, 400);
			dlg.setLocationRelativeTo(Jn.getForm());

			JTabbedPane tabPane = new JTabbedPane();

			// XML
			JEditorPane htmlDisplay = new JEditorPane();
			htmlDisplay.setEditable(false);
			htmlDisplay.setContentType("text/html");
			htmlDisplay.setText(_xml);

			// Source
			JEditorPane sourceDisplay = new JEditorPane();
			sourceDisplay.setEditable(false);
			sourceDisplay.setContentType("text/plain");
			sourceDisplay.setText(_xml);

			tabPane.add(new JScrollPane(htmlDisplay), "XML");
			tabPane.add(new JScrollPane(sourceDisplay), "Source");

			dlg.add(tabPane);
			dlg.setVisible(true);
		}
	}
}
