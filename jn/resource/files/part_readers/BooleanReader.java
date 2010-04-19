package part_readers;

import com.jds.jn.parser.datatree.NumberValuePart;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.valuereader.ValueReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.*;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.10.2009
 * Time: 5:01:39
 */
public class BooleanReader implements ValueReader
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
		if(!(part instanceof NumberValuePart))
			return "";

	    boolean result = (int)((NumberValuePart)part).getValueAsInt() == 1;

		return String.valueOf(result).toUpperCase();
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
