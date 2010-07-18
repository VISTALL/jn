package com.jds.jn.gui.listeners;

import com.jds.jn.Jn;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 04/01/2010
 * Time: 13:06:21
 */
public class WindowMoveAdapter implements MouseListener, MouseMotionListener
{
	private Point _location;
	private MouseEvent _pressed;

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		_pressed = e;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Jn.getForm().repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		_location = Jn.getForm().getLocation(_location);
		int x = _location.x - _pressed.getX() + e.getX();
		int y = _location.y - _pressed.getY() + e.getY();
		Jn.getForm().setLocation(x, y);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{

	}
}
