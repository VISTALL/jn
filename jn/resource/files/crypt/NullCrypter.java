package crypt;


import com.jds.jn.crypt.ProtocolCrypter;
import com.jds.jn.network.packets.PacketType;
import com.jds.jn.protocol.Protocol;
import com.jds.jn.session.Session;

/**
 * @author Gilles Duboscq
 */
public class NullCrypter implements ProtocolCrypter
{

	public byte[] decrypt(byte[] raw, PacketType dir, Session session)
	{
   		return raw;
	}

	@Override
	public byte[] encrypt(byte[] raw, PacketType dir, Session session)
	{
		return null;
	}

	public void setProtocol(Protocol protocol)
	{

	}
}