package part_readers;

import java.text.SimpleDateFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;

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
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

	public JComponent readToComponent(ValuePart part)
	{
		return new JLabel(read(part));
	}

	@Override
	public String read(ValuePart part)
	{
		if(!(part instanceof VisualValuePart))
			return "";

		long val = 0;
		switch(((VisualValuePart) part).getValueType())
		{
			case d:
				val = ((VisualValuePart) part).getValueAsInt() * 1000L;
				break;
			case Q:
				val = ((VisualValuePart) part).getValueAsLong();
				break;
		}

		return FORMAT.format(val);
	}
}
