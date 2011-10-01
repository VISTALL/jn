package packet_readers.aion;

import java.io.File;
import java.text.SimpleDateFormat;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.session.Session;

/**
 * @author VISTALL
 * @date 0:42/01.10.2011
 */
public abstract class AionAbstractListener extends IPacketListener.Abstract
{
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH-mm-ss dd.MM.yyyy");

	protected AionWorld _world;

	@Override
	public final void invoke(Session session, DecryptedPacket p)
	{
		if(_world == null)
			_world = getWorld(session);

		invokeImpl(p);
	}

	public abstract void invokeImpl(DecryptedPacket p);

	public File getLogFile(String prefix, String ext)
	{
		String format = TIME_FORMAT.format(System.currentTimeMillis());

		File file = new File("./saves/" + prefix + format + "." + ext);
		file.mkdirs();

		return file;
	}

	public AionWorld getWorld(Session session)
	{
		AionWorld world = session.getVar(AionWorld.class);
		if(world == null)
			session.setVar(AionWorld.class, world = new AionWorld());
		return world;
	}
}