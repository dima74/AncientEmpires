package ru.ancientempires.campaign.scripts;

import java.util.HashMap;

import ru.ancientempires.campaign.conditions.ConditionAnd;
import ru.ancientempires.campaign.conditions.ConditionCastleNumber;
import ru.ancientempires.campaign.conditions.ConditionNamedUnitDead;
import ru.ancientempires.campaign.conditions.ConditionNamedUnitIntoBounds;
import ru.ancientempires.campaign.conditions.ConditionOr;
import ru.ancientempires.campaign.conditions.ConditionTurn;
import ru.ancientempires.campaign.conditions.ConditionUnitIntoBounds;
import ru.ancientempires.campaign.conditions.ConditionUnitNumber;
import ru.ancientempires.framework.MyAssert;

public enum ScriptType {
	DIALOG_INTRO(ScriptDialogIntro.class),
	DIALOG(ScriptDialog.class),
	DIALOG_WITHOUT_IMAGE(ScriptDialogWithoutImage.class),
	DIALOG_TARGET(ScriptDialogTarget.class),
	TOAST_TITLE(ScriptToastTitle.class),
	TOAST_MISSION_COMPLETE(ScriptToastMissionComplete.class),
	DELAY(ScriptDelay.class),

	SHOW_BLACK_SCREEN(ScriptShowBlackScreen.class),
	HIDE_BLACK_SCREEN(ScriptHideBlackScreen.class),
	BLACK_SCREEN(true, ScriptBlackScreen.class),

	HIDE_CURSOR(true, ScriptHideCursor.class),
	SHOW_CURSOR(true, ScriptShowCursor.class),
	SET_CURSOR_POSITION(true, ScriptSetCursorPosition.class),

	SET_CAMERA_SPEED(true, ScriptSetCameraSpeed.class),
	CAMERA_MOVE(ScriptCameraMove.class),
	CAMERA_MOVE_ON_KING(ScriptCameraMoveOnKing.class),
	SET_MAP_POSITION(true, ScriptSetMapPosition.class),

	SET_UNIT_SPEED(true, ScriptSetUnitSpeed.class),
	UNIT_HANDLER_POINT(true, ScriptUnitMoveHandlerPoint.class),
	UNIT_MOVE(ScriptUnitMove.class),
	UNIT_MOVE_EXTENDED(ScriptUnitMoveExtended.class),
	UNIT_MOVE_ABOUT(ScriptUnitMoveAbout.class),
	UNIT_CREATE_MOVE(ScriptUnitCreateAndMove.class),

	UNIT_CREATE(true, ScriptUnitCreate.class),
	REMOVE_UNIT(true, ScriptRemoveUnit.class),
	UNIT_CHANGE_POSITION(true, ScriptUnitChangePosition.class),
	UNIT_ATTACK(ScriptUnitAttack.class),
	UNIT_DIE(ScriptUnitDie.class),

	SPARK_DEFAULT(ScriptSparkDefault.class),
	SPARK_ATTACK(ScriptSparkAttack.class),
	CELL_ATTACK_PART_TWO(ScriptCellAttackPartTwo.class),

	ENABLE_ACTIVE_GAME(true, ScriptEnableActiveGame.class),
	DISABLE_ACTIVE_GAME(true, ScriptDisableActiveGame.class),
	GAME_OVER(ScriptGameOver.class),
	CLOSE_MISSION(ScriptCloseMission.class),
	HIDE_INFO_IMMEDIATELY(true, ScriptHideInfoImmediately.class),

	SET_NAMED_UNIT(true, ScriptSetNamedUnit.class),
	SET_COORDINATE_NAMED_UNIT_I(true, ScriptSetCoordinateNamedUnitI.class),
	SET_COORDINATE_NAMED_UNIT_J(true, ScriptSetCoordinateNamedUnitJ.class),
	SET_COORDINATE_FROM_OTHER(true, ScriptSetCoordinateFromOther.class),

	CONDITION_UNIT_NUMBER(true, ConditionUnitNumber.class),
	CONDITION_CASTLE_NUMBER(true, ConditionCastleNumber.class),
	CONDITION_NAMED_UNIT_DEAD(true, ConditionNamedUnitDead.class),
	CONDITION_UNIT_INTO_BOUNDS(true, ConditionUnitIntoBounds.class),
	CONDITION_NAMED_UNIT_INTO_BOUNDS(true, ConditionNamedUnitIntoBounds.class),
	CONDITION_PLAYER_TURN(true, ConditionTurn.class),
	CONDITION_OR(true, ConditionOr.class),
	CONDITION_AND(true, ConditionAnd.class);

	public boolean                 isSimple;
	public Class<? extends Script> scriptClass;

	ScriptType(Class<? extends Script> scriptClass) {
		this(false, scriptClass);
	}

	ScriptType(boolean isSimple, Class<? extends Script> scriptClass) {
		this.isSimple = isSimple;
		this.scriptClass = scriptClass;
	}

	private static HashMap<Class<? extends Script>, ScriptType> map = ScriptType.createMap();

	private static HashMap createMap() {
		HashMap map = new HashMap();
		for (ScriptType type : ScriptType.values())
			map.put(type.scriptClass, type);
		return map;
	}

	public static ScriptType getType(Script script) {
		ScriptType type = ScriptType.map.get(script.getClass());
		MyAssert.a(type != null);
		return type;
	}

}
