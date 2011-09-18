package com.jds.swing;

import javax.swing.JProgressBar;

import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.cpp.com.COMRuntime;
import org.bridj.cpp.com.shell.ITaskbarList3;

/**
 * @author VISTALL
 * @date 23:42/24.04.2011
 */
public class JWin7ProgressBar extends JProgressBar
{
	public static interface WindowsComponent
	{
		Pointer<Integer> HWND();
	}

	private WindowsComponent _windowsComponent;
	private ITaskbarList3 _taskbarList3;

	public JWin7ProgressBar(WindowsComponent f)
	{
		if(Platform.isWindows7())
		{
			try
			{
				_taskbarList3 = COMRuntime.newInstance(ITaskbarList3.class);
			}
			catch(ClassNotFoundException e)
			{
				//
			}
			_windowsComponent = f;
		}
	}

	@Override
	public void setValue(int v)
	{
		if(_taskbarList3 != null)
			_taskbarList3.SetProgressValue(_windowsComponent.HWND(), v, getMaximum());

		super.setValue(v);
	}

	@Override
	public void setVisible(boolean aFlag)
	{
		setValue(0);
		setMaximum(100);

		if(_taskbarList3 != null)
			_taskbarList3.SetProgressState(_windowsComponent.HWND(), aFlag ? ITaskbarList3.TbpFlag.TBPF_NORMAL : ITaskbarList3.TbpFlag.TBPF_NOPROGRESS);

		super.setVisible(aFlag);
	}

}
