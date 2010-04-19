package com.jds.jn.session;

import com.jds.jn.Jn;
import com.jds.jn.crypt.NullCrypter;
import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.packets.DataPacket;
import com.jds.jn.network.packets.JPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.protocol.Protocol;
import javolution.util.FastList;

import java.util.Arrays;

/**
 * @author Ulysses R. Ribeiro
 * @author Gilles Duboscq
 * @author VISTALL
 */
public class Session
{
	private long _sessionId;
	private String _version = "Unknown Jn Version";

	private ProtocolCrypter _crypt;
	private Protocol _protocol;

	private final FastList<JPacket> _notDecryptPackets = new FastList<JPacket>();
	private final FastList<DataPacket> _decryptPackets = new FastList<DataPacket>();

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
		_version = Jn.VERSION;
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

		/*FastList<Class<? extends IPacketListener>> l = new FastList<Class<? extends IPacketListener>>();
		l.add(CharMoveToLocationL.class);

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
		}   */
	}

	public DataPacket decode(JPacket packet)
	{
		byte data[] = Arrays.copyOf(packet.getBuffer().array(), packet.getBuffer().array().length);

		_crypt.decrypt(data, packet.getType());

		return new DataPacket(data, packet.getType(), _protocol);
	}

	public long getSessionId()
	{
		return _sessionId;
	}

	public FastList<JPacket> getNotDecryptPackets()
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

	public synchronized void receivePacket(JPacket p)
	{
		_notDecryptPackets.add(p);

		if (_viewPane != null)
		{
			_viewPane.getPacketTableModel2().addRow(p);
			_viewPane.updateInfo(this);
		}
	}

	public void receivePacket(DataPacket p)
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

	public FastList<DataPacket> getDecryptPackets()
	{
		return _decryptPackets;
	}

	public void addDecryptPacket(DataPacket packet)
	{
		_decryptPackets.add(packet);

		for(IPacketListener f :_invokes)
		{
			f.invoke(packet);
		}
	}

	public String getVersion()
	{
		return _version;
	}

	public void setVersion(String version)
	{
		_version = version;
	}
}

