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
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;

public interface IDrawCampaign
{

	void dialogIntro(Bitmap image, String text, ScriptDialogIntro script);

	void dialog(Bitmap image, String text, ScriptDialog script);

	void dialog(String text, ScriptDialogWithoutImage script);

	void dialogTarget(String textTitle, String textTarget, ScriptDialogTarget script);

	void toastTitle(String text, Script script);

	void delay(int milliseconds, ScriptDelay script);

	//
	void showBlackScreen(ScriptShowBlackScreen script);

	void hideBlackScreen(ScriptHideBlackScreen script);

	void blackScreen(ScriptBlackScreen script);

	//
	void hideCursor(ScriptHideCursor script);

	void showCursor(ScriptShowCursor script);

	void setCursorPosition(int i, int j, ScriptSetCursorPosition script);

	//
	void setCameraSpeed(int delta, ScriptSetCameraSpeed script);

	void cameraMove(AbstractScriptOnePoint script);

	void setMapPosition(float i, float j);

	void focusOnCurrentPlayerCenter();

	//
	void setUnitSpeed(int framesForCell, ScriptSetUnitSpeed script);

	void unitMove(ScriptUnitMove script, boolean initFromStart);

	//
	void unitCreate(int i, int j, UnitType unitType, Player player, ScriptUnitCreate script);

	void removeUnit(int i, int j, ScriptRemoveUnit script);

	void unitChangePosition(int i, int j, int iNew, int jNew, ScriptUnitChangePosition script);

	void unitAttack(int i, int j, ScriptUnitAttack script);

	void unitDie(int i, int j, ScriptUnitDie script);

	void citadelAttack(ScriptOnePoint script);

	//
	void sparksDefault(int i, int j, ScriptSparkDefault script);

	void sparksAttack(int i, int j, ScriptSparkAttack script);

	void cellAttackPartTwo(int i, int j, ScriptCellAttackPartTwo script);

	//
	void enableActiveGame(ScriptEnableActiveGame script);

	void disableActiveGame(ScriptDisableActiveGame script);

	void gameOver(ScriptGameOver script);

	void closeMission() throws Exception;

	void hideInfoImmediately(Script script);

	void vibrate();

	void updateCampaign();

	void snakeMap(ScriptSnakeMap script);

}
