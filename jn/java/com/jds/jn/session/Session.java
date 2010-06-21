package com.jds.jn.session;

import javolution.util.FastList;

import java.util.ArrayList;
import java.util.Arrays;

import com.jds.jn.crypt.NullCrypter;
import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.NotDecryptPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.parser.packetfactory.lineage2.npc.L2NpcSpawnListener;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.util.Version;

/**
 * @author Ulysses R. Ribeiro
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class Session
{
	private long _sessionId;
	private Version _version;

	private ProtocolCrypter _crypt;
	private Protocol _protocol;

	private final ArrayList<NotDecryptPacket> _notDecryptPackets = new ArrayList<NotDecryptPacket>();
	private final ArrayList<DecryptPacket> _decryptPackets = new ArrayList<DecryptPacket>();

	private IMethod _method;
	private ListenerType _type;
	private ViewPane _viewPane;

	private final FastList<IPacketListener> _invokes =new FastList<IPacketListener>();

	public Session(IMethod iMethod, Protocol protocol)
	{
		_method = iMethod;
		_type = iMethod.getListenerType();
		_sessionId = iMethod.getSessionId();
		_protocol = protocol;
		_version = Version.UNKNOWN;
		init(true);
	}

	public Session(ListenerType type, long sessionId, Protocol protocol)
	{
		_type = type;
		_sessionId = sessionId;
		_protocol = protocol;

		init(true);
	}

	private void init(boolean crypted)
	{
		if (crypted)
		{
			try
			{
				Class<?> clazz = Class.forName("com.jds.jn.crypt." + _protocol.getEncryption() + "Crypter");
				_crypt = (ProtocolCrypter) clazz.newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			_crypt = new NullCrypter();
		}

		_crypt.setProtocol(_protocol);

		FastList<Class<? extends IPacketListener>> l = new FastList<Class<? extends IPacketListener>>();
		l.add(L2NpcSpawnListener.class);

		for(Class<? extends IPacketListener> cl : l)
		{
			try
			{
				_invokes.add(cl.newInstance());
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}

	public DecryptPacket decode(NotDecryptPacket packet)
	{
		byte data[] = Arrays.copyOf(packet.getBuffer().array(), packet.getBuffer().array().length);

		_crypt.decrypt(data, packet.getPacketType());

		return new DecryptPacket(data, packet.getPacketType(), _protocol);
	}

	public long getSessionId()
	{
		return _sessionId;
	}

	public ArrayList<NotDecryptPacket> getNotDecryptPackets()
	{
		return _notDecryptPackets;
	}

	public void setProtocol(Protocol p)
	{
		_protocol = p;
	}

	public Protocol getProtocol()
	{
		return _protocol;
	}

	public ProtocolCrypter getCrypt()
	{
		return _crypt;
	}

	public synchronized void receivePacket(NotDecryptPacket p)
	{
		_notDecryptPackets.add(p);

		if (_viewPane != null)
		{
			_viewPane.getPacketTableModel2().addRow(p);
			_viewPane.updateInfo(this);
		}
	}

	public void receivePacket(DecryptPacket p)
	{
		addDecryptPacket(p);

		if (_viewPane != null)
		{
			_viewPane.getPacketTableModel().addRow(p);
			_viewPane.updateInfo(this);
		}
	}

	public void close()
	{
		SessionTable.getInstance().removeGameSession(getSessionId());
	}

	public IMethod getMethod()
	{
		return _method;
	}

	public ListenerType getListenerType()
	{
		return _type;
	}

	public ViewPane getViewPane()
	{
		return _viewPane;
	}

	public void setViewPane(ViewPane viewPane)
	{
		_viewPane = viewPane;
	}

	public ArrayList<DecryptPacket> getDecryptPackets()
	{
		return _decryptPackets;
	}

	public void addDecryptPacket(DecryptPacket packet)
	{
		_decryptPackets.add(packet);

		for(IPacketListener f :_invokes)
		{
			f.invoke(packet);
		}
	}

	public Version getVersion()
	{
		return _version;
	}

	public void setVersion(Version version)
	{
		_version = version;
	}
}

