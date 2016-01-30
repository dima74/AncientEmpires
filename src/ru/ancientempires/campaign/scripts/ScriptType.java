package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.conditions.ConditionCastleNumber;
import ru.ancientempires.campaign.conditions.ConditionNamedUnitDead;
import ru.ancientempires.campaign.conditions.ConditionNamedUnitIntoBounds;
import ru.ancientempires.campaign.conditions.ConditionTurn;
import ru.ancientempires.campaign.conditions.ConditionUnitIntoBounds;
import ru.ancientempires.campaign.conditions.ConditionUnitNumber;

public enum ScriptType
{
	INTRO(ScriptIntro.class),
	TITLE(ScriptTitle.class),
	DIALOG(ScriptDialog.class),
	SHOW_TARGET(ScriptShowTarget.class),
	CAMERA_MOVE(ScriptCameraMove.class),
	UNIT_MOVE(ScriptUnitMove.class),
	SHOW_CURSOR(true, ScriptShowCursor.class),
	HIDE_CURSOR(true, ScriptHideCursor.class),
	DELAY(ScriptDelay.class),
	UNIT_ATTACK(ScriptUnitAttack.class),
	UNIT_DIE(ScriptUnitDie.class),
	UNIT_CREATE(true, ScriptUnitCreate.class),
	UNIT_MOVE_ABOUT(ScriptUnitMoveAbout.class),
	CAMERA_MOVE_ON_KING(ScriptCameraMoveOnKing.class),
	MISSION_COMPLETE(ScriptMissionComplete.class),
	CLOSE_MISSION(ScriptCloseMission.class),
	SHOW_BLACK_SCREEN(ScriptShowBlackScreen.class),
	HIDE_BLACK_SCREEN(ScriptHideBlackScreen.class),
	BLACK_SCREEN(true, ScriptBlackScreen.class),
	// HIDE_INFO_BAR(ScriptHideInfoBar.class),
	// SHOW_INFO_BAR(ScriptShowInfoBar.class),
	ENABLE_ACTIVE_GAME(true, ScriptEnableActiveGame.class),
	DISABLE_ACTIVE_GAME(true, ScriptDisableActiveGame.class),
	GAME_OVER(ScriptGameOver.class),
	SET_NAMED_UNIT(true, ScriptSetNamedUnit.class),
	SET_UNIT_SPEED(true, ScriptSetUnitSpeed.class),
	SET_CAMERA_SPEED(true, ScriptSetCameraSpeed.class),
	REMOVE_UNIT(true, ScriptRemoveUnit.class),
	DIALOG_WITHOUT_IMAGE(ScriptDialogWithoutImage.class),
	SPARK_DEFAULT(ScriptSparkDefault.class),
	SPARK_ATTACK(ScriptSparkAttack.class),
	UNIT_MOVE_EXTENDED(ScriptUnitMoveExtended.class),
	UNIT_CHANGE_POSITION(true, ScriptUnitChangePosition.class),
	SET_MAP_POSITION(true, ScriptSetMapPosition.class),
	CELL_ATTACK_PART_TWO(ScriptCellAttackPartTwo.class),
	SET_COORDINATE_NAMED_UNIT_I(true, ScriptSetCoordinateNamedUnitI.class),
	SET_COORDINATE_NAMED_UNIT_J(true, ScriptSetCoordinateNamedUnitJ.class),
	SET_COORDINATE_FROM_OTHER(true, ScriptSetCoordinateFromOther.class),
	
	CONDITION_UNIT_NUMBER(true, ConditionUnitNumber.class),
	CONDITION_CASTLE_NUMBER(true, ConditionCastleNumber.class),
	CONDITION_NAMED_UNIT_DEAD(true, ConditionNamedUnitDead.class),
	CONDITION_UNIT_INTO_BOUNDS(true, ConditionUnitIntoBounds.class),
	CONDITION_NAMED_UNIT_INTO_BOUNDS(true, ConditionNamedUnitIntoBounds.class),
	CONDITION_PLAYER_TURN(true, ConditionTurn.class);
	
	public boolean isSimple;
	public Class<? extends Script> scriptClass;
	
	ScriptType(Class<? extends Script> scriptClass)
	{
		this(false, scriptClass);
	}
	
	ScriptType(boolean isSimple, Class<? extends Script> scriptClass)
	{
		this.isSimple = isSimple;
		this.scriptClass = scriptClass;
	}
	
}