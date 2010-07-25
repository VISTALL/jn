package part_readers;

import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.valuereader.ValueReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.*;
import java.sql.Timestamp;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 20.10.2009
 * Time: 20:46:40
 */
public class TimestampReader implements ValueReader
{
	@Override
	public boolean loadReaderFromXML(Node n)
	{
		return true;
	}

	@Override
	public boolean saveReaderToXML(Element element, Document doc)
	{
		return true;
	}

	public JComponent readToComponent(ValuePart part)
	{
		return new JLabel(read(part));
	}

	@Override
	public String read(ValuePart part)
	{
		if(!(part instanceof VisualValuePart))
			return "";

		long result = ((VisualValuePart)part).getValueAsInt();

		Timestamp tt = new Timestamp(result);

		return tt.toString();
	}

	@Override
	public boolean supportsEnum()
	{
		return false;
	}

	@Override
	public <T extends Enum<T>> T getEnum(ValuePart part)
	{
		return null;
	}
}
