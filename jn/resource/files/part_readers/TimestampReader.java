package part_readers;

import javax.swing.*;

import java.sql.Timestamp;

import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 20.10.2009
 * Time: 20:46:40
 */
public class TimestampReader implements ValueReader
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

		long result = ((VisualValuePart)part).getValueAsInt();

		Timestamp tt = new Timestamp(result);

		return tt.toString();
	}
}
