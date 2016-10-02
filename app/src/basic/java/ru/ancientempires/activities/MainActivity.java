package ru.ancientempires.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import ru.ancientempires.MenuActions;
import ru.ancientempires.R;
import ru.ancientempires.actions.ActionGameEndTurn;
import ru.ancientempires.campaign.CampaignImmediately;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.ii.II;
import ru.ancientempires.model.Game;

public class MainActivity extends BaseListActivity {

	public static int     skirmish    = 5;
	public static int     campaign    = 0;
	public static String  gameToStart = "skirmish";
	public static boolean firstStart  = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public MenuActions[] getStrings() {
		return new MenuActions[] {
				MenuActions.PLAY,
				MenuActions.ONLINE,
				MenuActions.SETTINGS,
				MenuActions.MAP_EDITOR,
				MenuActions.INSTRUCTIONS,
				MenuActions.AUTHORS
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int itemId = item.getItemId();
		if (itemId == R.id.action_show_application_details)
			startApplicationDetailsActivity(this);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (getStrings()[position]) {
			case PLAY:
				moveTo(PlayMenuActivity.class);
				break;
			case MAP_EDITOR:
				moveTo(EditorBaseActivity.class);
				break;
			case ONLINE:
				new Thread() {
					@Override
					public void run() {
						try {
							Client.client.finishPart2();

							Game game = Client.client.allGames.get("skirmish.5").loadGame(true);
							game.path.isBaseGame = false;
							//Thread.sleep(1000);

							//Debug.startMethodTracing();
							for (int i = 0; i < 10; i++)
								game.ii.turnFull(game);
							game.path.isBaseGame = true;
							//Debug.stopMethodTracing();

							//сделать method tracing, пока что всегда только "try to exit form method ... while in method ...", мб попробовать в эмуляторе
							//или просто поискать method profiling на десктопе

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(MainActivity.this, "end", Toast.LENGTH_LONG).show();
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
							MyAssert.a(false);
						}
					}
				}.start();
				break;
			default:
				break;
		}
	}

	public static void testII(String gameID) throws Exception {
		Game game = Client.client.startGame(gameID);
		game.campaign.iDrawCampaign = new CampaignImmediately(game);
		game.campaign.start();

		for (int i = 0; i < 1; i++) {
			//System.out.println("i = " + i);
			if (game.campaign.isDefault)
				new II(game.rules).turnFull(game);
			else
				new ActionGameEndTurn().perform(game);
			game.campaign.update();
			new II(game.rules).turnFull(game);
			game.campaign.update();
		}
		if (!game.players[0].units.isEmpty())
			System.out.println(String.format("!!! units not empty %s (%d)", gameID, game.players[0].units.size()));

		game.saver.finishSave();
		//testLoadGame(game.path.gameID, true);
	}

	public static void testLoadGame(String gameID, boolean reload) throws Exception {
		Game game = Client.client.startGame(gameID);
		game.campaign.iDrawCampaign = new CampaignImmediately(game);
		game.campaign.start();
		game.saver.finishSave();

		if (reload)
			testLoadGame(game.path.gameID, false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (firstStart) {
			if ("info".equals(gameToStart))
				startApplicationDetailsActivity(this);
			else if ("editor".equals(gameToStart))
				moveTo(EditorBaseActivity.class);
			else if ("test".equals(gameToStart))
				moveTo(TestActivity.class);
			else if (gameToStart != "")
				moveTo(PlayMenuActivity.class);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
