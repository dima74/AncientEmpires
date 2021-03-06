package ru.ancientempires.draws;

import android.graphics.Bitmap;

import ru.ancientempires.campaign.IDrawCampaign;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.campaign.scripts.ScriptDialogIntro;
import ru.ancientempires.campaign.scripts.ScriptDialogTarget;
import ru.ancientempires.campaign.scripts.ScriptDialogWithoutImage;
import ru.ancientempires.campaign.scripts.ScriptDisableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptGameOver;
import ru.ancientempires.campaign.scripts.ScriptSnakeMap;
import ru.ancientempires.draws.campaign.SimpleDialog;

public class DrawCampaign extends BaseDrawCampaign implements IDrawCampaign {

	public DrawCampaign(BaseDrawMain mainBase) {
		super(mainBase);
	}

	@Override
	public void dialogIntro(Bitmap bitmap, String text, ScriptDialogIntro script) {
		SimpleDialog.create(getGameActivity(), text, script);
	}

	@Override
	public void dialog(Bitmap bitmap, String text, ScriptDialog script) {
		SimpleDialog.create(getGameActivity(), text, script);
	}

	@Override
	public void dialog(String text, ScriptDialogWithoutImage script) {
		SimpleDialog.create(getGameActivity(), text, script);
	}

	@Override
	public void dialogTarget(String textTitle, String textTarget, ScriptDialogTarget script) {
		SimpleDialog.create(getGameActivity(), textTitle + "\n\n" + textTarget, script);
	}

	@Override
	public void toastTitle(final String text, final Script script) {
		SimpleDialog.create(getGameActivity(), text, script);
	}

	//
	@Override
	public void disableActiveGame(ScriptDisableActiveGame script) {
		main.isActiveGame = false;
		main.saveScreenCenter();
		script.performAction();
	}

	@Override
	public void gameOver(ScriptGameOver script) {
		System.out.println("Game Over!");
	}

	@Override
	public void vibrate() {}

	@Override
	public void snakeMap(ScriptSnakeMap script) {
		script.finish();
	}
}
