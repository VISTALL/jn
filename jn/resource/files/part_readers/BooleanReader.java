package part_readers;

import javax.swing.*;

import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23.10.2009
 * Time: 5:01:39
 */
public class BooleanReader implements ValueReader
{
	public JComponent readToComponent(ValuePart part)
	{
		return new JLabel(read(part));
	}

	@Override
	public String read(ValuePart part)
	{
		if(!(part instanceof VisualValuePart))
			return "";

	    boolean result = (int)((VisualValuePart)part).getValueAsInt() == 1;

		return String.valueOf(result).toUpperCase();
	}
}
