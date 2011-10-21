package packet_readers.lineage2.infos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @date  23:02:37/31.07.2010
 */
public class L2MultiSell
{
	public static enum MultisellType
	{
		NORMAL_SELL,
		CHANCE_SELL;

		public static final MultisellType[] VALUES = values();
	}

	private final int _id;
	private final MultisellType _type;
	private List<L2MultiSellEntry> _entries = new ArrayList<L2MultiSellEntry>();

	public L2MultiSell(int id, int type)
	{
		_id = id;
		_type = MultisellType.VALUES[type];
	}

	public void addEntry(L2MultiSellEntry p)
	{
		_entries.add(p);
	}

	public List<L2MultiSellEntry> getEntries()
	{
		return _entries;
	}

	public int getId()
	{
		return _id;
	}

	public MultisellType getType()
	{
		return _type;
	}
}
