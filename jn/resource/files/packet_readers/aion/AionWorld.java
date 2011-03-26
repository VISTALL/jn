package packet_readers.aion;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CTreeIntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;
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
import packet_readers.aion.infos.AionLoc;
import packet_readers.aion.infos.AionNpc;
import packet_readers.aion.listeners.AionNpcInfoListener;
import packet_readers.aion.listeners.AionPlayerInfoListener;

/**
 * @author VISTALL
 * @date 13:56/15.02.2011
 */
public class AionWorld implements IPacketListener
{
	public class SaveActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				SimpleDateFormat format = new SimpleDateFormat("HH.mm.ss dd.MM.yyyy");
				File npcFile = new File("./saves/npcs-" + format.format(System.currentTimeMillis()) + ".xml");
				FileWriter writer = new FileWriter(npcFile);
				writer.write("<list>\n");
				for(AionNpc npc : valuesNpc())
				{
					if(!npc.isValid())
						continue;
					writer.write("\t<npc id=\"" + npc.getNpcId() + "\" max_hp=\"" + npc.getMaxHP() + "\" name_id=\"" + npc.getNameId() + "\" title_id=\"" + npc.getTitleId() + "\" state=\"" + npc.getNpcState() + "\">\n");
					for(AionLoc loc : npc.getLocs())
					{
						writer.write("\t\t<loc x=\"" + loc.getX() + "\" y=\"" + loc.getY() + "\" z=\"" + loc.getZ() + "\" h=\"" + loc.getH() + "\" world_id=\"" + loc.getWorldId() + "\" static_object_id=\"" + loc.getStaticId() +"\" />\n");
					}
					writer.write("\t</npc>\n");
				}
				writer.write("</list>");
				writer.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public class ClearActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			_npcInfos.clear();
			_npcInfosByNpcId.clear();
			_npcList.setListData(new Object[]{});
			updateInfo(null);
		}
	}

	public class ListSelectionListenerImpl implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			AionNpc npc = (AionNpc) _npcList.getSelectedValue();
			updateInfo(npc);
		}
	}

	public class OnSelectTargetChangeListenerImpl implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e)
		{
			_onSelectTarget = true;
		}
	}

	public class AllChangeListenerImpl implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e)
		{
			_onSelectTarget = false;
		}
	}

	private static final String[] DIRs =
	{
		"./saves/"
	};

	private IntObjectMap<AionNpc> _npcInfos = new TreeIntObjectMap<AionNpc>();
	private IntObjectMap<AionNpc> _npcInfosByNpcId = new CTreeIntObjectMap<AionNpc>();

	private List<IPacketListener> _listeners = new ArrayList<IPacketListener>(5);
	private int _worldId;

	private boolean _onSelectTarget;

	private JList _npcList;
	private JLabel _maxHpLabel;
	private JLabel _levelLabel;
	private JLabel _nameIdLabel;
	private JLabel _titleIdLabel;
	private JLabel _npcStateLabel;

	public AionWorld()
	{
		for(String st : DIRs)
			new File(st).mkdir();

		_listeners.add(new AionNpcInfoListener(this));
		_listeners.add(new AionPlayerInfoListener(this));

		ClientStringHolder.getInstance();
	}

	@Override
	public List<JRibbonBand> getRibbonBands()
	{
		JRibbonBand aionWorldBand = new JRibbonBand("Aion World", new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));
		aionWorldBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(aionWorldBand));

		aionWorldBand.startGroup("Search Type");

		final JRadioButton allRadio = new JRadioButton("All");
		allRadio.setSelected(true);
		allRadio.addChangeListener(new AllChangeListenerImpl());

		final JRadioButton onClick = new JRadioButton("Target");
		onClick.addChangeListener(new OnSelectTargetChangeListenerImpl());

		aionWorldBand.addRibbonComponent(new JRibbonComponent(allRadio));
		aionWorldBand.addRibbonComponent(new JRibbonComponent(onClick));

		ButtonGroup b = new ButtonGroup();
		b.add(allRadio);
		b.add(onClick);

		aionWorldBand.startGroup();

		final JCommandButton saveButton = new JCommandButton(Bundle.getString("Save"), ImageStatic.SAVE_48x48);
		saveButton.addActionListener(new SaveActionListener());
		aionWorldBand.addCommandButton(saveButton, RibbonElementPriority.TOP);

		final JCommandButton clearButton = new JCommandButton(Bundle.getString("Clear"), ImageStatic.EXIT_48x48);
		clearButton.addActionListener(new ClearActionListener());
		aionWorldBand.addCommandButton(clearButton, RibbonElementPriority.TOP);

		aionWorldBand.startGroup();

		_maxHpLabel = new JLabel();
		_levelLabel = new JLabel();
		_nameIdLabel = new JLabel();
		_titleIdLabel = new JLabel();
		_npcStateLabel = new JLabel();

		_maxHpLabel.setPreferredSize(new Dimension(93, 10));
		_nameIdLabel.setPreferredSize(new Dimension(93, 10));
		_titleIdLabel.setPreferredSize(new Dimension(93, 10));
		_levelLabel.setPreferredSize(new Dimension(93, 10));
		_npcStateLabel.setPreferredSize(new Dimension(93, 10));

		updateInfo(null);

		aionWorldBand.addRibbonComponent(new JRibbonComponent(_maxHpLabel));
		aionWorldBand.addRibbonComponent(new JRibbonComponent(_nameIdLabel));
		aionWorldBand.addRibbonComponent(new JRibbonComponent(_titleIdLabel));
		aionWorldBand.addRibbonComponent(new JRibbonComponent(_npcStateLabel));
		aionWorldBand.addRibbonComponent(new JRibbonComponent(_levelLabel));

		aionWorldBand.startGroup();

		_npcList = new JList();
		_npcList.addListSelectionListener(new ListSelectionListenerImpl());

		final JScrollPane scrollPane1 = new JScrollPane(_npcList);
		scrollPane1.setPreferredSize(new Dimension(70, 200));

		aionWorldBand.addRibbonComponent(new JRibbonComponent(scrollPane1), 3);

		return Collections.singletonList(aionWorldBand);
	}

	private void updateInfo(AionNpc npc)
	{
		if(npc == null)
		{
			_maxHpLabel.setText("Max HP: 0");
			_levelLabel.setText("Level: 0");
			_nameIdLabel.setText("NameID: 0");
			_titleIdLabel.setText("TitleID: 0");
			_npcStateLabel .setText("Npc State: 0");
		}
		else
		{
			_maxHpLabel.setText("Max HP: " + npc.getMaxHP());
			_levelLabel.setText("Level: " + npc.getLevel());
			_nameIdLabel.setText("NameID: " + npc.getNameId());
			_titleIdLabel.setText("TitleID: " + npc.getTitleId());
			_npcStateLabel.setText("Npc State: "+ npc.getNpcState());
		}
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
		//
	}

	public boolean isOnSelectTarget()
	{
		return _onSelectTarget;
	}

	public int getWorldId()
	{
		return _worldId;
	}

	public void setWorldId(int worldId)
	{
		_worldId = worldId;
	}

	//===========================================================================================
	// 				Npcs
	//===========================================================================================

	public void addNpc(int obj, AionNpc npc)
	{
		_npcInfos.put(obj, npc);
	}

	public void addNpcByNpcId(int npcId, AionNpc npc)
	{
		if(_npcInfosByNpcId.containsKey(npcId))
			return;

		_npcInfosByNpcId.put(npcId, npc);
		_npcList.setListData(_npcInfosByNpcId.values().toArray());
	}

	public AionNpc getNpc(int obj)
	{
		return _npcInfos.get(obj);
	}

	public AionNpc getNpcByNpcId(int npcId)
	{
		return _npcInfosByNpcId.get(npcId);
	}

	public Collection<AionNpc> valuesNpc()
	{
		return _npcInfosByNpcId.values();
	}
}
