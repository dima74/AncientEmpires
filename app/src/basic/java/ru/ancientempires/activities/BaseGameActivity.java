package ru.ancientempires.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.ViewGroup;

import ru.ancientempires.BaseThread;
import ru.ancientempires.BaseView;
import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;

public class BaseGameActivity extends BaseActivity {

	public BaseView     view;
	public Game         game;
	public BaseThread   thread;
	public BaseDrawMain drawMain;

	public Dialog dialog;

	public BaseView getView() {
		return view;
	}

	public BaseThread getThread() {
		return thread;
	}

	public BaseDrawMain getDrawMain() {
		return drawMain;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivity.firstStart = false;
	}

	@Override
	protected void onStop() {
		super.onStop();
		getThread().isRunning = false;
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		try {
			getThread().join();
		} catch (InterruptedException e) {
			MyAssert.a(false);
			e.printStackTrace();
		}
		if (view != null) {
			ViewGroup viewGroup = (ViewGroup) view.getParent();
			MyAssert.a(viewGroup != null);
			if (viewGroup != null)
				viewGroup.removeView(view);
		}
		game = null;
		// activity = null;
	}

	public void vibrate() {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(400);
	}

	private static final String KEY_SCALE = "KEY_SCALE";

	public float getDensity() {
		return getResources().getDisplayMetrics().density;
	}

	public float getMapScale() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (preferences.contains(KEY_SCALE))
			return preferences.getFloat(KEY_SCALE, 0);

		float mapScale = getDensity() * (4.5f / 3f);
		setMapScale(mapScale);
		return mapScale;
	}

	public void setMapScale(float mapScale) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.edit().putFloat(KEY_SCALE, mapScale).commit();
	}
}
