package ru.ancientempires.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ru.ancientempires.Strings;
import ru.ancientempires.framework.Debug;

public class BaseActivity extends AppCompatActivity {

	public static void startApplicationDetailsActivity(Context context) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package", "ru.ancientempires", null);
		intent.setData(uri);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Debug.onCreate(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Debug.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Debug.onStop(this);
	}

	@Override
	public void onBackPressed() {
		moveTo(MainActivity.class);
	}

	public void moveTo(Class<? extends Activity> activity) {
		moveTo(activity, new Intent());
	}

	public void moveTo(Class<? extends Activity> activity, Intent extra) {
		startActivity(new Intent(this, activity).putExtras(extra));
		finish();
	}

	public void setText(int textId, Strings string) {
		((TextView) findViewById(textId)).setText(string.toString());
	}

	public void setHint(int textId, String string) {
		((EditText) findViewById(textId)).setHint(string.toString());
	}

	public String getValue(int id) {
		EditText editText = (EditText) findViewById(id);
		String text = editText.getText().toString();
		return text.isEmpty() ? editText.getHint().toString() : text;
	}

	public int getIntValue(int id) {
		return Integer.valueOf(getValue(id));
	}

	public String getValue(View view, int id) {
		EditText editText = (EditText) view.findViewById(id);
		String text = editText.getText().toString();
		return text.isEmpty() ? editText.getHint().toString() : text;
	}

	public int getIntValue(View view, int id) {
		return Integer.valueOf(getValue(view, id));
	}
}
