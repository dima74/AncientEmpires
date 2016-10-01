package ru.ancientempires;

import android.view.SurfaceHolder;

import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.draws.inputs.InputMain;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.PlayerType;

public class GameThread extends BaseThread {

	volatile public boolean needUpdateCampaign = false;
	public InputMain inputMain;

	public GameThread(GameActivity activity, SurfaceHolder surfaceHolder) {
		super(activity, surfaceHolder);
		drawMain = new DrawMain(activity);
		inputMain = new InputMain(activity, (DrawMain) drawMain);
	}

	@Override
	public void beforeRun() {
		activity.invalidateOptionsMenu();
		activity.game.campaign.start();
		if (drawMain.isActiveGame()) {
			MyAssert.a(drawMain.game.currentPlayer.type == PlayerType.PLAYER);
			inputMain.beginTurn();
		}
	}

	@Override
	public void onRun() {
		if (needUpdateCampaign) {
			needUpdateCampaign = false;
			drawMain.game.campaign.update();
		}
	}

	@Override
	public void afterRun() {
		if (drawMain.isActiveGame())
			drawMain.saveScreenCenter();
		// thread = null;
		try {
			Client.client.stopGame();
		} catch (Exception e) {
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
}
