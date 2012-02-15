/*
 * Jn (Java sNiffer)
 * Copyright (C) 2012 napile.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package packet_readers.lineage2.listeners;

import java.util.Arrays;
import java.util.List;

import com.jds.jn.network.packets.DecryptedPacket;
import packet_readers.lineage2.L2AbstractListener;

/**
 * @author VISTALL
 * @date 11:29/12.02.2012
 */
public class L2PlayerListener extends L2AbstractListener
{
	private static final String USER_INFO = "UserInfo";

	@Override
	public List<String> getPackets()
	{
		return Arrays.asList(USER_INFO);
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		//if (p.getName().equalsIgnoreCase(USER_INFO))
		{
			_world.setUserLevel(p.getInt("level"));
			_world.setUserClassId(p.getInt("class_id"));
		}
	}
}
