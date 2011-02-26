package packet_readers.aion;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.parser.packetfactory.IPacketListener;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.ImageStatic;
import com.jds.swing.SimpleResizableIcon;
import packet_readers.aion.holders.ClientStringHolder;
import packet_readers.aion.listeners.AionNpcInfoListener;
import packet_readers.aion.listeners.AionPlayerInfoListener;

/**
 * @author VISTALL
 * @date 13:56/15.02.2011
 */
public class AionWorld implements IPacketListener
{
	static
	{
		ClientStringHolder.getInstance();
	}

	private static final String[] DIRs =
	{
		"./saves/"
	};

	private List<IPacketListener> _listeners = new ArrayList<IPacketListener>(5);
	private int _worldId;

	public AionWorld()
	{
		for(String st : DIRs)
			new File(st).mkdir();

		_listeners.add(new AionNpcInfoListener(this));
		_listeners.add(new AionPlayerInfoListener(this));
	}

	@Override
	public List<JRibbonBand> getRibbonBands()
	{
		JRibbonBand aionWorldBand = new JRibbonBand("Aion World", new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 15, 15));
		aionWorldBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(aionWorldBand));

		aionWorldBand.startGroup("Search Type");

		final JRadioButton allRadio = new JRadioButton("All");
		final JRadioButton onClick = new JRadioButton("Target");

		aionWorldBand.addRibbonComponent(new JRibbonComponent(allRadio));
		aionWorldBand.addRibbonComponent(new JRibbonComponent(onClick));

		ButtonGroup b = new ButtonGroup();
		b.add(allRadio);
		b.add(onClick);

		aionWorldBand.startGroup();

		final JCommandButton saveButton = new JCommandButton(Bundle.getString("Save"), ImageStatic.SAVE_48x48);
		aionWorldBand.addCommandButton(saveButton, RibbonElementPriority.TOP);

		final JCommandButton clearButton = new JCommandButton(Bundle.getString("Clear"), ImageStatic.EXIT_48x48);
		aionWorldBand.addCommandButton(clearButton, RibbonElementPriority.TOP);

		return Collections.singletonList(aionWorldBand);
	}

	@Override
	public void invoke(DecryptedPacket p)
	{
		if (p == null || p.getPacketInfo() == null || p.getName() == null || p.hasError())
		{
			return;
		}

		for (IPacketListener l : _listeners)
		{
			l.invoke(p);
		}
	}

	@Override
	public void close()
	{
		for (IPacketListener l : _listeners)
		{
			l.close();
		}
	}

	public int getWorldId()
	{
		return _worldId;
	}

	public void setWorldId(int worldId)
	{
		_worldId = worldId;
	}
}
