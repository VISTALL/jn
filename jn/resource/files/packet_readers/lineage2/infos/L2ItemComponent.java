package packet_readers.lineage2.infos;

/**
 * @author VISTALL
 * @date  23:02:56/31.07.2010
 */
public class L2ItemComponent
{
	private final int _itemId;
	private final int _chance;
	private final long _count;

	public L2ItemComponent(int itemId, long count, int chance)
	{
		_itemId = itemId;
		_count = count;
		_chance = chance;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public long getCount()
	{
		return _count;
	}

	public int getChance()
	{
		return _chance;
	}
}
