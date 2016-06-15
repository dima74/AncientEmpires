package ru.ancientempires.campaign;

import android.graphics.Bitmap;

import ru.ancientempires.campaign.scripts.AbstractScriptOnePoint;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptCellAttackPartTwo;
import ru.ancientempires.campaign.scripts.ScriptDelay;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.campaign.scripts.ScriptDialogIntro;
import ru.ancientempires.campaign.scripts.ScriptDialogTarget;
import ru.ancientempires.campaign.scripts.ScriptDialogWithoutImage;
import ru.ancientempires.campaign.scripts.ScriptDisableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptEnableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptGameOver;
import ru.ancientempires.campaign.scripts.ScriptHideBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptHideCursor;
import ru.ancientempires.campaign.scripts.ScriptOnePoint;
import ru.ancientempires.campaign.scripts.ScriptRemoveUnit;
import ru.ancientempires.campaign.scripts.ScriptSetCameraSpeed;
import ru.ancientempires.campaign.scripts.ScriptSetCursorPosition;
import ru.ancientempires.campaign.scripts.ScriptSetMapPosition;
import ru.ancientempires.campaign.scripts.ScriptSetUnitSpeed;
import ru.ancientempires.campaign.scripts.ScriptShowBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptShowCursor;
import ru.ancientempires.campaign.scripts.ScriptSnakeMap;
import ru.ancientempires.campaign.scripts.ScriptSparkAttack;
import ru.ancientempires.campaign.scripts.ScriptSparkDefault;
import ru.ancientempires.campaign.scripts.ScriptUnitAttack;
import ru.ancientempires.campaign.scripts.ScriptUnitChangePosition;
import ru.ancientempires.campaign.scripts.ScriptUnitCreate;
import ru.ancientempires.campaign.scripts.ScriptUnitDie;
import ru.ancientempires.campaign.scripts.ScriptUnitMove;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveHandler;
import ru.ancientempires.client.Client;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;

public class CampaignImmediately implements IDrawCampaign
{
	
	public Game game;
	
	public CampaignImmediately(Game game)
	{
		this.game = game;
	}
	
	@Override
	public void dialogIntro(Bitmap image, String text, ScriptDialogIntro script)
	{
		script.finish();
	}
	
	@Override
	public void toastTitle(String text, Script script)
	{
		script.finish();
	}
	
	@Override
	public void dialog(Bitmap image, String text, ScriptDialog script)
	{
		script.finish();
	}
	
	@Override
	public void dialog(String text, ScriptDialogWithoutImage script)
	{
		script.finish();
	}
	
	@Override
	public void dialogTarget(String textTitle, String textTarget, ScriptDialogTarget script)
	{
		script.finish();
	}
	
	@Override
	public void delay(int milliseconds, ScriptDelay script)
	{
		script.finish();
	}
	
	@Override
	public void showBlackScreen(ScriptShowBlackScreen script)
	{
		script.finish();
	}
	
	@Override
	public void hideBlackScreen(ScriptHideBlackScreen script)
	{
		script.finish();
	}
	
	@Override
	public void blackScreen(ScriptBlackScreen script)
	{}
	
	@Override
	public void hideCursor(ScriptHideCursor script)
	{}
	
	@Override
	public void showCursor(ScriptShowCursor script)
	{}
	
	@Override
	public void setCameraSpeed(int delta, ScriptSetCameraSpeed script)
	{}
	
	@Override
	public void cameraMove(AbstractScriptOnePoint script)
	{
		script.finish();
	}
	
	@Override
	public void setMapPosition(int i, int j, ScriptSetMapPosition script)
	{}
	
	@Override
	public void setCursorPosition(int i, int j, ScriptSetCursorPosition script)
	{}
	
	@Override
	public void setUnitSpeed(int framesForCell, ScriptSetUnitSpeed script)
	{}
	
	@Override
	public void unitMove(ScriptUnitMove script, boolean initFromStart)
	{
		if (script.handlers != null)
			for (Script handler : script.handlers)
				((ScriptUnitMoveHandler) handler).complete = true;
		script.performAction();
		script.finish();
	}

	@Override
	public void unitCreate(int i, int j, UnitType unitType, Player player, ScriptUnitCreate script)
	{
		script.performAction();
	}

	@Override
	public void unitChangePosition(int i, int j, int iNew, int jNew, ScriptUnitChangePosition script)
	{
		script.performAction();
	}

	@Override
	public void removeUnit(int i, int j, ScriptRemoveUnit script)
	{
		script.performAction();
	}

	@Override
	public void unitAttack(int i, int j, ScriptUnitAttack script)
	{
		script.finish();
	}
	
	@Override
	public void unitDie(int i, int j, ScriptUnitDie script)
	{
		script.performAction();
		script.finish();
	}

	@Override
	public void citadelAttack(ScriptOnePoint script)
	{
		script.finish();
	}

	@Override
	public void sparksDefault(int i, int j, ScriptSparkDefault script)
	{
		script.finish();
	}
	
	@Override
	public void sparksAttack(int i, int j, ScriptSparkAttack script)
	{
		script.finish();
	}
	
	@Override
	public void cellAttackPartTwo(int i, int j, ScriptCellAttackPartTwo script)
	{
		script.finish();
	}
	
	@Override
	public void enableActiveGame(ScriptEnableActiveGame script)
	{
		script.performAction();
	}
	
	@Override
	public void disableActiveGame(ScriptDisableActiveGame script)
	{}
	
	@Override
	public void hideInfoImmediately(Script script)
	{}
	
	@Override
	public void gameOver(ScriptGameOver script)
	{
		script.finish();
	}
	
	@Override
	public void closeMission() throws Exception
	{
		Client.client.stopGame();
	}
	
	@Override
	public void vibrate()
	{}

	@Override
	public void snakeMap(ScriptSnakeMap script)
	{
		script.finish();
	}

	@Override
	public void updateCampaign()
	{
		game.campaign.update();
	}

}
