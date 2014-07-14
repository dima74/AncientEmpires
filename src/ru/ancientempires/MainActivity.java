package ru.ancientempires;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import ru.ancientempires.gamelife.GameLifeStartActivity;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import client.Client;

public class MainActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState == null)
		{
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
			
			// мой код
			init();
		}
	}
	
	private void init()
	{
		try
		{
			// копируем файл games.zip из assets в externalStorageCacheDir для удобной работы с ним (использование плюшек ZipFile).
			
			ZipInputStream zipInputStream = new ZipInputStream(getAssets().open("games.zip"));
			File zipFileOutput = new File(getBaseContext().getExternalCacheDir(), "games.zip");
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileOutput));
			
			int b;
			
			ZipEntry zipEntry;
			while ((zipEntry = zipInputStream.getNextEntry()) != null)
			{
				zipOutputStream.putNextEntry(zipEntry);
				if (!zipEntry.isDirectory())
					while ((b = zipInputStream.read()) != -1)
						zipOutputStream.write(b);
				zipOutputStream.closeEntry();
				
				System.out.print(zipEntry);
				System.out.println();
			}
			
			zipInputStream.close();
			zipOutputStream.close();
			
			// скопировали - предаем клиенту, чтобы разобрал на части
			
			ZipFile zipFile = new ZipFile(new File(getBaseContext().getExternalCacheDir(), "games.zip"));
			Client.setGameInputStream(zipFile);
		}
		catch (IOException e)
		{
			e.printStackTrace(); // какая-то странная ошибка тут
		}
		
		Client.init();
		GameView.initResources(getResources());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
			return true;
		if (id == R.id.action_game_life)
		{
			Intent intent = new Intent();
			intent.setClass(this, GameLifeStartActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_ancient_empires)
		{
			Intent intent = new Intent();
			intent.setClass(this, GameActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends DialogFragment
	{
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			
			return rootView;
		}
	}
	
}
