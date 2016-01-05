package ru.ancientempires.view.draws.campaign;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.campaign.scripts.Script;

public class MyDialogFragment
{
	
	public AlertDialog	dialog;
	public View			viewOverlay;
	public Script		script;
	
	public void showDialog(final Builder builder, Script script)
	{
		this.script = script;
		builder.setCancelable(false);
		GameActivity.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				dialog = builder.show();
				
				WindowManager.LayoutParams params = new WindowManager.LayoutParams(
						1, 1,
						LayoutParams.TYPE_APPLICATION,
						LayoutParams.FLAG_NOT_FOCUSABLE
								// | LayoutParams.FLAG_NOT_TOUCH_MODAL
								| LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
						PixelFormat.TRANSPARENT);
				viewOverlay = new View(GameActivity.activity);
				WindowManager windowManager = (WindowManager) GameActivity.activity.getSystemService(Context.WINDOW_SERVICE);
				windowManager.addView(viewOverlay, params);
				viewOverlay.setOnTouchListener(new OnTouchListener()
				{
					@Override
					public boolean onTouch(View v, MotionEvent event)
					{
						dialog.dismiss();
						((WindowManager) GameActivity.activity.getSystemService(Context.WINDOW_SERVICE)).removeView(viewOverlay);
						MyDialogFragment.this.script.finish();
						return false;
					}
				});
			}
		});
	}
	
}
