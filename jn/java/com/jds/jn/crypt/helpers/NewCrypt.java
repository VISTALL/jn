package com.jds.jn.crypt.helpers;

import java.io.IOException;

/**
 * Author: VISTALL
 * Date: 15.08.2009
 * Time: 21:23:55
 */
public class NewCrypt
{
	private BlowfishEngine _crypt;
	private BlowfishEngine _decrypt;

	public NewCrypt(byte[] blowfishKey)
	{
		_crypt = new BlowfishEngine();
		_crypt.init(true, blowfishKey);
		_decrypt = new BlowfishEngine();
		_decrypt.init(false, blowfishKey);
	}

	public NewCrypt(String key)
	{
		this(key.getBytes());
	}

	public static boolean verifyChecksum(byte[] raw)
	{
		/**
		 // check if size is multiple of 4 and if there is more then only the checksum
		 if ((size & 3) != 0 || size <= 4)
		 {
		 return false;
		 }  */

		long chksum = 0;
		int count = raw.length - 4;
		long check = -1;
		int i;

		for (i = 0; i < count; i += 4)
		{
			check = raw[i] & 0xff;
			check |= raw[i + 1] << 8 & 0xff00;
			check |= raw[i + 2] << 0x10 & 0xff0000;
			check |= raw[i + 3] << 0x18 & 0xff000000;

			chksum ^= check;
		}

		check = raw[i] & 0xff;
		check |= raw[i + 1] << 8 & 0xff00;
		check |= raw[i + 2] << 0x10 & 0xff0000;
		check |= raw[i + 3] << 0x18 & 0xff000000;

		return check == chksum;
	}

	public static void appendChecksum(byte[] raw)
	{
		NewCrypt.appendChecksum(raw, raw.length);
	}

	public static void appendChecksum(byte[] raw, final int size)
	{
		long chksum = 0;
		int count = size - 4;
		long ecx;
		int i;

		for (i = 0; i < count; i += 4)
		{
			ecx = raw[i] & 0xff;
			ecx |= raw[i + 1] << 8 & 0xff00;
			ecx |= raw[i + 2] << 0x10 & 0xff0000;
			ecx |= raw[i + 3] << 0x18 & 0xff000000;

			chksum ^= ecx;
		}

		ecx = raw[i] & 0xff;
		ecx |= raw[i + 1] << 8 & 0xff00;
		ecx |= raw[i + 2] << 0x10 & 0xff0000;
		ecx |= raw[i + 3] << 0x18 & 0xff000000;

		raw[i] = (byte) (chksum & 0xff);
		raw[i + 1] = (byte) (chksum >> 0x08 & 0xff);
		raw[i + 2] = (byte) (chksum >> 0x10 & 0xff);
		raw[i + 3] = (byte) (chksum >> 0x18 & 0xff);
	}

	/**
	 * JPacket is first XOR encoded with <code>key</code>
	 * Then, the last 4 bytes are overwritten with the the XOR "key".
	 * Thus this assume that there is enough room for the key to fit without overwriting data.
	 *
	 * @param raw The raw bytes to be encrypted
	 * @param key The 4 bytes (int) XOR key
	 */
	public static void encXORPass(byte[] raw, int key)
	{
		NewCrypt.encXORPass(raw, 0, raw.length, key);
	}

	/**
	 * JPacket is first XOR encoded with <code>key</code>
	 * Then, the last 4 bytes are overwritten with the the XOR "key".
	 * Thus this assume that there is enough room for the key to fit without overwriting data.
	 *
	 * @param raw	The raw bytes to be encrypted
	 * @param offset The begining of the data to be encrypted
	 * @param size   Length of the data to be encrypted
	 * @param key	The 4 bytes (int) XOR key
	 */
	public static void encXORPass(byte[] raw, final int offset, final int size, int key)
	{
		int stop = size - 8;
		int pos = 4 + offset;
		int edx;
		int ecx = key; // Initial xor key

		while (pos < stop)
		{
			edx = (raw[pos] & 0xFF);
			edx |= (raw[pos + 1] & 0xFF) << 8;
			edx |= (raw[pos + 2] & 0xFF) << 16;
			edx |= (raw[pos + 3] & 0xFF) << 24;

			ecx += edx;

			edx ^= ecx;

			raw[pos++] = (byte) (edx & 0xFF);
			raw[pos++] = (byte) (edx >> 8 & 0xFF);
			raw[pos++] = (byte) (edx >> 16 & 0xFF);
			raw[pos++] = (byte) (edx >> 24 & 0xFF);
		}

		raw[pos++] = (byte) (ecx & 0xFF);
		raw[pos++] = (byte) (ecx >> 8 & 0xFF);
		raw[pos++] = (byte) (ecx >> 16 & 0xFF);
		raw[pos++] = (byte) (ecx >> 24 & 0xFF);
	}

	public static void decXORPass(byte[] raw)
	{
		int count = raw.length / 4;
		int pos = (count - 1) * 4;
		int ecx;

		ecx = (raw[--pos] & 0xFF) << 24;
		ecx |= (raw[--pos] & 0xFF) << 16;
		ecx |= (raw[--pos] & 0xFF) << 8;
		ecx |= (raw[--pos] & 0xFF);

		int val;
		while (pos > 4)
		{
			raw[--pos] ^= (ecx >> 24);

			val = (raw[pos] & 0xFF) << 24;
			raw[--pos] ^= (ecx >> 16);
			val += (raw[pos] & 0xFF) << 16;
			raw[--pos] ^= (ecx >> 8);
			val += (raw[pos] & 0xFF) << 8;
			raw[--pos] ^= ecx;
			val += (raw[pos] & 0xFF);

			ecx = ecx - val;
		}
	}

	public void decrypt(byte[] raw) throws IOException
	{
		int count = raw.length / 8;

		for (int i = 0; i < count; i++)
		{
			_decrypt.processBlock(raw, i * 8);
		}
	}


	public void crypt(byte[] raw) throws IOException
	{
		int count = raw.length / 8;

		for (int i = 0; i < count; i++)
		{
			_crypt.processBlock(raw, i * 8);
		}
	}

	public byte[] encrypt(byte[] raw) throws IOException
	{
		int size = raw.length;
		// reserve checksum
		size += 4;
		// padding
		size += 8 - size % 8;

		byte[] b = new byte[size];

		System.arraycopy(raw, 0, b, 0, raw.length);

		NewCrypt.appendChecksum(b, size);

		crypt(b);

		return b;
	}
}
