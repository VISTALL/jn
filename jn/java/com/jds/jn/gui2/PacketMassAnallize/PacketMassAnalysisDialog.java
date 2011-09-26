package com.jds.jn.gui2.PacketMassAnallize;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jds.jn.gui.forms.MainForm;
import com.jds.jn.gui2.PacketMassAnallize.listeners.FileTypeListener;
import com.jds.jn.logs.Reader;
import com.jds.jn.logs.listeners.ReaderListener;
import com.jds.jn.logs.readers.AbstractReader;
import com.jds.jn.network.packets.CryptedPacket;
import com.jds.jn.network.packets.DecryptedPacket;
import com.jds.jn.session.Session;
import com.jds.jn.util.RunnableImpl;
import com.jds.jn.util.ThreadPoolManager;
import com.jds.swing.JDirectoryChooser;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  16:12:51/03.09.2010
 */
public class PacketMassAnalysisDialog extends JDialog
{
	private static final Logger _log = Logger.getLogger(PacketMassAnalysisDialog.class);

	private class ReaderListenerImpl implements ReaderListener
	{
		@Override
		public DecryptedPacket newPacket(Session session, CryptedPacket p)
		{
			return new DecryptedPacket(session, p.getPacketType(), p.getAllData(), p.getTime(), session.getProtocol(), false);
		}

		@Override
		public void readPacket(Session session, DecryptedPacket p)
		{
			session.receiveQuitPacket(p, false, true);
		}

		@Override
		public void readPacket(Session session, CryptedPacket p)
		{
			session.receiveQuitPacket(p, false);
		}

		@Override
		public void onFinish(Session session, File file)
		{
			if(session != null)
			{
				MainForm.getInstance().getProgressBar().setVisible(true);
				MainForm.getInstance().getProgressBar().setValue(0);
				int i = 1;
				int size = session.getCryptedPackets().size();
				for(CryptedPacket packet : session.getCryptedPackets())
				{
					DecryptedPacket decryptedPacket = session.decode(packet);
					session.receiveQuitPacket(decryptedPacket, true, true);

					int p = (int) ((100D * (i + 1)) / size);

					MainForm.getInstance().getProgressBar().setValue(p);
					i++;
				}

				MainForm.getInstance().getProgressBar().setValue(0);
				MainForm.getInstance().getProgressBar().setVisible(false);

				session.close();
			}

			_leftFiles.setText(String.valueOf(_files.size()));
			_progressBar1.setValue(_progressBar1.getMaximum() - _files.size());

			parseNext();
		}
	}

	private final ReaderListener LISTENER = new ReaderListenerImpl();

	private JTextField _dirText;
	private JButton _chooseDirectory;
	private JPanel rootPane;
	private JComboBox _fileTypes;
	private JButton _startButton;
	private JRadioButton _allFormats;
	private JRadioButton _chooseTypeRadioButton;
	private JProgressBar _progressBar1;
	private JLabel _fileTypesLabel;
	private JLabel _filesCount;
	private JLabel _leftFiles;
	private JCheckBox _withSubdirectories;
	private JLabel _currentFile;

	private File _directory;
	private Queue<File> _files;

