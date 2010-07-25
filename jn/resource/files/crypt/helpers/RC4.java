/******************************************************************************
 *
 * Copyright (c) 1998,99 by Mindbright Technology AB, Stockholm, Sweden.
 *                 www.mindbright.se, info@mindbright.se
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *****************************************************************************
 * $Author: nallen $
 * $Date: 2001/11/12 16:31:16 $
 * $Name:  $
 *****************************************************************************/
/*
 * !!! Author's comment: I don't know if there are any copyright
 * issues here but this code is so trivial so I guess there are not
 * (apart from the name RC4 which I believe is a trademark...).
 */
package crypt.helpers;

public class RC4
{
	private int _x;
	private int _y;
	private byte[] _state = new byte[256];

	final int arcfour_byte()
	{
		int x;
		int y;
		int sx, sy;

		x = (_x + 1) & 0xff;
		sx = (int) _state[x];
		y = (sx + _y) & 0xff;
		sy = (int) _state[y];
		_x = x;
		_y = y;
		_state[y] = (byte) (sx & 0xff);
		_state[x] = (byte) (sy & 0xff);
		return (int) _state[((sx + sy) & 0xff)];
	}

	public synchronized void encrypt(byte[] src, int srcOff, byte[] dest, int destOff, int len)
	{
		int end = srcOff + len;
		for (int si = srcOff, di = destOff; si < end; si++, di++)
		{
			dest[di] = (byte) (((int) src[si] ^ arcfour_byte()) & 0xff);
		}
	}

	public void decrypt(byte[] src, int srcOff, byte[] dest, int destOff, int len)
	{
		encrypt(src, srcOff, dest, destOff, len);
	}

	public void setKey(byte[] key)
	{
		int t, u;
		int keyindex;
		int stateindex;
		int counter;

		for (counter = 0; counter < 256; counter++)
		{
			_state[counter] = (byte) counter;
		}
		keyindex = 0;
		stateindex = 0;
		for (counter = 0; counter < 256; counter++)
		{
			t = (int) _state[counter];
			stateindex = (stateindex + key[keyindex] + t) & 0xff;
			u = (int) _state[stateindex];
			_state[stateindex] = (byte) (t & 0xff);
			_state[counter] = (byte) (u & 0xff);
			if (++keyindex >= key.length)
			{
				keyindex = 0;
			}
		}
	}
}

