package ru.ancientempires.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;

public class SettingsActivity extends BasePreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		update();
	}

	private void update() {
		Preference preference = findPreference("deleteSaves");
		preference.setSummary("" + Client.client.numberSaves());
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				deleteSavesAsync();
				return true;
			}
		});

		Preference appInfo = findPreference("appInfo");
		appInfo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				intent.setData(Uri.parse("package:ru.ancientempires"));
				startActivity(intent);
				return true;
			}
		});
	}

	public void deleteSavesAsync() {
		new AsyncTask<Void, Float, Void>() {
			public int numberSaves = Client.client.numberSaves();

			@Override
			protected void onPreExecute() {
				Toast.makeText(SettingsActivity.this, "Число сохранений:  " + numberSaves, Toast.LENGTH_SHORT).show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				deleteSaves();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				Toast.makeText(SettingsActivity.this, "Удалено: " + numberSaves, Toast.LENGTH_SHORT).show();
				update();
			}
		}.execute();
	}

	public void deleteSaves() {
		try {
			Client.client.save.deleteAll();
		} catch (Exception e) {
			MyAssert.a(false);
			e.printStackTrace();
		}
	}

}
