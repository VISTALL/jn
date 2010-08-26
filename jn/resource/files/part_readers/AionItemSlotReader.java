package part_readers;

import javax.swing.*;

import com.jds.jn.parser.datatree.ValuePart;
import com.jds.jn.parser.datatree.VisualValuePart;
import com.jds.jn.parser.valuereader.ValueReader;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 19.10.2009
 * Time: 18:18:42
 */
public class AionItemSlotReader implements ValueReader
{
	public String read(ValuePart part)
	{
		if(!(part instanceof VisualValuePart))
			return "";

		int result = ((VisualValuePart)part).getValueAsInt();
		return "Slot: " + ItemSlots.getById(getMask(result));
	}

	public JComponent readToComponent(ValuePart part)
	{
		return new JLabel(read(part));
	}

	public int getMask(int num)
	{
		for (int i = 0; i < 32; i++)
		{
			if ((num & (1 << i))== (1 << i))
			{
				return i;
			}
		}

    	return -1;
	}

	public static enum ItemSlots
	{
		NONE(-1),
		MAIN_HAND(0),
		OFF_HAND(1),
		HELMET(2),
		CHEST(3),
		GLOVES(4),
		BOOTS(5),
		EARRING_LEFT(6),
		EARRING_RIGHT(7),
		RING_LEFT(8),
		RING_RIGHT(9),
		NECKLACE(10),
		SHOULDER(11), //плече
		LEGS(12),
		POWER_SHARD_RIGHT(13),
		POWER_SHARD_LEFT(14),
		WINGS(15),
		BELT(16), //0x00010000
		SECONDARY_MAIN_HAND(17),
		SECONDARY_OFF_HAND(18);

		private final int _slotId;
		private final byte _id;

		ItemSlots(int st)
		{
			_slotId = 1 << st;
			_id = (byte) st;
		}

		public int getMask()
		{
			return _slotId;
		}

		public byte getId()
		{
			return _id;
		}

		public static ItemSlots getById(int id)
		{
			for (ItemSlots e : ItemSlots.values())
			{
				if (e.getId() == id)
				{
					return e;
				}
			}
			return NONE;
		}
	}
}
