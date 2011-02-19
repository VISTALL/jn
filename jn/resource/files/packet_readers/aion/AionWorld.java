package packet_readers.aion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import packet_readers.aion.holders.ClientStringHolder;
import packet_readers.aion.listeners.AionNpcInfoListener;
import packet_readers.aion.listeners.AionPlayerInfoListener;

/**
 * @author VISTALL
 * @date 13:56/15.02.2011
 */
public class AionWorld implements IPacketListener
{
	static
	{
		ClientStringHolder.getInstance();
	}

	private static final String[] DIRs =
	{
		"./saves/"
	};

	private List<IPacketListener> _listeners = new ArrayList<IPacketListener>(5);
	private int _worldId;

	public AionWorld()
	{
		for(String st : DIRs)
			new File(st).mkdir();

		_listeners.add(new AionNpcInfoListener(this));
		_listeners.add(new AionPlayerInfoListener(this));
	}

	@Override
	public void invoke(DecryptedPacket p)
	{
		if (p == null || p.getPacketInfo() == null || p.getName() == null || p.hasError())
		{
			return;
		}

		for (IPacketListener l : _listeners)
		{
			l.invoke(p);
		}
	}

	@Override
	public void close()
	{
		for (IPacketListener l : _listeners)
		{
			l.close();
		}
	}

	public int getWorldId()
	{
		return _worldId;
	}

	public void setWorldId(int worldId)
	{
		_worldId = worldId;
	}
}
