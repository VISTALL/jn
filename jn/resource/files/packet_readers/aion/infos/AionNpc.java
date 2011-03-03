package packet_readers.aion.infos;

import java.util.Collection;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import com.jds.jn.network.packets.DecryptedPacket;

/**
 * @author VISTALL
 * @date 14:15/15.02.2011
 */
public class AionNpc
{
	private final int _npcId;
	private final int _level;
	private final int _maxHP;
	private final int _nameId;
	private final int _titleId;
	private final int _npcState;

	private IntObjectMap<AionLoc> _loc = new HashIntObjectMap<AionLoc>();

	public AionNpc(DecryptedPacket p)
	{
		_npcId = p.getInt("npcId");
		_level = p.getInt("level");
		_maxHP = p.getInt("maxHp");
		_nameId = p.getInt("npcTemplateNameId");
		_titleId = p.getInt("npcTemplateTitleId");
		_npcState = p.getInt("npcState");
	}

	public void addLoc(int d, AionLoc loc)
	{
		_loc.put(d, loc);
	}

	public int getNpcId()
	{
		return _npcId;
	}

	public int getLevel()
	{
		return _level;
	}

	public int getMaxHP()
	{
		return _maxHP;
	}

	public int getNameId()
	{
		return _nameId;
	}
	public int getTitleId()
	{
		return _titleId;
	}

	public int getNpcState()
	{
		return _npcState;
	}

	@Override
	public String toString()
	{
		return String.valueOf(_npcId);
	}

	public Collection<AionLoc> getLocs()
	{
		return _loc.values();
	}
}
