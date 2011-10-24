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

package packet_readers.lineage2.infos;

/**
 * @author VISTALL
 * @date 19:15/24.10.2011
 */
public enum L2ChatType
{
	ALL,  //0
	SHOUT, //1    !
	TELL,  //2    "
	PARTY,  //3   #
	CLAN,   //4    @
	GM,      //5
	PETITION_PLAYER, //6   used for petition
	PETITION_GM,    //7   * used for petition
	TRADE,          //8  +
	ALLIANCE,       //9   $
	ANNOUNCEMENT,    //10
	SYSTEM_MESSAGE,  //11
	L2FRIEND,
	MSNCHAT,
	PARTY_ROOM,    //14
	COMMANDCHANNEL_ALL, //15 ``
	COMMANDCHANNEL_COMMANDER,  //16  `
	HERO_VOICE,                //17 %
	CRITICAL_ANNOUNCE,   //18
	SCREEN_ANNOUNCE,
	BATTLEFIELD, //20   ^
	MPCC_ROOM,   //21 добавлен в епилоге, подобия PARTY_ROOM ток для СС
	NPC_ALL,  // 22 добавлен в ХФ, аналог  ALL, но может игнорироватся клиентом
	NPC_SHOUT;  // 23 добавлен в ХФ, аналог  SHOUT, но может игнорироватся клиентом

	public static final L2ChatType[] VALUES = values();
}
