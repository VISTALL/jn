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

/**
 * @author VISTALL
 * @date 15:17/14.10.2011
 */
public class JumpTrack extends JumpNode
{
	public JumpTrack(int id, JumpChooseType jumpChooseType)
	{
		super(id, jumpChooseType);
	}

	@Override
	public JumpTrack getParent()
	{
		return this;
	}
}
