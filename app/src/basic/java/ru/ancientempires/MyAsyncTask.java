package ru.ancientempires;

import android.app.Activity;
import android.app.ProgressDialog;

import ru.ancientempires.framework.MyAssert;

public abstract class MyAsyncTask extends Thread
{
	
	private ProgressDialog dialog;
	private Activity       activity;

	public MyAsyncTask(Activity activity)
	{
		this.activity = activity;
		dialog = new ProgressDialog(activity);
		dialog.setMessage(activity.getString(R.string.loading));
		dialog.setCancelable(false);
		dialog.show();
	}
	
	@Override
	public final void run()
	{
		try
		{
			doInBackground();
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				dialog.dismiss();
				onPostExecute();
			}
		});
	}
	
	public abstract void doInBackground() throws Exception;
	
	public void onPostExecute()
	{}
	
}
