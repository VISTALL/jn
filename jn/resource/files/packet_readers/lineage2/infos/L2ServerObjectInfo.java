package packet_readers.lineage2.infos;

import org.apache.log4j.Logger;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.datatree.DataSwitchBlock;
import com.jds.jn.parser.datatree.DataTreeNode;
import com.jds.jn.parser.datatree.VisualValuePart;
import packet_readers.lineage2.L2World;

/**
 * @author VISTALL
 * @date 2:29/01.10.2011  \
 * 			<part name="object_id" type="d"/>
 			<part name="npc_id" type="d"/>
 			<part name="name" type="S"/>
 			<part name="attackable" type="d"/>
 			<part name="x" type="d"/>
 			<part name="y" type="d"/>
 			<part name="z" type="d"/>
 			<part name="h" type="d"/>
 			<part name="movement_multiplier" type="D"/>
 			<part name="attack_multiplier" type="D"/>
 			<part name="col_radius" type="D"/>
 			<part name="col_height" type="D"/>
 			<part name="current_hp" type="d"/>
 			<part name="max_hp" type="d"/>
 			<part name="type" type="d" id="0" />
 			<part name="special_effects" type="d"/>
 */
public class L2ServerObjectInfo extends L2DialogObject
{
	private static final Logger LOGGER = Logger.getLogger(L2ServerObjectInfo.class);
	private int _npcId;
	private int _maxHp;
	private int _type;
	private int _statisticType = -1;
	private double _colRadius;
	private double _colHeight;
	private L2SpawnLocInfo _spawnLoc;

	public L2ServerObjectInfo(DecryptedPacket packet)
	{
		_npcId = packet.getInt("npc_id");
		_maxHp = packet.getInt("max_hp");
		_type = packet.getInt("type");
		_colHeight = packet.getDouble("col_height"); 
		_colRadius = packet.getDouble("col_radius"); 
		_spawnLoc = new L2SpawnLocInfo(packet);
		switch(_type)
		{
			case 4:
				break;
			case 7:
				for(DataTreeNode a : packet.getRootNode().getNodes())
				{
					if(a instanceof DataSwitchBlock)
					{
						DataSwitchBlock caseBlock = (DataSwitchBlock)a;
						if(caseBlock.getCaseValue() == 7)
							_statisticType = ((VisualValuePart) caseBlock.getPartByName("statistic-name-id")).getValueAsInt();
						break;
					}
				}
				break;
			default:
				LOGGER.warn("Unknown type: " + _type + "; object-id:" + packet.getInt(L2World.OBJECT_ID));
				break;
		}
	}

	public static Logger getLogger()
	{
		return LOGGER;
	}

	public int getNpcId()
	{
		return _npcId;
	}

	public int getMaxHp()
	{
		return _maxHp;
	}

	public int getType()
	{
		return _type;
	}

	public double getColRadius()
	{
		return _colRadius;
	}

	public double getColHeight()
	{
		return _colHeight;
	}

	public L2SpawnLocInfo getSpawnLoc()
	{
		return _spawnLoc;
	}

	public int getStatisticType()
	{
		return _statisticType;
	}
}
