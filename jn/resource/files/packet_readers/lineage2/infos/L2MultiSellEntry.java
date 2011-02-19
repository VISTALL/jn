package packet_readers.lineage2.infos;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  23:43:04/31.07.2010
 */
public class L2MultiSellEntry
{
	private final int _id;
	private List<L2ItemComponent> _ingridients = new ArrayList<L2ItemComponent>();
	private List<L2ItemComponent> _productions = new ArrayList<L2ItemComponent>();

	public L2MultiSellEntry(int id)
	{
		_id = id;
	}

	public void addIngridient(L2ItemComponent i)
	{
		_ingridients.add(i);
	}

	public void addProduction(L2ItemComponent p)
	{
		_productions.add(p);
	}

	public List<L2ItemComponent> getIngridients()
	{
		return _ingridients;
	}

	public List<L2ItemComponent> getProductions()
	{
		return _productions;
	}

	public int getId()
	{
		return _id;
	}
}
