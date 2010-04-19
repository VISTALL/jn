package com.jds.jn_module.network.jpcap;

import jpcap.NetworkInterface;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  2:46:47/04.04.2010
 */
public class DeviceInfo
{
	private final NetworkInterface _device;

	public DeviceInfo(NetworkInterface d)
	{
		_device = d;
	}

	public NetworkInterface getDevice()
	{
		return _device;
	}

	@Override
	public String toString()
	{
		return _device.description;
	}
}
