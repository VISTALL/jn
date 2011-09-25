package com.jds.jn.util;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.jdesktop.swingx.icon.EmptyIcon;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import com.jds.jn.parser.parttypes.PartType;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 21/11/2009
 * Time: 15:19:19
 */
public class ImageStatic
{
	public static ResizableIcon START_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/start.png"), new Dimension(15, 15));
	public static ResizableIcon STOP_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/stop.png"), new Dimension(15, 15));
	public static ResizableIcon SAVE_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/fsave.png"), new Dimension(15, 15));
	public static ResizableIcon FILE_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/fopen.png"), new Dimension(15, 15));
	public static ResizableIcon SEARCH_PACKET_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/searchPacket.png"), new Dimension(15, 15));
	public static ResizableIcon EXIT_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/exit.png"), new Dimension(15, 15));
	public static ResizableIcon INFO_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/info.png"), new Dimension(15, 15));

	public static ResizableIcon PROGRAM_SET_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/pset.png"), new Dimension(15, 15));
	public static ResizableIcon PROGRAM_SET_24x24 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/pset2.png"), new Dimension(15, 15));
	public static ResizableIcon NETWORK_SET_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/nset.png"), new Dimension(15, 15));
	public static ResizableIcon NETWORK_SET_24x24 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/nset2.png"), new Dimension(15, 15));

	public static ResizableIcon DOC_24x24 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/JnDoc24.png"), new Dimension(15, 15));
	public static ResizableIcon HELP_48x48 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/help.png"), new Dimension(15, 15));

	public static ResizableIcon JN_ICON_32 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/icon_32.png"), new Dimension(32, 32));
	public static ResizableIcon JN_ICON_16 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/icon_16.png"), new Dimension(16, 16));
	public static ResizableIcon JN_ICON_64 = ImageWrapperResizableIcon.getIcon(ImageStatic.class.getResource("/com/jds/jn/resources/nimg/icon_64.png"), new Dimension(64, 64));


	public static final ImageIcon ICON_PLUS = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/add.png"));
	public static final ImageIcon ICON_DEL = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/dell.png"));
	public static final ImageIcon ICON_EDIT = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/runToCursor.png"));
	public static final ImageIcon ICON_CHANGE = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/change.png"));
	public static final ImageIcon ICON_PACKET = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/packet.png"));
	public static final ImageIcon WARNING = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/warning.png"));
	public static final ImageIcon PART_ID = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/id.png"));

	public static final ImageIcon CLOSE_ICON = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/close.png"));
	public static final ImageIcon OPEN_ICON = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/open.png"));

	public static final ImageIcon FOLDER_OPEN = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/folder.png"));
	public static final ImageIcon FOLDER_CLOSE = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/folderOpen.png"));


	public static final ImageIcon PART_USHORT = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/ushort.png"));
	public static final ImageIcon PART_SHORT = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/short.png"));

	public static final ImageIcon PART_UINT = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/uint.png"));
	public static final ImageIcon PART_INT = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/int.png"));

	public static final ImageIcon PART_BYTE_ARRAY = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/byte[].png"));
	public static final ImageIcon PART_BYTE = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/byte.png"));
	public static final ImageIcon PART_UBYTE = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/ubyte.png"));

	public static final ImageIcon PART_NORMAL_STRING = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/NormalString.png"));
	public static final ImageIcon PART_DOUBLE = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/double.png"));
	public static final ImageIcon PART_FLOAT = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/float.png"));

	public static final ImageIcon PART_LONG = new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/part/long.png"));

	public static final Icon EMPTY_ICON = new EmptyIcon();

	public static final JLabel ICON_FROM_SERVER = new JLabel(new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/se_packet.png")));
	public static final JLabel ICON_FROM_CLIENT = new JLabel(new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/cl_packet.png")));

	public static final JLabel ICON_FROM_SERVER_ERROR = new JLabel(new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/se_packet_error.png")));
	public static final JLabel ICON_FROM_CLIENT_ERROR = new JLabel(new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/cl_packet_error.png")));

	public static final JLabel ICON_KEY_PACKET = new JLabel(new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/key_packet.png")));
	public static final JLabel ICON_SERVER_LIST_PACKET = new JLabel(new ImageIcon(ImageStatic.class.getResource("/com/jds/jn/resources/images/serverlist_packet.png")));

	private HashMap<PartType, ImageIcon> _iconMap;
	public static ImageStatic _instance;

	public static ImageStatic getInstance()
	{
		if (_instance == null)
		{
			_instance = new ImageStatic();
		}
		return _instance;
	}

	private ImageStatic()
	{
		_iconMap = new HashMap<PartType, ImageIcon>();
	}

	public void registerIcon(PartType type, ImageIcon icon)
	{
		_iconMap.put(type, icon);
	}

	public ImageIcon getIconForPartType(PartType type)
	{
		return _iconMap.get(type);
	}
}
