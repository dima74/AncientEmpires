package ru.ancientempires.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import ru.ancientempires.framework.Debug;

public abstract class BasePreferenceActivity extends PreferenceActivity {

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

}
