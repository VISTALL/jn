package part_readers;

import javax.swing.*;

import packet_readers.lineage2.holders.SkillNameHolder;
import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:34:35/24.08.2010
 */
public class SkillNameReader implements ValueReader
{
	@Override
	public JComponent readToComponent(ValuePart part)
	{
		return new JLabel(read(part));
	}

	@Override
	public String read(ValuePart part)
	{
		return SkillNameHolder.getInstance().name(((VisualValuePart)part).getValueAsInt(), 1);
	}
}
