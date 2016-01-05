package ru.ancientempires;

import android.graphics.Bitmap;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptCellAttackPartTwo;
import ru.ancientempires.campaign.scripts.ScriptDelay;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.campaign.scripts.ScriptDialogWithoutImage;
import ru.ancientempires.campaign.scripts.ScriptDisableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptEnableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptGameOver;
import ru.ancientempires.campaign.scripts.ScriptHideBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptHideCursor;
import ru.ancientempires.campaign.scripts.ScriptIntro;
import ru.ancientempires.campaign.scripts.ScriptRemoveUnit;
import ru.ancientempires.campaign.scripts.ScriptSetCameraSpeed;
import ru.ancientempires.campaign.scripts.ScriptSetMapPosition;
import ru.ancientempires.campaign.scripts.ScriptSetUnitSpeed;
import ru.ancientempires.campaign.scripts.ScriptShowBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptShowCursor;
import ru.ancientempires.campaign.scripts.ScriptShowTarget;
import ru.ancientempires.campaign.scripts.ScriptSparkAttack;
import ru.ancientempires.campaign.scripts.ScriptSparkDefault;
import ru.ancientempires.campaign.scripts.ScriptUnitAttack;
import ru.ancientempires.campaign.scripts.ScriptUnitChangePosition;
import ru.ancientempires.campaign.scripts.ScriptUnitCreate;
import ru.ancientempires.campaign.scripts.ScriptUnitDie;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveExtended;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;

public interface IDrawCampaign
{
	
	public void showDialog(Bitmap image, String text, ScriptDialog script);
	
	public void showDialog(String text, ScriptDialogWithoutImage script);
	
	public void showTitle(final String text, Script script);
	
	public void showTarget(String textTitle, String textTarget, ScriptShowTarget script);
	
	public void showIntro(Bitmap image, String text, ScriptIntro script);
	
	public void cameraMove(int iEnd, int jEnd, Script script);
	
	public void hideCursor(ScriptHideCursor script);
	
	public void showCursor(ScriptShowCursor script);
	
	public void delay(int milliseconds, ScriptDelay script);
	
	public void unitDie(int i, int j, ScriptUnitDie script);
	
	public void unitAttack(int i, int j, ScriptUnitAttack script);
	
	public void unitCreate(int i, int j, UnitType unitType, Player player, ScriptUnitCreate script);
	
	public void unitMove(int iStart, int jStart, int iEnd, int jEnd, Script script);
	
	public void closeMission();
	
	public void showBlackScreen(ScriptShowBlackScreen script);
	
	public void hideBlackScreen(ScriptHideBlackScreen script);
	
	public void blackScreen(ScriptBlackScreen script);
	
	public void updateCampaign();
	
	public void enableActiveGame(ScriptEnableActiveGame script);
	
	public void disableActiveGame(ScriptDisableActiveGame script);
	
	public void gameOver(ScriptGameOver script);
	
	public void setUnitSpeed(int framesForCell, ScriptSetUnitSpeed script);
	
	public void setCameraSpeed(int delta, ScriptSetCameraSpeed script);
	
	public void removeUnit(int i, int j, ScriptRemoveUnit script);
	
	public void sparkDefault(int i, int j, ScriptSparkDefault script);
	
	public void unitMove(Point[] way, ScriptUnitMoveExtended script);
	
	public void sparkAttack(int i, int j, ScriptSparkAttack script);
	
	public void setMapPosition(int i, int j, ScriptSetMapPosition script);
	
	public void unitChangePosition(int i, int j, int iNew, int jNew, ScriptUnitChangePosition script);
	
	public void cellAttackPartTwo(int i, int j, ScriptCellAttackPartTwo script);
	
}
