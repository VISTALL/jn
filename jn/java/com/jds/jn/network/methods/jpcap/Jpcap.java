package com.jds.jn.network.methods.jpcap;

import java.io.IOException;

import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.methods.IMethod;
import com.jds.jn.network.profiles.*;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 23/12/2009
 * Time: 2:24:55
 */
public class Jpcap implements IMethod
{
	private final JpcapPacketListener _receiver = new JpcapPacketListener(this);
	private JpcapCaptor _cap;
	private ListenerType _type;
	private int _port;

	public Jpcap(ListenerType type)
	{
		_type = type;
	}

	@Override
	public void init() throws IOException
	{
		NetworkProfile profile = NetworkProfiles.getInstance().active();
		if (profile == null)
		{
			throw new IOException("Not set network profile");
		}

		NetworkProfilePart part = profile.getPart(_type);

		NetworkInterface[] list = JpcapCaptor.getDeviceList();

		_cap = JpcapCaptor.openDevice(list[part.getDeviceId()], 65535, true, 10);

		_port = part.getDevicePort();
		_cap.setFilter("tcp port " + part.getDevicePort(), true);
	}

	@Override
	public void start() throws Exception
	{
		MainForm.getInstance().info("Start Jpcap on port " + _port);
		_cap.loopPacket(-1, _receiver);
	}

	@Override
	public void stop() throws Exception
	{
		MainForm.getInstance().info("Stop Jpcap on port " + _port);
		_cap.close();
	}

	@Override
	public ListenerType getListenerType()
	{
		return _type;
	}

	@Override
	public long getSessionId()
	{
		throw new IllegalArgumentException("Can't get session id from Jpcap");
	}

	@Override
	public ReceiveType getType()
	{
		return ReceiveType.JPCAP;
	}

	public int getPort()
	{
		return _port;
	}
}
