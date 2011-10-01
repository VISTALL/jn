package packet_readers.lineage2.infos;

import java.util.HashSet;
import java.util.Set;

/**
 * @author VISTALL
 * @date 2:21/01.10.2011
 */
public class L2DialogObject extends L2Object
{
	private final Set<L2DialogInfo> _dialogs = new HashSet<L2DialogInfo>();

	public void addDialog(L2DialogInfo t)
	{
		_dialogs.add(t);
	}

	public Set<L2DialogInfo> getDialogs()
	{
		return _dialogs;
	}
}