	public PacketMassAnalysisDialog()
	{
		super(MainForm.getInstance());
		$$$setupUI$$$();
		setContentPane(rootPane);
		setResizable(true);
		setTitle("Mass Analysis");
		setSize(600, 350);
		setVisible(true);

		FileTypeListener fileTypeListener = new FileTypeListener(this);
		_allFormats.addActionListener(fileTypeListener);
		_chooseTypeRadioButton.addActionListener(fileTypeListener);
		for(Map.Entry<AbstractReader, FileFilter> entry : Reader.getInstance().getReaders())
		{
			_fileTypes.addItem(entry.getValue());
		}

		_chooseDirectory.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JDirectoryChooser c = new JDirectoryChooser(PacketMassAnalysisDialog.this);
				if(c.showDirectoryDialog())
				{
					_directory = c.getSelectedFolder();
					_dirText.setText(c.getSelectedFolder().getAbsolutePath());
					_startButton.setEnabled(true);
				}
			}
		});
		_startButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				File[] f = _directory.listFiles();
				_files = new ArrayDeque<File>(f.length);
				for(File file : f)
				{
					if(_withSubdirectories.isSelected())
					{
						read(_files, file);
					}
					else
					{
						if(file.isFile())
						{
							_files.add(file);
						}
					}
				}

				actionStart(true);
				_leftFiles.setText(String.valueOf(_files.size()));
				_filesCount.setText(String.valueOf(_files.size()));
				_progressBar1.setMaximum(_files.size());

				parseNext();
			}
		});
	}

	private void actionStart(boolean start)
	{
		_withSubdirectories.setEnabled(start);
		_allFormats.setEnabled(start);
		_chooseTypeRadioButton.setEnabled(start);
		_fileTypes.setEnabled(start);
		_fileTypesLabel.setEnabled(start);
	}

	private void parseNext()
	{
		File f = _files.poll();
		if(f == null)
		{
			_currentFile.setText("Finish");
			finish();
			return;
		}

		try
		{
			_currentFile.setText(f.getName());
			Reader.getInstance().read(f, LISTENER);
		}
		catch(Exception e)
		{
			LISTENER.onFinish(null, null);
			_log.info("Exception: " + e, e);
		}
	}

	private void finish()
	{
		ThreadPoolManager.getInstance().execute(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				//L2World.getInstance().close();
			}
		});
	}

	private void read(Queue<File> list, File file)
	{
		if(file.isDirectory())
		{
			for(File f : file.listFiles())
			{
				read(list, f);
			}
		}
		else
		{
			if(_allFormats.isSelected())
			{
				list.add(file);
			}
			else
			{
				FileFilter info = (FileFilter) _fileTypes.getSelectedItem();
				if(info.accept(file))
				{
					list.add(file);
				}
			}
		}
	}

	public void toggleFileTypes()
	{
		_fileTypes.setEnabled(_chooseTypeRadioButton.isSelected());
		_fileTypesLabel.setEnabled(_chooseTypeRadioButton.isSelected());
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$()
	{
		rootPane = new JPanel();
		rootPane.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
		rootPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		rootPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panel1.setBorder(BorderFactory.createTitledBorder("Directory"));
		_dirText = new JTextField();
		_dirText.setEditable(false);
		panel1.add(_dirText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(397, 22), null, 0, false));
		_chooseDirectory = new JButton();
		_chooseDirectory.setText("Choose Directory");
		panel1.add(_chooseDirectory, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
		rootPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panel2.setBorder(BorderFactory.createTitledBorder("Type Settings"));
		_allFormats = new JRadioButton();
		_allFormats.setSelected(true);
		_allFormats.setText("All Types");
		panel2.add(_allFormats, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		_chooseTypeRadioButton = new JRadioButton();
		_chooseTypeRadioButton.setSelected(false);
		_chooseTypeRadioButton.setText("Choose Type");
		panel2.add(_chooseTypeRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panel2.add(panel3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		_fileTypes = new JComboBox();
		_fileTypes.setEnabled(false);
		panel3.add(_fileTypes, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_fileTypesLabel = new JLabel();
		_fileTypesLabel.setEnabled(false);
		_fileTypesLabel.setText("File Type:");
		panel3.add(_fileTypesLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_withSubdirectories = new JCheckBox();
		_withSubdirectories.setSelected(true);
		_withSubdirectories.setText("With subdirectories?");
		panel2.add(_withSubdirectories, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		label1.setText("Current File:");
		panel2.add(label1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_currentFile = new JLabel();
		_currentFile.setText("");
		panel2.add(_currentFile, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
		rootPane.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panel4.setBorder(BorderFactory.createTitledBorder("Progress"));
		_startButton = new JButton();
		_startButton.setEnabled(false);
		_startButton.setText("Start");
		panel4.add(_startButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_progressBar1 = new JProgressBar();
		_progressBar1.setBorderPainted(true);
		panel4.add(_progressBar1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
		panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("All Files:");
		panel5.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer2 = new Spacer();
		panel5.add(spacer2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		_filesCount = new JLabel();
		_filesCount.setText("0");
		panel5.add(_filesCount, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label3 = new JLabel();
		label3.setText("Left Files:");
		panel5.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_leftFiles = new JLabel();
		_leftFiles.setText("0");
		panel5.add(_leftFiles, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		_fileTypesLabel.setLabelFor(_fileTypes);
		ButtonGroup buttonGroup;
		buttonGroup = new ButtonGroup();
		buttonGroup.add(_allFormats);
		buttonGroup.add(_chooseTypeRadioButton);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return rootPane;
	}
}
