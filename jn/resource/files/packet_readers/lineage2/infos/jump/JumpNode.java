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

import java.util.Collection;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;

/**
 * @author VISTALL
 * @date 15:16/14.10.2011
 */
public abstract class JumpNode
{
	private IntObjectMap<JumpPoint> _nodes = new TreeIntObjectMap<JumpPoint>();
	private int _id;
	private JumpChooseType _chooseType;

	public JumpNode(int id, JumpChooseType jumpChooseType)
	{
		_id = id;
		_chooseType = jumpChooseType;
	}

	public void addNode(JumpPoint node)
	{
		_nodes.put(node.getId(), node);
	}

	public JumpPoint getNode(int id)
	{
		return _nodes.get(id);
	}

	public Collection<JumpPoint> getPoints()
	{
		return _nodes.values();
	}

	public int getId()
	{
		return _id;
	}

	public JumpChooseType getChooseType()
	{
		return _chooseType;
	}

	public abstract JumpTrack getParent();
}
