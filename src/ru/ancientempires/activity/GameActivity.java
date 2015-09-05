package ru.ancientempires.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ru.ancientempires.R;
import ru.ancientempires.UnitBuyDialog;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.action.handlers.UnitHelper;
import ru.ancientempires.client.Client;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Unit;
import ru.ancientempires.server.ClientServer;
import ru.ancientempires.view.GameView;
import ru.ancientempires.view.algortihms.InputAlgorithmMain;

public class GameActivity extends Activity
{
	
	public static GameView		gameView;
	public static GameActivity	gameActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		GameActivity.gameActivity = this;
		startGameView();
	}
	
	public void startGameView()
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
		else if (id == R.id.action_exit)
			android.os.Process.killProcess(android.os.Process.myPid());
		else if (id == R.id.action_kill_unit)
		{
			while (!GameHandler.game.players[1].units.isEmpty())
			{
				Unit unit = GameHandler.game.players[1].units.get(0);
				unit.health = 0;
				UnitHelper.checkDied(unit);
				GameActivity.gameView.thread.gameDraw.gameDrawUnit.update(GameHandler.game);
				GameActivity.gameView.thread.gameDraw.inputAlgorithmMain.tapWithoutAction(unit.i, unit.j);
				GameActivity.gameView.thread.gameDraw.focusOnCell(unit.i, unit.j);
				GameActivity.gameView.thread.needUpdateCampaign = true;
			}
			return true;
		}
		else if (id == R.id.action_capture_castle)
		{
			CellType type = CellType.getType("CASTLE");
			for (Cell[] line : GameHandler.fieldCells)
				for (Cell cell : line)
					if (cell.type == type && cell.player != null && cell.player.ordinal == 1)
					{
						cell.player = GameHandler.game.players[0];
						GameActivity.gameView.thread.needUpdateCampaign = true;
					}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void buyUnit(InputAlgorithmMain main, Unit[] units, boolean[] isAvailable)
	{
		DialogFragment dialogFragment = new UnitBuyDialog(units, isAvailable).setNoticeListener(main);
		dialogFragment.show(getFragmentManager(), "UnitBuyDialog");
	}
	
}
