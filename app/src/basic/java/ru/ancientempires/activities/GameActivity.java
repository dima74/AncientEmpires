package ru.ancientempires.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.ancientempires.Extras;
import ru.ancientempires.GameThread;
import ru.ancientempires.GameView;
import ru.ancientempires.MyAsyncTask;
import ru.ancientempires.R;
import ru.ancientempires.actions.ActionUnitMove;
import ru.ancientempires.actions.campaign.ActionCampaignRemoveUnit;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.draws.inputs.InputMain;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.helpers.ActionHelper;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Unit;

public class GameActivity extends BaseGameActivity {

	public String baseGameID;
	public String gameID;

	@Override
	public GameView getView() {
		return (GameView) view;
	}

	@Override
	public GameThread getThread() {
		return (GameThread) thread;
	}

	@Override
	public DrawMain getDrawMain() {
		return (DrawMain) drawMain;
	}

	public InputMain getInputMain() {
		return getDrawMain().inputMain;
	}

	public void postUpdateCampaign() {
		getThread().needUpdateCampaign = true;
	}

	public static boolean startGame(Activity activity, String gameID, String lastTeams) {
		GamePath path = Client.getGame(gameID);

		Intent intent = new Intent();
		if (!path.canChooseTeams)
			intent.setClass(activity, GameActivity.class);
		else
			intent.setClass(activity, PlayersConfigureActivity.class)
					.putExtra(Extras.LAST_PLAYERS, lastTeams);
		intent.putExtra(Extras.GAME_ID, gameID);
		activity.startActivity(intent);
		return path.canChooseTeams;
	}

	public void startGame(String gameID, String lastTeams) {
		startGame(this, gameID, lastTeams);
		finish();
	}

	public void restartGame() {
		try {
			String lastTeams = game.path.loadGame(0, false, null).toJson().get("players").toString();
			startGame(game.path.baseGameID, lastTeams);
		} catch (Exception e) {
			MyAssert.a(false);
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseGameID = getIntent().getStringExtra(Extras.GAME_ID);
		if (savedInstanceState != null)
			gameID = savedInstanceState.getString("gameID");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("gameID", gameID);
	}

	@Override
	protected void onStart() {
		super.onStart();
		new MyAsyncTask(this) {
			@Override
			public void doInBackground() throws Exception {
				Client.client.finishPart2();
				String players = getIntent().getStringExtra(Extras.PLAYERS);
				game = Client.client.startGame(gameID == null ? baseGameID : gameID, players);
				gameID = game.path.gameID;
			}

			@Override
			public void onPostExecute() {
				setTitle(game.path.name);
				view = new GameView(GameActivity.this);
				setContentView(view);
			}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		boolean isActiveGame = getDrawMain() != null
				&& getThread().drawMain != null
				&& getThread().drawMain.isActiveGame();
		if (isActiveGame)
			getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.action_wisp);
		if (item != null && game != null && game.currentPlayer != null && getDrawMain() != null)
			item.setVisible(new ActionHelper(game).isUnitActive(getDrawMain().cursorDefault.cursorI, getDrawMain().cursorDefault.cursorJ) && game.checkFloating());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int itemId = item.getItemId();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				switch (itemId) {
					case android.R.id.home:
						MyLog.l("GameActivity up");
						finish();
						break;
					case R.id.action_end_turn:
						((GameThread) view.thread).inputMain.endTurn(true);
						break;
					case R.id.action_reset:
						restartGame();
						break;
					case R.id.action_wisp:
						int i = getDrawMain().cursorDefault.cursorI;
						int j = getDrawMain().cursorDefault.cursorJ;
						new ActionUnitMove()
								.setIJ(i, j)
								.setTargetIJ(i, j)
								.perform(game);
						getDrawMain().units.update();
						if (getInputMain().inputPlayer.inputUnit.isActive)
							getInputMain().inputPlayer.inputUnit.tap(i, j);
						getInputMain().inputPlayer.inputUnit.start(i, j);
						break;
					case R.id.action_kill_unit:
						while (!game.players[1].units.isEmpty()) {
							Unit unit = game.players[1].units.get(0);
							new ActionCampaignRemoveUnit().setIJ(unit.i, unit.j).perform(game);
						}
						getDrawMain().units.update();
						getThread().needUpdateCampaign = true;
						break;
					case R.id.action_capture_castle:
						CellType type = game.rules.getCellType("CASTLE");
						for (Cell[] line : game.fieldCells)
							for (Cell cell : line)
								if (cell.type == type && cell.player != null && cell.player.ordinal == 1) {
									cell.player = game.players[0];
									getThread().needUpdateCampaign = true;
								}
						break;
				}
			}
		};
		synchronized (view.thread) {
			view.thread.runOnGameThread(runnable);
		}
		return true;
	}
}