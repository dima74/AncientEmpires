package ru.ancientempires;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import ru.ancientempires.gamelife.GameLifeStartActivity;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.AssetManager;
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
		System.out.println();
		
		try
		{
			// копируем файл games.zip из assets в externalStorageCacheDir для удобной работы с ним (использование плюшек ZipFile).
			ZipFile gameZipFile = getZipFileFromAssets(getAssets(), "games.zip");
			Client.setGamesZipFile(gameZipFile);
			
			// то же самое с rules.zip
			ZipFile rulesZipFile = getZipFileFromAssets(getAssets(), "rules.zip");
			Client.setRulesZipFile(rulesZipFile);
			
			// то же самое с images.zip
			ZipFile imagesZipFile = getZipFileFromAssets(getAssets(), "images.zip");
			Client.setImagesZipFile(imagesZipFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			Client.init();
		}
		catch (IOException e)
		{
			// TODO оповещение пользователю, что что-то не так + вывод сообщения
			e.printStackTrace();
		}
		
		try
		{
			GameView.initResources(getResources());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public ZipFile getZipFileFromAssets(AssetManager assets, String name) throws IOException
	{
		InputStream inputStream = assets.open(name);
		File zipFileOutput = new File(getBaseContext().getExternalCacheDir(), name);
		FileOutputStream fileOutputStream = new FileOutputStream(zipFileOutput);
		
		// TODO расширить, если файл > 1МБ
		byte[] buffer = new byte[1000 * 1000];
		int length = inputStream.read(buffer);
		fileOutputStream.write(buffer, 0, length);
		
		fileOutputStream.close();
		
		ZipFile zipFile = new ZipFile(new File(getBaseContext().getExternalCacheDir(), name));
		return zipFile;
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
			Client.getClient().startGame("first");
			try
			{
				GameView.startGame(Client.getClient().getGame());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
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
