/*
 * Jn (Java sNiffer)
 * Copyright (C) 2011 napile.org
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

package packet_readers.lineage2.infos.jump;

import packet_readers.lineage2.infos.L2Loc;

/**
 * @author VISTALL
 * @date 15:18/14.10.2011
 */
public class JumpPoint extends JumpNode
{
	private L2Loc _loc;
	private JumpTrack _jumpTrack;

	public JumpPoint(int id, JumpChooseType jumpChooseType, L2Loc loc, JumpTrack jumpTrack)
	{
		super(id, jumpChooseType);
		_loc = loc;
		_jumpTrack = jumpTrack;
	}

	public L2Loc getLoc()
	{
		return _loc;
	}

	@Override
	public JumpTrack getParent()
	{
		return _jumpTrack;
	}
}
