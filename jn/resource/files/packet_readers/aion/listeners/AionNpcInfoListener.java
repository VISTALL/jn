package packet_readers.aion.listeners;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import packet_readers.aion.AionWorld;

/**
 * @author VISTALL
 * @date 14:16/15.02.2011
 */
public class AionNpcInfoListener  implements IPacketListener
{
	private AionWorld _world;

	public AionNpcInfoListener(AionWorld world)
	{
		_world = world;
	}

	@Override
	public void invoke(DecryptedPacket p)
	{

	}

	@Override
	public void close()
	{

	}
}
