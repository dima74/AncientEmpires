package ru.ancientempires.activity;

import android.app.Activity;
import ru.ancientempires.framework.Debug;

public class BaseActivity extends Activity
{
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Debug.onStart(this);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Debug.onStop(this);
	}
	
}
