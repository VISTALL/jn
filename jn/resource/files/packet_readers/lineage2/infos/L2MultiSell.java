package packet_readers.lineage2.infos;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  23:02:37/31.07.2010
 */
public class L2MultiSell
{
	private final int _id;
	private List<L2MultiSellEntry> _entries = new ArrayList<L2MultiSellEntry>();

	public L2MultiSell(int id)
	{
		_id = id;
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
}
