package ru.ancientempires.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import ru.ancientempires.Strings;
import ru.ancientempires.framework.Debug;

public class BaseActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Debug.onCreate(this);
	}
	
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
	
	@Override
	public void onBackPressed()
	{
		moveTo(MainActivity.class);
	}
	
	public void moveTo(Class<? extends Activity> activity)
	{
		moveTo(activity, new Intent());
	}
	
	public void moveTo(Class<? extends Activity> activity, Intent extra)
	{
		startActivity(new Intent(this, activity).putExtras(extra));
		finish();
	}
	
	public void setText(int textId, Strings string)
	{
		((TextView) findViewById(textId)).setText(string.toString());
	}
	
	public void setHint(int textId, String string)
	{
		((EditText) findViewById(textId)).setHint(string.toString());
	}
	
	public String getValue(int id)
	{
		EditText editText = (EditText) findViewById(id);
		String text = editText.getText().toString();
		return text.isEmpty() ? editText.getHint().toString() : text;
	}
	
	public int getIntValue(int id)
	{
		return Integer.valueOf(getValue(id));
	}
	
}
