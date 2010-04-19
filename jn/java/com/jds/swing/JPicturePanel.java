package com.jds.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: VISTALL
 * I not a author. @author Rebellion
 * Company: J Develop Station
 * Date: 31.08.2009
 * Time: 22:04:42
 */
public class JPicturePanel extends JPanel
{
	private BufferedImage originalImage;
	private Image image;

	public JPicturePanel()
	{
		initComponents();
	}

	private void initComponents()
	{
		setLayout(null);
		addComponentListener(new java.awt.event.ComponentAdapter()
		{
			@Override
			public void componentResized(java.awt.event.ComponentEvent evt)
			{
				formComponentResized();
			}
		});
	}

	private void formComponentResized()
	{
		int w = this.getWidth();
		int h = this.getHeight();
		if ((originalImage != null) && (w > 0) && (h > 0))
		{
			image = originalImage.getScaledInstance(w, h, getScalingMode());
			image.flush();
			repaint();
		}
	}

	@Override
	public void paint(Graphics g)
	{
		if (image != null)
		{
			g.drawImage(image, 0, 0, null);
		}

		super.paintChildren(g);
		super.paintBorder(g);
	}

	public BufferedImage getImage()
	{
		return originalImage;
	}

	public void setImage(BufferedImage image)
	{
		originalImage = image;
		int w = this.getWidth();
		int h = this.getHeight();
		if ((originalImage != null) && (w > 0) && (h > 0))
		{
			this.image = originalImage.getScaledInstance(w, h, getScalingMode());
			getParent().repaint();
			originalImage.flush();
		}
	}

	public Dimension getImageSize()
	{
		if (originalImage == null)
		{
			return new Dimension();
		}
		return new Dimension(originalImage.getWidth(), originalImage.getHeight());
	}


	public void setValidPreferredSize(Dimension d)
	{
		setPreferredSize(d);
		d = new Dimension(d);
		d.setSize(d.getWidth() + 19, d.getHeight() + 19);
		getParent().setPreferredSize(d);
	}

	public Image getPaintedImage()
	{
		return image;
	}

	private int getScalingMode()
	{
		return Image.SCALE_DEFAULT;
	}
}