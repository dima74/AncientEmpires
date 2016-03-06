package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.framework.MyAssert;

public class DrawToast extends DrawOnFrames
{
	
	public String	text;
	public int		duration;
					
	public DrawToast(String text, int duration)
	{
		this.text = text;
		this.duration = duration;
		animate(1);
	}
	
	@Override
	public void onStart()
	{
		GameActivity.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast toast = Toast.makeText(GameActivity.activity, text, duration);
				TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
				MyAssert.a(view != null);
				view.setGravity(Gravity.CENTER);
				toast.show();
			}
		});
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{}
	
}
