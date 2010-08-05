package com.jds.swing;

import java.awt.*;

import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 13:52:26
 */
public class SimpleResizableIcon implements ResizableIcon
{
	private int currWidth;
	private int currHeight;
	private int origWidth;
	private int origHeight;
	private RibbonElementPriority priority;

	public SimpleResizableIcon(RibbonElementPriority priority, int startWidth, int startHeight)
	{
		this.origWidth = startWidth;
		this.origHeight = startHeight;
		this.priority = priority;
		this.currWidth = startWidth;
		this.currHeight = startHeight;
	}

	public void setDimension(Dimension newDimension)
	{
		this.currWidth = newDimension.width;
		this.currHeight = newDimension.height;
	}

	public int getIconHeight()
	{
		return this.currHeight;
	}

	public int getIconWidth()
	{
		return this.currWidth;
	}

	public void setHeight(int height)
	{
		double coef = height / this.currHeight;
		this.currWidth = (int) (coef * this.currWidth);
		this.currHeight = height;
	}

	public void setWidth(int width)
	{
		double coef = width / this.currWidth;
		this.currHeight = (int) (coef * this.currHeight);
		this.currWidth = width;
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int rx = this.currWidth / 3;
		int ry = this.currHeight / 3;
		int cx = x + this.currWidth / 2 - 1;
		int cy = y + this.currHeight / 2 - 1;
		Color color = null;
		switch (this.priority)
		{
			case TOP:
				color = new Color(0, 0, 128);
				break;
			case MEDIUM:
				color = Color.blue;
				break;
			case LOW:
				color = new Color(128, 128, 255);
		}

		graphics.setColor(color);
		graphics.fillOval(cx - rx, cy - ry, 2 * rx, 2 * ry);
		graphics.setColor(color.darker());
		graphics.drawOval(cx - rx, cy - ry, 2 * rx, 2 * ry);
		graphics.drawRect(x, y, this.currWidth - 2, this.currHeight - 2);

		graphics.dispose();
	}

	public void revertToOriginalDimension()
	{
		this.currHeight = this.origHeight;
		this.currWidth = this.origWidth;
	}
}