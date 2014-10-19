package ru.ancientempires;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.ALog;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.gamelife.GameLifeStartActivity;
import ru.ancientempires.images.Images;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends Activity
{
	
	public static Context	context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		String string = getText(R.string.app_name).toString();
		
		setContentView(R.layout.activity_main);
		
		MainActivity.context = getBaseContext();
		
		if (savedInstanceState == null)
		{
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
			
			// мой код
			MyLog.currLog = new ALog();
			
			long s = System.nanoTime();
			init();
			long e = System.nanoTime();
			
			String text = String.format("инит %s секунд", (e - s) / 1000000000.0f);
			// Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
			MainActivity.text += text += "\n";
		}
	}
	
	private void init()
	{
		System.out.println();
		
		long s1 = System.nanoTime();
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
		long e1 = System.nanoTime();
		String text1 = String.format("zip %s секунд", (e1 - s1) / 1000000000.0f);
		MainActivity.text += text1 += "\n";
		// Toast.makeText(getBaseContext(), text1, Toast.LENGTH_LONG).show();
		
		long s2 = System.nanoTime();
		try
		{
			Client.init();
		}
		catch (IOException e)
		{
			// TODO оповещение пользователю, что что-то не так + вывод сообщения
			e.printStackTrace();
		}
		long e2 = System.nanoTime();
		String text2 = String.format("Client.init() %s секунд", (e2 - s2) / 1000000000.0f);
		MainActivity.text += text2 += "\n";
		// Toast.makeText(getBaseContext(), text2, Toast.LENGTH_LONG).show();
		
		long s3 = System.nanoTime();
		try
		{
			Images.preloadResources(Client.imagesZipFile);
			GameView.initResources(getResources());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		long e3 = System.nanoTime();
		String text3 = String.format("images %s секунд", (e3 - s3) / 1000000000.0f);
		// Toast.makeText(getBaseContext(), text3, Toast.LENGTH_LONG).show();
		MainActivity.text += text3 += "\n";
	}
	
	public ZipFile getZipFileFromAssets(AssetManager assets, String name) throws IOException
	{
		InputStream inputStream = assets.open(name);
		File zipFileOutput = new File(getBaseContext().getExternalCacheDir(), name);
		FileOutputStream fileOutputStream = new FileOutputStream(zipFileOutput);
		
		// TODO расширить, если файл > 1МБ
		byte[] buffer = new byte[1024 * 1024];
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
				long s = System.nanoTime();
				GameView.startGame(Client.getClient().getGame());
				long e = System.nanoTime();
				
				GameActivity.title = String.valueOf((e - s) / 1000000000.0f);
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
	
	public static String	text	= "";
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends DialogFragment
	{
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			TextView textView = (TextView) rootView.findViewById(R.id.text_hello_word);
			textView.setText(MainActivity.text);
			return rootView;
		}
		
	}
	
}
