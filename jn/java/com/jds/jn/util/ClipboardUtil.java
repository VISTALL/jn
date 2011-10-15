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

package com.jds.jn.util;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author VISTALL
 * @date 20:46/13.10.2011
 */
public class ClipboardUtil
{
	public static String getClipboard()
	{
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

		try
		{
			if(t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor))
				return (String) t.getTransferData(DataFlavor.stringFlavor);
		}
		catch(UnsupportedFlavorException e)
		{
		}
		catch(IOException e)
		{
		}
		return null;
	}

	public static void setClipboard(String str)
	{
		StringSelection ss = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}
}
