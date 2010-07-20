package com.jds.jn.session;

import org.apache.log4j.Logger;

import java.util.*;

import com.jds.jn.crypt.NullCrypter;
import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.NotDecryptPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.protocol.ProtocolManager;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.jn.version_control.Version;

/**
 * @author Ulysses R. Ribeiro
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class Session
{
	private static final Logger _log = Logger.getLogger(Session.class);

	private long _sessionId;
	private Version _version = Version.CURRENT;

	private ProtocolCrypter _crypt;
	private Protocol _protocol;

	private final List<NotDecryptPacket> _notDecryptPackets = new ArrayList<NotDecryptPacket>();
	private final List<DecryptPacket> _decryptPackets = new ArrayList<DecryptPacket>();

	private IMethod _method;
	private ListenerType _type;

	private ViewPane _viewPane = new ViewPane(this);

	private final List<IPacketListener> _invokes = new ArrayList<IPacketListener>();

	public Session(IMethod iMethod, Protocol protocol)
	{
		_method = iMethod;
		_type = iMethod.getListenerType();
		_sessionId = iMethod.getSessionId();

		if(protocol == null)
		{
			_log.info("Not find protocol for type: " + iMethod.getListenerType());
			throw new IllegalArgumentException("Not find protocol");
		}

		_protocol = protocol;

		init(true);
	}

	public Session(ListenerType type, long sessionId)
	{
		_type = type;
		_sessionId = sessionId;

		Protocol protocol = ProtocolManager.getInstance().getProtocol(type);
		if(protocol == null)
		{
			_log.info("Not find protocol for type: " + type);
			throw new IllegalArgumentException("Not find protocol");
		}

		_protocol = protocol;

		init(true);
	}

	private void init(boolean crypted)
	{    		

		if (crypted)
		{
			try
			{
				Class<?> clazz = Class.forName("com.jds.jn.crypt." + getProtocol().getEncryption() + "Crypter");
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

		_crypt.setProtocol(getProtocol());

		/*List<Class<? extends IPacketListener>> l = new ArrayList<Class<? extends IPacketListener>>();
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
		} */
	}

	public DecryptPacket decode(NotDecryptPacket packet)
	{
		byte data[] = Arrays.copyOf(packet.getBuffer().array(), packet.getBuffer().array().length);

		data = _crypt.decrypt(data, packet.getPacketType());

		return new DecryptPacket(data, packet.getPacketType(), getProtocol());
	}

	public long getSessionId()
	{
		return _sessionId;
	}

	public List<NotDecryptPacket> getNotDecryptPackets()
	{
		return _notDecryptPackets;
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

		_viewPane.getNotDecryptPacketTableModel().addRow(p);
		_viewPane.updateInfo(this);
	}

	public synchronized void receivePacket(DecryptPacket p)
	{
		addDecryptPacket(p);

		_viewPane.getDecryptPacketTableModel().addRow(p);
		_viewPane.updateInfo(this);
	}

	public synchronized void receiveQuitPacket(NotDecryptPacket p)
	{
		_notDecryptPackets.add(p);

		_viewPane.getNotDecryptPacketTableModel().addRow(p);
	}

	public synchronized void receiveQuitPacket(DecryptPacket p)
	{
		addDecryptPacket(p);

		_viewPane.getDecryptPacketTableModel().addRow(p);
	}

	public void addDecryptPacket(DecryptPacket packet)
	{
		_decryptPackets.add(packet);

		fireInvokePacket(packet);
	}

	public void show()
	{
		_viewPane.drawThis();

		_viewPane.updateInfo(this);
		_viewPane.getPacketListPane().getPacketTable().updateUI();
		_viewPane.getNotDecPacketListPane().getPacketTable().updateUI();
	}

	public void close()
	{
		fireClose();
		
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

	public List<DecryptPacket> getDecryptPackets()
	{
		return _decryptPackets;
	}

	public void fireInvokePacket(final DecryptPacket o)
	{
		ThreadPoolManager.getInstance().execute(new Runnable()
		{
			@Override
			public void run()
			{
				for(IPacketListener f :_invokes)
				{
					f.invoke(o);
				}
			}
		});
	}

	public void fireClose()
	{
		ThreadPoolManager.getInstance().execute(new Runnable()
		{
			@Override
			public void run()
			{
				for(IPacketListener f :_invokes)
				{
					f.close();
				}
			}
		});
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

