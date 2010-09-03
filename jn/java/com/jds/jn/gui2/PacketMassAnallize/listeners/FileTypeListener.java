package com.jds.jn.gui2.PacketMassAnallize.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jds.jn.gui2.PacketMassAnallize.PacketMassAnalysisDialog;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:45:01/03.09.2010
 */
public class FileTypeListener implements ActionListener
{
	private PacketMassAnalysisDialog _dialog;

	public FileTypeListener(PacketMassAnalysisDialog dialog)
	{
		_dialog = dialog;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
   		_dialog.toggleFileTypes();
	}
}
