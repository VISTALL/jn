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

package packet_readers.lineage2.infos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @date 10:37/12.02.2012
 */
public class L2SkillLearn implements Comparable<L2SkillLearn>
{
	private int _id;
	private int _level;
	private int _minLevel;
	private int _cost;

	private List<L2ItemWithCount> _needItems = new ArrayList<L2ItemWithCount>(1);
	private List<L2SkillInfo> _needSkills = new ArrayList<L2SkillInfo>(1);

	public L2SkillLearn(int id, int level, int minLevel, int cost)
	{
		_id = id;
		_level = level;
		_minLevel = minLevel;
		_cost = cost;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof L2SkillLearn && ((L2SkillLearn) o)._id == _id && ((L2SkillLearn) o)._level == _level;
	}

	@Override
	public int compareTo(L2SkillLearn o)
	{
		if(_id == o._id)
			return 0;
		return o._id - _id;
	}

	public List<L2ItemWithCount> getNeedItems()
	{
		return _needItems;
	}

	public int getId()
	{
		return _id;
	}

	public int getLevel()
	{
		return _level;
	}

	public int getMinLevel()
	{
		return _minLevel;
	}

	public List<L2SkillInfo> getNeedSkills()
	{
		return _needSkills;
	}

	public int getCost()
	{
		return _cost;
	}
}
