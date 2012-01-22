package packet_readers.lineage2;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.session.Session;

/**
 * @author VISTALL
 * @date 0:11/01.10.2011
 */
public abstract class L2AbstractListener extends IPacketListener.Abstract
{
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH-mm-ss dd.MM.yyyy");

	protected L2World _world;

	@Override
	public final void invoke(Session session, DecryptedPacket p)
	{
		if(_world == null)
			_world = getWorld(session);

		invokeImpl(p);
	}

	@Override
	public final void close() throws IOException
	{
		if(_world == null)
			return;

		closeImpl();
	}

	public abstract void invokeImpl(DecryptedPacket p);

	public void closeImpl() throws IOException {}

	public File getLogFile(String prefix, String ext)
	{
		String format = TIME_FORMAT.format(System.currentTimeMillis());

		File file = new File("./saves/" + prefix + format + "." + ext);
		File parent = file.getParentFile();
		while(parent != null)
		{
			if(!parent.exists())
				parent.mkdirs();

			parent = parent.getParentFile();
		}

		return file;
	}

	public L2World getWorld(Session session)
	{
		L2World world = session.getVar(L2World.class);
		if(world == null)
			session.setVar(L2World.class, world = new L2World());
		return world;
	}
}
