package com.jds.swing;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * User: VISTALL
 * I not a author. @author INTERNET
 * Company: J Develop Station
 * Date: 18.09.2009
 * Time: 18:55:12
 */
public class JDirectoryChooser extends JComponent
{
	private JTextField folderPath;
	private JLabel messageLabel;
	private JButton select;
	private JButton cancel;
	private File selectedFolder;
	private boolean fileSelected;
	private boolean showHidden;
	private JTree tree;
	private JDialog dialog;

	public JDirectoryChooser(JDialog parent)  //
	{
		this.dialog = new JDialog(parent, ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("ChooseFolder"), true);
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		this.messageLabel = new JLabel(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("ChooseFolder"));
		northPanel.add(this.messageLabel);
		northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		centerPanel.setLayout(new BorderLayout(10, 10));
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("My Computer");
		DefaultTreeModel rootModel = new DefaultTreeModel(rootNode, true);
		File[] rootDrive = File.listRoots();
		DefaultMutableTreeNode[] rootDriveTree = new DefaultMutableTreeNode[rootDrive.length];

		for (int i = 0; i < rootDrive.length; ++i)
		{
			rootDriveTree[i] = new DefaultMutableTreeNode(rootDrive[i]);
			rootNode.add(rootDriveTree[i]);
		}

		DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();
		cellRenderer.setLeafIcon(cellRenderer.getClosedIcon());

		this.tree = new JTree(rootModel);
		this.tree.setCellRenderer(cellRenderer);
		//this.tree.setRootVisible(false);
		this.tree.setExpandsSelectedPaths(true);
		this.tree.putClientProperty("JTree.lineStyle", "Angled");

		centerPanel.add(new JScrollPane(this.tree), "Center");
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout(10, 10));
		namePanel.add(new JLabel(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Folder")), "West");
		namePanel.add(this.folderPath = new JTextField(), "Center");
		centerPanel.add(namePanel, "South");
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.select = new JButton(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Select"));
		this.select.setMnemonic('S');
		this.cancel = new JButton(ResourceBundle.getBundle("com/jds/jn/resources/bundle/LanguageBundle").getString("Cancel"));
		this.cancel.setMnemonic('C');

		southPanel.setLayout(new FlowLayout(2, 10, 10));
		southPanel.add(this.select);
		southPanel.add(this.cancel);

		this.dialog.getContentPane().setLayout(new BorderLayout());
		this.dialog.getContentPane().add(northPanel, "North");
		this.dialog.getContentPane().add(centerPanel, "Center");
		this.dialog.getContentPane().add(southPanel, "South");

		this.dialog.setSize(325, 325);
		this.dialog.setLocationRelativeTo(parent);
		this.dialog.getRootPane().setDefaultButton(this.select);

		this.tree.addTreeWillExpandListener(new TreeWillExpandListener()
		{
			public void treeWillExpand(TreeExpansionEvent e)
			{
				TreePath selectionPath = e.getPath();
				if (selectionPath == null)
				{
					return;
				}
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
				int i = selectionPath.getPathCount();
				StringBuffer temp = new StringBuffer();
				for (int j = 1; j < i - 1; ++j)
				{
					temp.append(selectionPath.getPathComponent(j) + "/");
				}
				temp.append(selectionPath.getPathComponent(i - 1));
				JDirectoryChooser.this.selectedFolder = new File(temp.toString());
				JDirectoryChooser.this.folderPath.setText(selectionPath.getPathComponent(i - 1).toString());
				if (node.getChildCount() > 0)
				{
					node.removeAllChildren();
				}
				JDirectoryChooser.this.createDriveTree(new File(temp.toString()), node);
				JDirectoryChooser.this.tree.setSelectionPath(selectionPath);
				JDirectoryChooser.this.tree.scrollPathToVisible(selectionPath);
			}

			public void treeWillCollapse(TreeExpansionEvent e)
			{
				TreePath selectionPath = JDirectoryChooser.this.tree.getSelectionPath();
				if (selectionPath == null)
				{
					return;
				}
				int i = selectionPath.getPathCount();
				StringBuffer temp = new StringBuffer();
				for (int j = 1; j < i - 1; ++j)
				{
					temp.append(selectionPath.getPathComponent(j) + "/");
				}
				temp.append(selectionPath.getPathComponent(i - 1));
				JDirectoryChooser.this.selectedFolder = new File(temp.toString());
				JDirectoryChooser.this.folderPath.setText(selectionPath.getPathComponent(i - 1).toString());
				JDirectoryChooser.this.tree.setSelectionPath(selectionPath);
			}

		});
		this.select.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (JDirectoryChooser.this.tree.getSelectionPath() == null)
				{
					return;
				}
				JDirectoryChooser.this.fileSelected = true;
				JDirectoryChooser.this.dialog.setVisible(false);
			}

		});
		this.cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JDirectoryChooser.this.fileSelected = false;
				JDirectoryChooser.this.dialog.setVisible(false);
			}

		});
		this.dialog.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				JDirectoryChooser.this.fileSelected = false;
				JDirectoryChooser.this.dialog.dispose();
			}
		});
	}

	private void createDriveTree(File parentFile, DefaultMutableTreeNode parentNode)
	{
		File[] childFiles = parentFile.listFiles();
		if (childFiles == null)
		{
			return;
		}
		Arrays.sort(childFiles);
		if (childFiles == null)
		{
			return;
		}
		for (File childFile : childFiles)
		{
			if (!(childFile.isDirectory()))
			{
				continue;
			}
			if (this.showHidden)
			{
				parentNode.add(new DefaultMutableTreeNode(childFile.getName()));
			}
			else
			{
				if (childFile.isHidden())
				{
					continue;
				}
				parentNode.add(new DefaultMutableTreeNode(childFile.getName()));
			}
		}
	}

	public boolean showDirectoryDialog()
	{
		this.dialog.setVisible(true);
		this.dialog.dispose();
		return this.fileSelected;
	}

	public File getSelectedFolder()
	{
		return this.selectedFolder;
	}

	public void setTitle(String title)
	{
		this.dialog.setTitle(title);
	}

	public void setMessage(String message)
	{
		this.messageLabel.setText(message);
	}

	public void setApproveButtonText(String text)
	{
		this.select.setText(text);
	}

	public void setCancelButtonText(String text)
	{
		this.cancel.setText(text);
	}

	public void setApproveButtonMnemonic(char c)
	{
		this.select.setMnemonic(c);
	}

	public void setCancelButtonMnemonic(char c)
	{
		this.cancel.setMnemonic(c);
	}

	public void showHiddenFolders(boolean showHidden)
	{
		this.showHidden = showHidden;
	}
}
