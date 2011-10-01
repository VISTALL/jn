package packet_readers.aion.listeners;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.session.Session;
import com.jds.jn.util.Bundle;
import com.jds.jn.util.ImageStatic;
import com.jds.swing.SimpleResizableIcon;
import packet_readers.aion.AionAbstractListener;
import packet_readers.aion.AionWorld;
import packet_readers.aion.infos.AionLoc;
import packet_readers.aion.infos.AionNpc;

/**
 * @author VISTALL
 * @date 14:16/15.02.2011
 */
public class AionNpcInfoListener extends AionAbstractListener
{
	public static final String TEMPLATE =
			"INSERT INTO spawns (spawn_id, npc_id, npc_name, map_id, x, y, z, heading, spawn_time, walker_id, random_walk, static_id, fly, respawn_time ) VALUES " +
			"(SPAWN_ID, %d, \"NULL\", %d, %f, %f, %f, %d, 'ALL', 0, 0, %d, 0, 295);\n";

	public class ListSelectionListenerImpl implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			AionNpc npc = (AionNpc) _npcList.getSelectedValue();
			updateInfo(npc);
		}
	}

	public class ChangeListenerImpl implements ChangeListener
	{
		private boolean _val;

		public ChangeListenerImpl(boolean val)
		{
			_val = val;
		}

		@Override
		public void stateChanged(ChangeEvent e)
		{
			if(_world != null)
				_world.setOnSelectTarget(_val);
		}
	}

	public class SaveActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(_world == null)
				return;

			try
			{
				FileWriter writer = new FileWriter(getLogFile("npcs-", "xml"));
				writer.write("<list>\n");
				for(AionNpc npc : _world.valuesNpc())
				{
					if(!npc.isValid())
						continue;
					writer.write("\t<npc id=\"" + npc.getNpcId() + "\" max_hp=\"" + npc.getMaxHP() + "\" name_id=\"" + npc.getNameId() + "\" title_id=\"" + npc.getTitleId() + "\" state=\"" + npc.getNpcState() + "\" />\n");
					/*for(AionLoc loc : npc.getLocs())
					{
						writer.write("\t\t<loc x=\"" + loc.getX() + "\" y=\"" + loc.getY() + "\" z=\"" + loc.getZ() + "\" h=\"" + loc.getH() + "\" world_id=\"" + loc.getWorldId() + "\" static_object_id=\"" + loc.getStaticId() +"\" />\n");
					}
					writer.write("\t</npc>\n");   */
				}
				writer.write("</list>");
				writer.close();

				writer = new FileWriter(getLogFile("npcs-", "sql"));
				for(AionNpc npc :  _world.valuesNpc())
				{
					if(!npc.isValid())
						continue;
					for(AionLoc loc : npc.getLocs())
						writer.write(String.format(TEMPLATE, npc.getNpcId(), loc.getWorldId(), loc.getX(), loc.getY(), loc.getZ(), loc.getH(), loc.getStaticId()));
				}
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
			
			if(_world != null)
				_world.clear();
			_npcList.setListData(new Object[]{});
			updateInfo(null);
		}
	}

	private JList _npcList;
	private JLabel _maxHpLabel;
	private JLabel _levelLabel;
	private JLabel _nameIdLabel;
	private JLabel _titleIdLabel;
	private JLabel _npcStateLabel;

	private JRadioButton _onTargetRadioButton;

	public AionNpcInfoListener()
	{

	}

	@Override
	public List<JRibbonBand> getRibbonBands()
	{
		JRibbonBand aionWorldBand = new JRibbonBand("Aion World", new SimpleResizableIcon(RibbonElementPriority.TOP, 32, 32));
		aionWorldBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(aionWorldBand));

		aionWorldBand.startGroup("Search Type");

		final JRadioButton allRadio = new JRadioButton("All");
		allRadio.setSelected(true);
		allRadio.addChangeListener(new ChangeListenerImpl(false));

		final JRadioButton _onTargetRadioButton = new JRadioButton("Target");
		_onTargetRadioButton.addChangeListener(new ChangeListenerImpl(true));

		aionWorldBand.addRibbonComponent(new JRibbonComponent(allRadio));
		aionWorldBand.addRibbonComponent(new JRibbonComponent(_onTargetRadioButton));

		ButtonGroup b = new ButtonGroup();
		b.add(allRadio);
		b.add(_onTargetRadioButton);

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
	public List<String> getPackets()
	{
		return Collections.singletonList("SM_NPC_INFO");
	}

	@Override
	public void invokeImpl(DecryptedPacket p)
	{
		int objId = p.getInt("objId");
		AionNpc npc = _world.getNpc(objId);
		if(npc == null)
			_world.addNpc(objId, npc = new AionNpc(p));

		AionNpc ownerNpc = _world.getNpcByNpcId(npc.getNpcId());
		if(ownerNpc == null)
		{
			_world.addNpcByNpcId(npc.getNpcId(), ownerNpc = npc);
			_npcList.setListData(_world.valuesNpc().toArray());
		}

		if(!_world.isOnSelectTarget())
			npc.setValid(true);

		float x = p.getFloat("x");
		float y = p.getFloat("y");
		float z = p.getFloat("z");
		int npcHeading = p.getInt("npcHeading");
		int spawnStaticId = p.getInt("spawnStaticId");

		AionLoc loc = new AionLoc(_world.getWorldId(), x, y, z, npcHeading, spawnStaticId);
		ownerNpc.addLoc(objId, loc);
	}

	@Override
	public AionWorld getWorld(Session session)
	{
		AionWorld world = session.getVar(AionWorld.class);
		if(world == null)
			session.setVar(AionWorld.class, world = new AionWorld());

		_world.setOnSelectTarget(_onTargetRadioButton.isSelected());
		return world;
	}
}
