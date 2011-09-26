package com.jds.jn.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup;
import com.jds.jn.classes.CLoader;
import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.data.xml.holder.ProtocolHolder;
import com.jds.jn.gui.panels.ViewPane;
import com.jds.jn.gui2.MainForm.ribbon.SessionMenu.SessionRibbonTaskGroup;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.parser.packetfactory.tasks.CloseTask;
import com.jds.jn.parser.packetfactory.tasks.InvokeTask;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.jn.util.version_control.Version;

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

	private final List<CryptedPacket> _cryptedPackets = new CopyOnWriteArrayList<CryptedPacket> ();
	private final List<DecryptedPacket> _decryptPackets = new CopyOnWriteArrayList<DecryptedPacket>();

	private final IMethod _method;
	private final ListenerType _type;

	// gui
	private ViewPane _viewPane;
	private RibbonContextualTaskGroup _ribbonGroup;

	private final List<IPacketListener> _invokes = new ArrayList<IPacketListener>();

	public Session(IMethod iMethod, long sessionId)
	{
		this(iMethod, iMethod.getListenerType(), sessionId);
	}

	public Session(ListenerType type, long sessionId)
	{
		this(null, type, sessionId);
	}

	private Session(IMethod method, ListenerType type, long sessionId)
	{
		_method = method;
		_type = type;
		_sessionId = sessionId;

		Protocol protocol = ProtocolHolder.getInstance().getProtocol(type);
		if(protocol == null)
		{
			_log.info("Not find protocol for type: " + type);
			throw new IllegalArgumentException("Not find protocol");
		}

		_protocol = protocol;

		try
		{
			Class<?> clazz = CLoader.getInstance().forName("crypt." + getProtocol().getEncryption() + "Crypter");
			_crypt = (ProtocolCrypter) clazz.newInstance();
		}
		catch (Exception e)
		{
			_log.info("Exception: " + e, e);
		}

		_invokes.addAll(getProtocol().getSessionListeners());
		_invokes.addAll(getProtocol().getGlobalListeners());
	}

	public DecryptedPacket decode(CryptedPacket packet)
	{
		packet.setDecrypted(true);

		return new DecryptedPacket(this, packet.getPacketType(), packet.getAllData(), packet.getTime(), getProtocol(), true);
	}

	public long getSessionId()
	{
		return _sessionId;
	}

	public List<CryptedPacket> getCryptedPackets()
	{
		return _cryptedPackets;
	}

	public Protocol getProtocol()
	{
		return _protocol;
	}

	public ProtocolCrypter getCrypt()
	{
		return _crypt;
	}

	public void receiveQuitPacket(CryptedPacket p, boolean update)
	{
		_cryptedPackets.add(p);

		ViewPane pane = getViewPane();
		pane.getCryptedPacketListPane().getModel().addRow(-1, p, update); 

		if(update)
		{
			if(!pane.getPacketListPane().isHidden() && pane.getSelectedComponent() == pane.getInfoPane())
				pane.updateInfo(this);
		} 
	}

	public void receiveCryptedPackets(List<CryptedPacket> packets)
	{
		_cryptedPackets.addAll(packets);

		for(CryptedPacket p : packets)
			getViewPane().getCryptedPacketListPane().getModel().addRow(-1, p, false);

		getViewPane().getCryptedPacketListPane().getModel().refresh();
		getViewPane().updateInfo(this);
	}

	public void receiveQuitPacket(DecryptedPacket p, boolean gui, boolean fire)
	{
		_decryptPackets.add(p);

		boolean isUnknown = p.getPacketInfo() == null;
		if(gui)
		{
			getViewPane().getDecryptedPacketListPane().getModel().addRow(-1, p, true);

			if(isUnknown)
				getViewPane().getUnknownPacketListPane().getModel().addRow(-1, p, true);
		}

		if(!isUnknown && fire && !_invokes.isEmpty())
			ThreadPoolManager.getInstance().execute(new InvokeTask(this, Collections.singletonList(p)));
	}

	public void receiveDecryptedPackets(List<DecryptedPacket> packets)
	{
		_decryptPackets.addAll(packets);

		for(DecryptedPacket p : packets)
		{
			boolean isUnknown = p.getPacketInfo() == null;
			getViewPane().getDecryptedPacketListPane().getModel().addRow(-1, p, false);

			if(isUnknown)
				getViewPane().getUnknownPacketListPane().getModel().addRow(-1, p, false);
		}

		getViewPane().getDecryptedPacketListPane().getModel().refresh();
		getViewPane().updateInfo(this);
	}

	public void onShow()
	{
		getViewPane().drawThis();

		getViewPane().updateInfo(this);

		getViewPane().getDecryptedPacketListPane().getPacketTable().updateUI();
		getViewPane().getCryptedPacketListPane().getPacketTable().updateUI();
	}

	public void close()
	{
		ThreadPoolManager.getInstance().execute(new CloseTask(this));

		SessionTable.getInstance().removeGameSession(getSessionId());

		_viewPane = null;
		_ribbonGroup = null;
		_decryptPackets.clear();
		_cryptedPackets.clear();
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
		if(_viewPane == null)
			_viewPane = new ViewPane(this);
		return _viewPane;
	}

	public List<DecryptedPacket> getDecryptPackets()
	{
		return _decryptPackets;
	}

	public void fireInvokePacket(final DecryptedPacket o)
	{
		for(IPacketListener f : _invokes)
			f.invoke(o);
	}

	public void fireClose()
	{
		for(IPacketListener f : _invokes)
			f.close();
	}

	public Version getVersion()
	{
		return _version;
	}

	public void setVersion(Version version)
	{
		_version = version;
	}

	public RibbonContextualTaskGroup getRibbonGroup()
	{
		if(_ribbonGroup == null)
			_ribbonGroup = new SessionRibbonTaskGroup(this);
		return _ribbonGroup;
	}

	public List<IPacketListener> getInvokes()
	{
		return _invokes;
	}
}

