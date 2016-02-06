package ru.ancientempires.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import ru.ancientempires.GameView;
import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.Debug;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public class GameActivity extends Activity
{
	
	public static final String	EXTRA_GAME_ID			= "EXTRA_GAME_ID";
	public static final String	EXTRA_USE_LAST_TEAMS	= "EXTRA_USE_LAST_TEAMS";
	public static GameActivity	activity;
	public GameView				view;
	public Game					game;
	private String				baseGameID;
	private String				gameID;
	
	public static void startGame(String gameID, boolean useLastTeams)
	{
		GameActivity.startGame(GameActivity.activity, gameID, useLastTeams);
	}
	
	public static void startGame(final Activity activity, String gameID, boolean useLastTeams)
	{
		if (GameActivity.activity != null)
			GameActivity.activity.finish();
			
		GamePath path = Client.getGame(gameID);
		
		Intent intent = new Intent();
		if (!path.canChooseTeams)
			intent.setClass(activity, GameActivity.class);
		else
			intent.setClass(activity, PlayersChooseActivity.class)
					.putExtra(GameActivity.EXTRA_USE_LAST_TEAMS, useLastTeams);
		intent.putExtra(GameActivity.EXTRA_GAME_ID, gameID);
		activity.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Debug.create(this);
		GameActivity.activity = this;
		baseGameID = getIntent().getStringExtra(GameActivity.EXTRA_GAME_ID);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Debug.onStart(this);
		final ProgressDialog dialog = new ProgressDialog(GameActivity.activity);
		dialog.setMessage(getString(R.string.loading));
		dialog.setCancelable(false);
		dialog.show();
		
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					Client.client.finishPart2();
					game = Client.client.startGame(gameID == null ? baseGameID : gameID);
					gameID = game.path.gameID;
				}
				catch (Exception e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
				
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						dialog.dismiss();
						view = new GameView(GameActivity.this);
						setContentView(view);
					}
				});
			}
		}.start();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Debug.onStop(this);
		if (view != null)
			((ViewGroup) view.getParent()).removeView(view);
		view = null;
		try
		{
			Client.client.stopGame(false);
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		game = null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		boolean isActiveGame = view != null
				&& view.thread != null
				&& view.thread.drawMain != null
				&& view.thread.drawMain.isActiveGame;
		if (isActiveGame)
			getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId())
		{
			case android.R.id.home:
				MyLog.l("GameActivity up");
				finish();
				return true;
			case R.id.action_end_turn:
				view.thread.inputMain.endTurn(true);
				return true;
			case R.id.action_reset:
				GameActivity.startGame(game.path.baseGameID, true);
				return true;
			case R.id.action_kill_unit:
				while (!game.players[1].units.isEmpty())
				{
					Unit unit = game.players[1].units.get(0);
					unit.health = 0;
					new UnitHelper(game).checkDied(unit);
					view.thread.drawMain.gameDrawUnits.update();
					view.thread.drawMain.inputPlayer.tapWithoutAction(unit.i, unit.j);
					view.thread.drawMain.focusOnCell(unit.i, unit.j);
				}
				view.thread.needUpdateCampaign = true;
				return true;
			case R.id.action_capture_castle:
				CellType type = game.rules.getCellType("CASTLE");
				for (Cell[] line : game.fieldCells)
					for (Cell cell : line)
						if (cell.type == type && cell.player != null && cell.player.ordinal == 1)
						{
							cell.player = game.players[0];
							view.thread.needUpdateCampaign = true;
						}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}