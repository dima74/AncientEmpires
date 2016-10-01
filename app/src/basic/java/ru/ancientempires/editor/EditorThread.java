package ru.ancientempires.editor;

import android.view.SurfaceHolder;

import ru.ancientempires.BaseThread;
import ru.ancientempires.activities.EditorActivity;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GameSaver;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;

public class EditorThread extends BaseThread {

	public EditorChooseView view;
	public EditorInputMain  inputMain;

	public EditorThread(EditorActivity activity, SurfaceHolder surfaceHolder) {
		super(activity, surfaceHolder);
		drawMain = new EditorDrawMain(activity);
		inputMain = new EditorInputMain(activity, (EditorDrawMain) drawMain);

		drawMain.cells.update();
		drawMain.cellsDual.update();
		drawMain.units.update();
		drawMain.buildingSmokes.update();
	}

	@Override
	public void beforeRun() {
		// thread = this;
	}

	@Override
	public void onRun() {
		if (view != null)
			view.postInvalidate();
	}

	@Override
	public void afterRun() {
		// thread = null;
		try {
			activity.game.trimPlayers();
			activity.game.currentPlayer = null;
			eraseDefaults(activity.game);
			activity.game.setScreenCenters();
			GameSaver.createBaseGame(activity.game);
		} catch (Exception e) {
			MyAssert.a(false);
			e.printStackTrace();
		}
	}

	private void eraseDefaults(Game game) {
		game.currentTurn = null;
		game.allowedUnits = null;
		game.random = null;
		for (Player player : game.players) {
			player.color = null;
			player.type = null;
			player.gold = null;
			player.unitsLimit = null;
			player.team = null;
		}
	}

}
