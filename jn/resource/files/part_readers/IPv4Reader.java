package part_readers;

import javax.swing.*;

import com.jds.jn.Jn;
import com.jds.jn.parser.datatree.RawValuePart;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * @author Ulysses R. Ribeiro
 */
public class IPv4Reader implements ValueReader
{
	public String read(ValuePart part)
	{
		if (part.getBytesSize() == 4)
		{
			byte[] ip = ((RawValuePart)part).getBytes();
			return (ip[0] & 0xFF) + "." + (ip[1] & 0xFF) + "." + (ip[2] & 0xFF) + "." + (ip[3] & 0xFF);
		}
		Jn.getForm().warn("IPv4 ValueReader requires a part with exactly 4 bytes. Faulty part : " + part.getModelPart().getName());
		return "";
	}

	public JComponent readToComponent(ValuePart part)
	{
		return new JLabel(this.read(part));
	}
}
