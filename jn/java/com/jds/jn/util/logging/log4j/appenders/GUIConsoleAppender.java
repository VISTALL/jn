package com.jds.jn.util.logging.log4j.appenders;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import com.jds.jn.gui.forms.ConsoleForm;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  11:41:33/20.07.2010
 *
 * @see ConsoleAppender
 */
public class GUIConsoleAppender extends ConsoleAppender
{

	public GUIConsoleAppender()
	{
	}

	public GUIConsoleAppender(Layout layout)
	{
		this(layout, SYSTEM_OUT);
	}

	public GUIConsoleAppender(Layout layout, String target)
	{
		setLayout(layout);
		setTarget(target);
		activateOptions();
	}

	@Override
	public void append(LoggingEvent event)
	{
		ConsoleForm.getInstance().log(event);

		super.append(event);
	}
}
