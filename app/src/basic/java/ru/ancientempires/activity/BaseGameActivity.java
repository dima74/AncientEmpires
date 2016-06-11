package ru.ancientempires.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;

import ru.ancientempires.BaseThread;
import ru.ancientempires.BaseView;
import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;

public class BaseGameActivity extends BaseActivity
{
	
	public static BaseGameActivity activity;

	public BaseView view;
	public Dialog   dialog;
	public Game     game;

	public BaseView getView()
	{
		return view;
	}
	
	public BaseThread getThread()
	{
		return view == null ? null : view.thread;
	}
	
	public BaseDrawMain getDrawMain()
	{
		return getThread() == null ? null : getThread().drawMain;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		MainActivity.firstStart = false;
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		getThread().isRunning = false;
		if (dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}
		try
		{
			getThread().join();
		}
		catch (InterruptedException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		if (view != null)
		{
			ViewGroup viewGroup = (ViewGroup) view.getParent();
			MyAssert.a(viewGroup != null);
			if (viewGroup != null)
				viewGroup.removeView(view);
		}
		game = null;
		// activity = null;
	}
	
}
