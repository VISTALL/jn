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

/**
 * @author VISTALL
 * @date 12:03/12.02.2012
 */
public enum L2AcquireType
{
	NORMAL,   // 0
	FISHING,   // 1
	CLAN,      // 2
	SUB_UNIT,   // 3
	TRANSFORMATION, // 4
	CERTIFICATION,   // 5
	COLLECTION,       // 6
	TRANSFER_CARDINAL,  // 7
	TRANSFER_EVA_SAINTS, // 8
	TRANSFER_SHILLIEN_SAINTS, // 9
	FISHING_NON_DWARF; //10

	public static final L2AcquireType[] VALUES = values();
}
