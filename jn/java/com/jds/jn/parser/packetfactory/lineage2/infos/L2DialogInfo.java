package com.jds.jn.parser.packetfactory.lineage2.infos;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  20:50:12/31.07.2010
 */
public class L2DialogInfo
{
	private final int _questId;
	private final String _dialog;

	public L2DialogInfo(int questId, String dialog)
	{
		_questId = questId;
		_dialog = dialog;
	}

	public int getQuestId()
	{
		return _questId;
	}

	public String getDialog()
	{
		return _dialog;
	}

	@Override
	public boolean equals(Object b)
	{
		if(b instanceof L2DialogInfo)
		{
			return _dialog.equals(((L2DialogInfo) b).getDialog());
		}

		return false;
	}
}
