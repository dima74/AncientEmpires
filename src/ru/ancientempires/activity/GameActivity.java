package ru.ancientempires.activity;

import ru.ancientempires.R;
import ru.ancientempires.UnitBuyDialog;
import ru.ancientempires.client.Client;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.server.ClientServer;
import ru.ancientempires.view.GameView;
import ru.ancientempires.view.algortihms.InputAlgoritmMain;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends Activity
{
	
	public static GameView	gameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		startGameView();
	}
	
	private void startGameView()
	{
		GameActivity.gameView = new GameView(this);
		GameActivity.gameView.gameActivity = this;
		setContentView(GameActivity.gameView);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
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
		else if (id == R.id.action_reset)
		{
			ClientServer.server.startGame(Client.getClient().gamePath);
			startGameView();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void buyUnit(InputAlgoritmMain main, UnitType[] units, boolean[] isAvailable)
	{
		DialogFragment dialogFragment = new UnitBuyDialog(units, isAvailable).setNoticeListener(main);
		dialogFragment.show(getFragmentManager(), "UnitBuyDialog");
	}
	
}
