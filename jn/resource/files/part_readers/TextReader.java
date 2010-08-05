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
 * Time: 5:32:21
 */
public class TextReader implements ValueReader
{	public String read(ValuePart part)
	{
		if (part instanceof VisualValuePart)
		{
			return part.getValueAsString();
		}
		Jn.getForm().warn("Text ValueReader set on a non String part: " + part.getModelPart().getName());
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
			JDialog dlg = new JDialog(Jn.getForm(), "Text");
			dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dlg.setSize(350, 400);
			dlg.setLocationRelativeTo(Jn.getForm());

			// Source
			JEditorPane sourceDisplay = new JEditorPane();
			sourceDisplay.setEditable(false);
			sourceDisplay.setContentType("text/plain");
			sourceDisplay.setText(_xml);

			dlg.add(new JScrollPane(sourceDisplay));
			dlg.setVisible(true);
		}
	}
}
