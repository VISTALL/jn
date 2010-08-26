package part_readers;

import javax.swing.*;

import com.jds.jn.holders.ItemNameHolder;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:34:35/24.08.2010
 */
public class ItemNameReader implements ValueReader
{
	@Override
	public JComponent readToComponent(ValuePart part)
	{
		return new JLabel(read(part));
	}

	@Override
	public String read(ValuePart part)
	{
		return ItemNameHolder.getInstance().name(((VisualValuePart)part).getValueAsInt());
	}
}
