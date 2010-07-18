package com.jds.jn.session;

import java.util.*;

import com.jds.jn.Jn;
import com.jds.jn.crypt.NullCrypter;
import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.packets.DecryptPacket;
import com.jds.jn.network.packets.NotDecryptPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.jn.version_control.Version;

/**
 * @author Ulysses R. Ribeiro
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class Session
{
	private long _sessionId;
	private Version _version = Jn.CURRENT;

	private ProtocolCrypter _crypt;
	private Protocol _protocol;

	private final ArrayList<NotDecryptPacket> _notDecryptPackets = new ArrayList<NotDecryptPacket>();
	private final ArrayList<DecryptPacket> _decryptPackets = new ArrayList<DecryptPacket>();

	private IMethod _method;
	private ListenerType _type;
	private ViewPane _viewPane;

	private final ArrayList<IPacketListener> _invokes = new ArrayList<IPacketListener>();

	public Session(IMethod iMethod, Protocol protocol)
	{
		_method = iMethod;
		_type = iMethod.getListenerType();
		_sessionId = iMethod.getSessionId();
		_protocol = protocol;
		init(true);
	}

	public Session(ListenerType type, long sessionId, Protocol protocol, boolean createViewPane)
	{
		_type = type;
		_sessionId = sessionId;
		_protocol = protocol;
		_viewPane = new ViewPane(this);

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

		/*FastList<Class<? extends IPacketListener>> l = new FastList<Class<? extends IPacketListener>>();
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
		return _viewPane != null ? _viewPane.getProtocol() : _protocol;
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

	public synchronized void receiveQuitPacket(NotDecryptPacket p)
	{
		_notDecryptPackets.add(p);

		if (_viewPane != null)
		{
			_viewPane.getPacketTableModel2().addRow(p);
		}
	}

	public synchronized void receiveQuitPacket(DecryptPacket p)
	{
		addDecryptPacket(p);

		if (_viewPane != null)
		{
			_viewPane.getPacketTableModel().addRow(p);
		}
	}

	public void updateUI()
	{
		if (_viewPane != null)
		{
			//_viewPane.updateInfo(this);
			_viewPane.getPacketListPane().getPacketTable().updateUI();
			_viewPane.getNotDecPacketListPane().getPacketTable().updateUI();
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

		fireInvokePacket(packet);
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

