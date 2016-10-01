package ru.ancientempires.activities;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import ru.ancientempires.Extras;
import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GamesFolder;

public class LevelMenuActivity extends BaseListActivity {

	public static LevelMenuActivity activity;

	public  GamesFolder currentFolder;
	private boolean     isStartingGameInProcess;
	private String      folderID;

	@Override
	public void loadBackground() throws Exception {
		super.loadBackground();
		Client.client.finishPart1();
	}

	@Override
	protected void onStart() {
		super.onStart();
		activity = this;
		isStartingGameInProcess = false;
	}

	@Override
	public String[] getStrings() {
		folderID = getIntent().getStringExtra(Extras.FOLDER_ID);
		MyAssert.a(folderID != null);
		currentFolder = Client.client.allFolders.get(folderID);
		MyAssert.a(currentFolder.name != null);
		setTitle(currentFolder.name);

		ArrayList<String> names = new ArrayList<>();
		for (GamePath game : currentFolder.games)
			names.add(currentFolder.getName(names.size(), game));
		for (String name : names)
			MyAssert.a(name != null);
		return names.toArray(new String[names.size()]);
	}

	@Override
	public void onLoadFinished() {
		super.onLoadFinished();
		if (MainActivity.gameToStart != "" && MainActivity.firstStart) {
			int i = "campaign".equals(folderID) ? MainActivity.campaign : "skirmish".equals(folderID) ? MainActivity.skirmish : 0;
			onListItemClick(null, null, i, 0);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (isStartingGameInProcess)
			return;
		isStartingGameInProcess = true;
		boolean canChooseTeams = GameActivity.startGame(this, currentFolder.games.get(position).gameID, null);
		if (!canChooseTeams)
			finish();
	}

	@Override
	public void onBackPressed() {
		moveTo(PlayMenuActivity.class);
	}

	@Override
	protected void onStop() {
		super.onStop();
		activity = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
			return true;
		return super.onOptionsItemSelected(item);
	}
}
