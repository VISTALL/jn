package com.jds.jn;

import javax.swing.JFrame;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import com.jds.jn.util.ImageStatic;
import com.jds.swing.SimpleResizableIcon;

/**
 * Author: VISTALL
 * Date:  10:55/21.12.2010
 */
public class JnTest
{
	public static void main(String... arg) throws Exception
	{
		JRibbonFrame frame = new JRibbonFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);

		RibbonApplicationMenu menu = new RibbonApplicationMenu();
		frame.getRibbon().setApplicationMenu(menu);



		frame.getRibbon().addTask(new RibbonTask("Main", files()));

		frame.setVisible(true);
	}

	public static JRibbonBand files()
	{
		JRibbonBand animationBand = new JRibbonBand("Files", new SimpleResizableIcon(RibbonElementPriority.MEDIUM, 30, 30));
		animationBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(animationBand));

		JCommandButton opnFile = new JCommandButton("Open", ImageStatic.FILE_48x48);
		JCommandButton searchPacket = new JCommandButton("Search Packet", ImageStatic.SEARCH_PACKET_48x48);

		animationBand.addCommandButton(opnFile, RibbonElementPriority.TOP);
		animationBand.addCommandButton(searchPacket, RibbonElementPriority.TOP);
		return animationBand;
	}
}
