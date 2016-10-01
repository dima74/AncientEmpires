package ru.ancientempires.campaign;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import ru.ancientempires.bridge.GameConverter;
import ru.ancientempires.campaign.conditions.Bounds;
import ru.ancientempires.campaign.conditions.ConditionAnd;
import ru.ancientempires.campaign.conditions.ConditionBoolean;
import ru.ancientempires.campaign.conditions.ConditionCastleNumber;
import ru.ancientempires.campaign.conditions.ConditionNamedBoolean;
import ru.ancientempires.campaign.conditions.ConditionNamedUnitDead;
import ru.ancientempires.campaign.conditions.ConditionNamedUnitIntoBounds;
import ru.ancientempires.campaign.conditions.ConditionOr;
import ru.ancientempires.campaign.conditions.ConditionUnitIntoBounds;
import ru.ancientempires.campaign.conditions.ConditionUnitNumber;
import ru.ancientempires.campaign.points.PointOffset;
import ru.ancientempires.campaign.points.PointProjection;
import ru.ancientempires.campaign.points.Projection;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptCameraMove;
import ru.ancientempires.campaign.scripts.ScriptCameraMoveOnKing;
import ru.ancientempires.campaign.scripts.ScriptCellAttackPartTwo;
import ru.ancientempires.campaign.scripts.ScriptCitadelAttack;
import ru.ancientempires.campaign.scripts.ScriptCloseMission;
import ru.ancientempires.campaign.scripts.ScriptDelay;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.campaign.scripts.ScriptDialogIntro;
import ru.ancientempires.campaign.scripts.ScriptDialogTarget;
import ru.ancientempires.campaign.scripts.ScriptDialogWithoutImage;
import ru.ancientempires.campaign.scripts.ScriptDisableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptEnableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptFocusOnCurrentPlayerCenter;
import ru.ancientempires.campaign.scripts.ScriptGameOver;
import ru.ancientempires.campaign.scripts.ScriptHideBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptHideCursor;
import ru.ancientempires.campaign.scripts.ScriptHideInfoImmediately;
import ru.ancientempires.campaign.scripts.ScriptRemoveAllUnits;
import ru.ancientempires.campaign.scripts.ScriptRemoveUnit;
import ru.ancientempires.campaign.scripts.ScriptSetCameraSpeed;
import ru.ancientempires.campaign.scripts.ScriptSetCursorPosition;
import ru.ancientempires.campaign.scripts.ScriptSetMapPosition;
import ru.ancientempires.campaign.scripts.ScriptSetMapPositionToCenter;
import ru.ancientempires.campaign.scripts.ScriptSetNamedBoolean;
import ru.ancientempires.campaign.scripts.ScriptSetNamedPoint;
import ru.ancientempires.campaign.scripts.ScriptSetNamedPointFromUnit;
import ru.ancientempires.campaign.scripts.ScriptSetNamedUnit;
import ru.ancientempires.campaign.scripts.ScriptSetUnitSpeed;
import ru.ancientempires.campaign.scripts.ScriptShowBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptShowCursor;
import ru.ancientempires.campaign.scripts.ScriptSnakeMap;
import ru.ancientempires.campaign.scripts.ScriptSparkAttack;
import ru.ancientempires.campaign.scripts.ScriptSparkDefault;
import ru.ancientempires.campaign.scripts.ScriptToast;
import ru.ancientempires.campaign.scripts.ScriptToastMissionComplete;
import ru.ancientempires.campaign.scripts.ScriptToastTitle;
import ru.ancientempires.campaign.scripts.ScriptUnitActivateStruct;
import ru.ancientempires.campaign.scripts.ScriptUnitAttack;
import ru.ancientempires.campaign.scripts.ScriptUnitChangePosition;
import ru.ancientempires.campaign.scripts.ScriptUnitCreate;
import ru.ancientempires.campaign.scripts.ScriptUnitCreateAndMove;
import ru.ancientempires.campaign.scripts.ScriptUnitDie;
import ru.ancientempires.campaign.scripts.ScriptUnitMove;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveAbout;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveHandlerPoint;
import ru.ancientempires.campaign.scripts.ScriptVibrate;
import ru.ancientempires.model.Game;

public class CampaignEditor
{

	// framesForCell = 48 / Unit.speed
	// delta = mapStepMax / 2

	// оставлять задержку после
	// PaintableObject.currentRenderer.setCurrentDisplayable(auxMapNameMessage);
	// createDialogScreen(PaintableObject.getLocaleString(221), (byte)2, (byte)4);

	// не нужно после
	// createSimpleSparkSprite(sprRedSpark, var12.currentX, var12.currentY, 0, 0, 2, 50);

	public ArrayList<ScriptContainer> allContainerLists = new ArrayList<ScriptContainer>();
	public ScriptContainer[]        allContainers;
	public HashSet<ScriptContainer> used;
	public ArrayList<Script>        scripts;

	public Game game;
	public int  h;
	public int  w;

	public CampaignEditor(Game game)
	{
		this.game = game;
		h = game.h;
		w = game.w;
	}

	public void createCampaign(int iMission) throws IOException
	{
		CampaignEditorGame.game = game;
		if (iMission == 0)
			mission0();
		if (iMission == 1)
			mission1();
		if (iMission == 2)
			mission2();
		if (iMission == 3)
			mission3();
		if (iMission == 4)
			mission4();
		if (iMission == 5)
			mission5();
		if (iMission == 6)
			mission6();
		if (iMission == 7)
			mission7();
		CampaignEditorGame.game = null;
		save();
	}

	public void createDefaultGameCampaign() throws IOException
	{
		CampaignEditorGame.game = game;

		ContainerList c = addRoot(new ScriptDisableActiveGame());
		c = c.add(new ScriptSetMapPositionToCenter());
		c = c.add(new ScriptHideInfoImmediately());
		c = c.add(new ScriptDialogTarget("name", "target"));
		c = c.add(new ScriptFocusOnCurrentPlayerCenter());
		c = c.add(new ScriptEnableActiveGame());

		CampaignEditorGame.game = null;
		save();
	}

	public void createTestGameCampaign() throws IOException
	{
		CampaignEditorGame.game = game;

		ContainerList c = addRoot(new ScriptDisableActiveGame());
		c = c.add(new ScriptUnitChangePosition(3, 3, 5, 5));
		c = c.add(new ScriptEnableActiveGame());

		CampaignEditorGame.game = null;
		save();
	}

	public void save() throws IOException
	{
		allContainers = allContainerLists.toArray(new ScriptContainer[0]);
		used = new HashSet<ScriptContainer>();
		scripts = new ArrayList<Script>();
		for (ScriptContainer container : allContainers)
			dfs(container);

		//for (Script script : scripts)
		//	script.type = ScriptType.getType(script);
		for (int i = 0; i < scripts.size(); i++)
			scripts.get(i).index = i;
		game.campaign.scripts = scripts.toArray(new Script[0]);
	}

	public void dfs(ScriptContainer container)
	{
		for (ScriptContainer prev : container.prev)
			if (!scripts.contains(prev.script))
				return;

		if (container.script instanceof ConditionBoolean)
			for (Script script : ((ConditionBoolean) container.script).scripts)
				if (!scripts.contains(script))
					return;

		if (used.contains(container))
			return;
		used.add(container);

		container.script.previous = Arrays.stream(container.prev.toArray(new ScriptContainer[0])).map(prev -> prev.script).toArray(Script[]::new);
		scripts.add(container.script);

		for (ScriptContainer next : container.next)
			dfs(next);
	}

	public ContainerList addRoot(Script... scripts)
	{
		ContainerList list = new ContainerList((Object[]) scripts);
		for (ScriptContainer container : list)
			allContainerLists.add(container);
		return list;
	}

	public ContainerList add(Script... scripts)
	{
		return new ContainerList((Object[]) scripts);
	}

	public ContainerList addConditionOr(ContainerList c, Script... scripts)
	{
		ContainerList list = c.add((Object[]) scripts);
		return addRoot(new ConditionOr(scripts));
	}

	public ContainerList addConditionAnd(ContainerList c, Script... scripts)
	{
		ContainerList list = c.add((Object[]) scripts);
		return addRoot(new ConditionOr(scripts));
	}

	public ConditionOr createConditionOr(ContainerList c)
	{
		return new ConditionOr(c.stream().map(c2 -> c2.script).toArray(Script[]::new));
	}

	public ConditionAnd createConditionAnd(ContainerList c)
	{
		return new ConditionAnd(c.stream().map(c2 -> c2.script).toArray(Script[]::new));
	}

	public void mission0() throws IOException
	{
		ContainerList c0 = addRoot(new ScriptDisableActiveGame());
		c0 = c0.add(new ScriptBlackScreen());
		c0 = c0.add(new ScriptHideInfoImmediately());
		c0 = c0.add(new ScriptHideCursor());
		c0 = c0.add(new ScriptSetUnitSpeed(12));
		c0 = c0.add(new ScriptSetCameraSpeed(1));
		c0 = c0.add(new ScriptSetNamedUnit(9, 1, "king"));

		// *
		ContainerList c = c0.add(new ScriptDialogIntro("intro.0", "campaign/intro/0.png"));
		c = c.add(new ScriptDialogIntro("intro.1", "campaign/intro/1.png"));
		c = c.add(new ScriptDialogIntro("intro.2", "campaign/intro/2.png"));
		c = c.add(new ScriptToastTitle("title"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptHideBlackScreen(),
				new ScriptUnitMove(8, 0, 8, 3),
				new ScriptUnitMove(9, 1, 9, 4),
				new ScriptUnitMove(10, 0, 10, 3), new ScriptCameraMove(9, 5)).last();
		c = c.add(new ScriptShowCursor());
		c = c.add(new ScriptCameraMove(3, 9));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.0", "campaign/portrait/2.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptUnitAttack(3, 9));
		c = c.add(new ScriptDialog("dialog.1", "campaign/portrait/2.png", "LEFT"));
		c = c.add(new ScriptUnitDie(3, 9));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptCameraMoveOnKing(0));
		// c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.2", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.3", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDialogTarget("name", "target"));
		// new ScriptShowCursor(),
		c.add(new ScriptEnableActiveGame());

		// part 2
		c = c.add(new ConditionUnitNumber(1, 0, 0));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptDelay(1333));
		c = c.add(new ScriptCameraMove(1, 1));
		c = c.add(new ScriptUnitCreate(1, "ARCHER", 1, 1),
				new ScriptUnitMoveAbout(1, 1, 2, 1)).last();
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptCameraMove(10, 10));
		c = c.add(new ScriptUnitCreate(1, "SOLDIER", 10, 10));
		c = c.add(new ScriptUnitMoveAbout(10, 10, 9, 10));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptDialog("dialog.4", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.5", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptEnableActiveGame());

		// part 3
		c = c.add(new ConditionUnitNumber(1, 0, 0));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptDelay(2000));
		c = c.add(new ScriptShowBlackScreen());
		c = c.add(new ScriptDialog("dialog.6", "campaign/portrait/2.png", "LEFT"));
		// c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.7", "campaign/portrait/0.png", "LEFT"));
		// c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.8", "campaign/portrait/2.png", "LEFT"));
		// c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.9", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(1000));
		c = c.add(new ScriptToastMissionComplete("missionComplete"));
		c = c.add(new ScriptCloseMission());

		// c = addRoot(new ConditionCastleNumber(0, 0, 0), new ConditionNamedUnitDead("king"));
		c = c0.add(new ConditionNamedUnitDead("king"));
		c = c.add(new ScriptGameOver());
		// */
	}

	public void mission1() throws IOException
	{
		ContainerList c0 = addRoot(new ScriptDisableActiveGame());
		c0 = c0.add(new ScriptBlackScreen());
		c0 = c0.add(new ScriptHideInfoImmediately());
		c0 = c0.add(new ScriptShowCursor());
		c0 = c0.add(new ScriptSetUnitSpeed(12));
		c0 = c0.add(new ScriptSetCameraSpeed(1));
		c0 = c0.add(new ScriptSetNamedUnit(11, 8, "king"));

		ContainerList c = c0.add(new ScriptToastTitle("title"));
		c = c.add(new ScriptHideBlackScreen(),
				new ScriptUnitMove(12, 7, 10, 7),
				new ScriptUnitMove(11, 8, 9, 8),
				new ScriptUnitMove(12, 9, 10, 9),
				new ScriptCameraMove(3, 7)).last();
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptSetCameraSpeed(2));
		c = c.add(new ScriptCameraMove(3, 12));
		c = c.add(new ScriptDialog("dialog.0", "campaign/portrait/1.png", "LEFT"));
		// c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptSetCursorPosition(9, 8));
		c = c.add(new ScriptDialog("dialog.1", "campaign/portrait/3.png", "LEFT"));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptSetCursorPosition(3, 7));
		c = c.add(new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptCameraMove(3, 7));

		c = c.add(new ScriptSetUnitSpeed(24));
		ScriptUnitMoveHandlerPoint handler = new ScriptUnitMoveHandlerPoint(3, 6);
		c = c.add(handler, new ScriptUnitCreateAndMove(1, "SOLDIER", 3, 7, 3, 6, -2, 6)
				.setHandlers(handler)
				.disableMakeSmoke()).first();
		handler = new ScriptUnitMoveHandlerPoint(3, 6);
		c = c.add(handler, new ScriptUnitCreateAndMove(1, "CRYSTAL", 3, 7, 3, 6, 0, 6)
				.setHandlers(handler)
				.disableMakeSmoke()).first();
		c = c.add(new ScriptUnitCreateAndMove(1, "SOLDIER", 3, 7, 3, 6, 1, 6));
		c = c.add(new ScriptDialog("dialog.3", "campaign/portrait/2.png", "LEFT"));
		c = c.add(new ScriptSetUnitSpeed(12));
		c = c.add(new ScriptUnitMove(0, 6, -1, 6).disableMakeSmoke(),
				new ScriptUnitCreateAndMove(0, "SOLDIER", 3, 7, 3, 6, 2, 6)).last();
		c = c.add(new ScriptUnitAttack(2, 6));
		c = c.add(new ScriptRemoveUnit(-2, 6),
				new ScriptRemoveUnit(-1, 6));
		c = c.add(new ScriptDialog("dialog.4", "campaign/portrait/2.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptUnitDie(2, 6));
		c = c.add(new ScriptSetCameraSpeed(6));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptDialog("dialog.5", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDialog("dialog.6", "campaign/portrait/1.png", "LEFT"));
		c = c.add(new ScriptCameraMove(5, 3));
		c = c.add(new ScriptDialog("dialog.7", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptDialogTarget("name", "target"));
		c = c.add(new ScriptEnableActiveGame());

		c = c.add(new ConditionUnitNumber(1, 0, 0),
				new ConditionCastleNumber(1, 0, 0));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptDelay(1333));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptDialog("dialog.8", "campaign/portrait/1.png", "LEFT"));
		c = c.add(new ScriptDialog("dialog.9", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptToastMissionComplete("missionComplete"));
		c = c.add(new ScriptCloseMission());

		c = c0.add(new ConditionCastleNumber(0, 0, 0),
				new ConditionNamedUnitDead("king"));
		c = c.add(new ScriptGameOver());
	}

	public void mission2() throws IOException
	{
		ContainerList c0 = addRoot(new ScriptDisableActiveGame());
		c0 = c0.add(new ScriptBlackScreen());
		c0 = c0.add(new ScriptHideInfoImmediately());
		c0 = c0.add(new ScriptHideCursor());
		c0 = c0.add(new ScriptSetUnitSpeed(12));
		c0 = c0.add(new ScriptSetCameraSpeed(1));
		c0 = c0.add(new ScriptSetNamedUnit(16, 8, "king"));

		ContainerList c = c0.add(new ScriptToastTitle("title"));
		c = c.add(new ScriptHideBlackScreen(),
				new ScriptUnitMove(16, 8, 14, 8),
				new ScriptUnitMove(17, 8, 15, 8, 15, 7, 14, 7),
				new ScriptUnitMove(18, 8, 15, 8, 15, 7),
				new ScriptUnitMove(19, 8, 15, 8));
		c = c.add(new ScriptDialog("dialog.0", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDialogTarget("name", "target"));
		c = c.add(new ScriptEnableActiveGame());

		// часть 2
		c = c.add(new ConditionUnitIntoBounds(0, new Bounds(0, 0, 10, w), new Bounds(0, 0, h, 4)));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptDelay(666));

		c = c.add(new ScriptCameraMove(8, 0),
				new ScriptUnitCreateAndMove(1, "DIRE_WOLF", 8, -1, 8, 0),
				new ScriptUnitCreateAndMove(1, "DIRE_WOLF", 7, -2, 7, 1));
		c = c.add(new ScriptDelay(666));

		c = c.add(new ScriptCameraMove(6, 8),
				new ScriptUnitCreateAndMove(1, "DIRE_WOLF", 6, 11, 6, 8));
		c = c.add(new ScriptDelay(333));

		c = c.add(new ScriptCameraMove(1, 2),
				new ScriptUnitCreateAndMove(1, "WISP", -1, 2, 1, 2),
				new ScriptUnitCreateAndMove(1, "DIRE_WOLF", -2, 1, 2, 1),
				new ScriptUnitCreateAndMove(1, "DIRE_WOLF", -2, 3, 2, 3));
		c = c.add(new ScriptDelay(333));

		c = c.add(new ScriptDialog("dialog.3", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDelay(666));

		c = c.add(new ScriptCameraMove(8, 4));
		c = c.add(new ScriptUnitCreate(1, "ELEMENTAL", 8, 3),
				new ScriptUnitCreate(1, "ELEMENTAL", 7, 4),
				new ScriptUnitCreate(1, "ELEMENTAL", 8, 5));
		c = c.add(new ScriptSparkDefault(8, 3),
				new ScriptSparkDefault(7, 4),
				new ScriptSparkDefault(8, 5));
		c = c.add(new ScriptDelay(333));

		c = c.add(new ScriptDialog("dialog.4", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialogWithoutImage("dialog.5"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.6", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptToastTitle("dialog.7")); // TODO toast

		c = c.add(new ScriptRemoveUnit(8, 3),
				new ScriptRemoveUnit(7, 4),
				new ScriptRemoveUnit(8, 5));
		c = c.add(new ScriptUnitCreate(0, "ELEMENTAL", 8, 3),
				new ScriptUnitCreate(0, "ELEMENTAL", 7, 4),
				new ScriptUnitCreate(0, "ELEMENTAL", 8, 5));
		c = c.add(new ScriptSparkDefault(8, 3),
				new ScriptSparkDefault(7, 4),
				new ScriptSparkDefault(8, 5));
		c = c.add(new ScriptEnableActiveGame());

		// часть 3
		c = c.add(new ConditionUnitNumber(1, 0, 0));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptToastMissionComplete("missionComplete"));
		c = c.add(new ScriptCloseMission());

		c = c0.add(new ConditionNamedUnitDead("king"));
		c = c.add(new ScriptGameOver());
	}

	public void mission3() throws IOException
	{
		ContainerList c0 = addRoot(new ScriptDisableActiveGame());
		c0 = c0.add(new ScriptBlackScreen());
		c0 = c0.add(new ScriptHideInfoImmediately());
		c0 = c0.add(new ScriptHideCursor());
		c0 = c0.add(new ScriptSetUnitSpeed(12));
		c0 = c0.add(new ScriptSetCameraSpeed(1));
		c0 = c0.add(new ScriptSetNamedUnit(5, 0, "king"));

		ContainerList c = c0.add(new ScriptDialogIntro("intro.0", "campaign/intro/3.png"));
		c = c.add(new ScriptToastTitle("title"));
		c = c.add(new ScriptHideBlackScreen(),
				new ScriptUnitMove(5, 0, 5, 3, 3, 3),
				new ScriptUnitMove(5, -1, 5, 3, 4, 3),
				new ScriptUnitMove(5, -2, 5, 4, 4, 4),
				new ScriptUnitMove(5, -3, 5, 2, 4, 2));
		c = c.add(new ScriptShowBlackScreen());
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.0", "campaign/portrait/2.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptUnitChangePosition(3, 3, 1, 7),
				new ScriptUnitChangePosition(4, 3, 4, 5),
				new ScriptUnitChangePosition(4, 4, 5, 7),
				new ScriptUnitChangePosition(4, 2, 3, 3),
				new ScriptSetMapPosition(3, 13));

		c = c.add(new ScriptHideBlackScreen());
		c = c.add(new ScriptSetCameraSpeed(1));
		c = c.add(new ScriptCameraMove(10, 13));
		c = c.add(new ScriptSetUnitSpeed(24), new ScriptSetCameraSpeed(2));
		c = c.add(new ScriptCameraMove(10, 6), new ScriptUnitMove(10, 10, 10, 6)).last();
		c = c.add(new ScriptShowCursor());
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptCameraMove(9, 4), new ScriptSparkAttack(9, 4));
		// c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptCellAttackPartTwo(9, 4));

		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.3", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptCameraMove(1, 7));
		c = c.add(new ScriptDialog("dialog.4", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialogTarget("name", "target"));
		c = c.add(new ScriptEnableActiveGame());

		// часть 2
		c = c.add(new ConditionUnitNumber(1, 0, 0),
				new ConditionCastleNumber(1, 0, 0));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptDelay(1000));
		c = c.add(new ScriptDialog("dialog.5", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.6", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptToastMissionComplete("missionComplete"));
		c = c.add(new ScriptCloseMission());

		c = c0.add(new ConditionCastleNumber(0, 0, 0),
				new ConditionNamedUnitDead("king"));
		c = c.add(new ScriptGameOver());
	}

	public void mission4() throws IOException
	{
		ContainerList c0 = addRoot(new ScriptDisableActiveGame());
		c0 = c0.add(new ScriptBlackScreen());
		c0 = c0.add(new ScriptHideInfoImmediately());
		c0 = c0.add(new ScriptHideCursor());
		c0 = c0.add(new ScriptSetUnitSpeed(12));
		c0 = c0.add(new ScriptSetCameraSpeed(6));// ?
		c0 = c0.add(new ScriptSetNamedUnit(-5, 11, "king"));
		c0 = c0.add(new ScriptSetNamedUnit(-7, 11, "crystall"));

		ContainerList c = c0.add(new ScriptToastTitle("title"));
		c = c.add(new ScriptHideBlackScreen(),
				new ScriptUnitMove(-3, 11, 2, 11),
				new ScriptUnitMove(-5, 10, 1, 10),
				new ScriptUnitMove(-5, 11, 1, 11),
				new ScriptUnitMove(-5, 12, 1, 12),
				new ScriptUnitMove(-7, 11, 0, 11),
				new ScriptUnitMove(-7, 12, 0, 12));
		c = c.add(new ScriptDialog("dialog.0", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialogTarget("name", "target"));
		c = c.add(new ScriptEnableActiveGame());

		// часть 2
		c = c.add(new ConditionUnitIntoBounds(0, new Bounds(0, 0, h, 8)));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptCameraMove(4, 4));
		c = c.add(new ScriptUnitCreateAndMove(1, "SKELETON", 4, 4, 1, 4));
		c = c.add(new ScriptUnitCreateAndMove(1, "ARCHER", 4, 4, 4, 5, 2, 5));
		c = c.add(new ScriptUnitCreateAndMove(1, "SKELETON", 4, 4, 3, 4));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptEnableActiveGame());

		// part 3
		c = c.add(new ConditionUnitIntoBounds(0, new Bounds(7, 0, h, w)));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptCameraMove(10, 6));
		c = c.add(new ScriptUnitCreate(1, "ARCHER", 10, 6));
		c = c.add(new ScriptUnitMoveAbout(10, 6, 10, 5));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptUnitCreate(1, "DIRE_WOLF", 10, 6));
		c = c.add(new ScriptUnitMoveAbout(10, 6, 8, 7));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptUnitCreate(1, "DIRE_WOLF", 10, 6));
		c = c.add(new ScriptUnitMoveAbout(10, 6, 9, 7));
		c = c.add(new ScriptEnableActiveGame());

		// part 4
		c = c.add(new ConditionUnitIntoBounds(0, new Bounds(6, 8, h, w)));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptCameraMove(5, 12));
		c = c.add(new ScriptUnitCreate(1, "DIRE_WOLF", 5, 12));
		c = c.add(new ScriptUnitMoveAbout(5, 12, 7, 12));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptUnitCreate(1, "GOLEM", 5, 12));
		c = c.add(new ScriptUnitMoveAbout(5, 12, 6, 12));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptUnitCreate(1, "DIRE_WOLF", 5, 12));
		c = c.add(new ScriptUnitMoveAbout(5, 12, 5, 11));
		c = c.add(new ScriptEnableActiveGame());

		// part 5
		c = c.add(new ConditionUnitIntoBounds(0, new Bounds(8, 15, h, w)));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptCameraMove(8, 18));
		c = c.add(new ScriptUnitCreate(1, "DIRE_WOLF", 8, 18));
		c = c.add(new ScriptUnitMoveAbout(8, 18, 10, 16));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptUnitCreate(1, "GOLEM", 8, 18));
		c = c.add(new ScriptUnitMoveAbout(8, 18, 10, 17));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptUnitCreate(1, "DIRE_WOLF", 8, 18));
		c = c.add(new ScriptUnitMoveAbout(8, 18, 10, 18));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptUnitCreate(1, "ARCHER", 18, 18));
		c = c.add(new ScriptUnitMoveAbout(8, 18, 9, 18));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.3", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptEnableActiveGame());

		c = addConditionOr(c, new ConditionUnitNumber(1, 0, 0),
				new ConditionNamedUnitIntoBounds("crystall",
						new Bounds(11, 15, h, w)));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptToastMissionComplete("missionComplete"));
		c = c.add(new ScriptCloseMission());

		c = addConditionOr(c0, new ConditionNamedUnitDead("king"),
				new ConditionNamedUnitDead("crystall"));
		c = c.add(new ScriptGameOver());
	}

	public void mission5() throws IOException
	{
		ContainerList c0 = addRoot(new ScriptDisableActiveGame());
		c0 = c0.add(new ScriptBlackScreen());
		c0 = c0.add(new ScriptHideInfoImmediately());
		c0 = c0.add(new ScriptHideCursor());
		c0 = c0.add(new ScriptSetUnitSpeed(12));// ?
		c0 = c0.add(new ScriptSetCameraSpeed(2));
		c0 = c0.add(new ScriptSetNamedUnit(17, 5, "king"));

		ContainerList c = c0.add(new ScriptToastTitle("title"));
		c = c.add(new ScriptHideBlackScreen(),
				new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.0", "campaign/portrait/1.png", "LEFT"));
		c = c.add(new ScriptDialogTarget("name", "target"));
		c = c.add(new ScriptEnableActiveGame());

		c = c.add(new ConditionUnitNumber(1, 0, 0), new ConditionCastleNumber(1, 0, 0));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptDelay(1000));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptToastMissionComplete("missionComplete"));
		c = c.add(new ScriptCloseMission());

		c = c0.add(new ConditionCastleNumber(0, 0, 0),
				new ConditionNamedUnitDead("king"));
		c = c.add(new ScriptGameOver());

	}

	public void mission6() throws IOException
	{
		ContainerList c0 = addRoot(new ScriptDisableActiveGame());
		c0 = c0.add(new ScriptBlackScreen());
		c0 = c0.add(new ScriptHideInfoImmediately());
		c0 = c0.add(new ScriptHideCursor());
		c0 = c0.add(new ScriptSetUnitSpeed(12));
		c0 = c0.add(new ScriptSetCameraSpeed(1)); //?
		c0 = c0.add(new ScriptSetNamedUnit(0, 13, "king"));
		c0 = c0.add(new ScriptSetNamedUnit(-4, 13, "crystall"));
		c0 = c0.add(new ScriptSetNamedBoolean("part1", true));

		ContainerList c = c0.add(new ScriptToastTitle("title"));
		c = c.add(new ScriptHideBlackScreen(),
				new ScriptUnitMove(0, 13, 1, 13, 1, 14, 3, 14).disableMakeSmoke(),
				new ScriptUnitMove(-1, 13, 1, 13, 1, 14, 2, 14).disableMakeSmoke(),
				new ScriptUnitMove(-2, 13, 1, 13, 1, 14).disableMakeSmoke(),
				new ScriptUnitMove(-3, 13, 1, 13).disableMakeSmoke(),
				new ScriptUnitMove(-4, 13, 0, 13));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.0", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(333));
		c = c.add(new ScriptDialogTarget("name", "target"));
		c = c.add(new ScriptEnableActiveGame());

		//c = c.add(new ConditionTurn(2));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptCameraMove(7, 11));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptUnitCreateAndMove(1, "DIRE_WOLF", 8, 11, 7, 14));
		c = c.add(new ScriptUnitCreateAndMove(1, "SOLDIER", 8, 11, 7, 13));
		c = c.add(new ScriptUnitCreateAndMove(1, "SORCERESS", 8, 11, 7, 12));
		c = c.add(new ScriptUnitCreateAndMove(1, "ARCHER", 8, 11, 8, 13));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptEnableActiveGame());

		c = c.add(createConditionOr(c.add(new ConditionUnitIntoBounds(0, new Bounds(10, 0, h, 9)),
				new ConditionUnitNumber(1, 0, 0))));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptDelay(666));

		c = c.add(new ScriptSetNamedPointFromUnit("crystall", "crystall"));
		c = c.add(new ScriptSetNamedPoint("rightIn", new PointProjection(Projection.RIGHT_IN, "crystall")));
		c = c.add(new ScriptSetNamedPoint("rightOut", new PointProjection(Projection.RIGHT_OUT, "crystall")));
		c = c.add(new ScriptSetNamedPoint("leftIn", new PointProjection(Projection.LEFT_IN, "crystall")));
		c = c.add(new ScriptSetNamedPoint("leftOut", new PointProjection(Projection.LEFT_OUT, "crystall")));
		c = c.add(new ScriptSetNamedPoint("crystallLeft", new PointOffset(0, -1, "crystall")));

		c = c.add(new ScriptCameraMove("rightIn"));
		c = c.add(new ScriptUnitCreateAndMove(1, "DRAGON", "rightOut", "rightIn"));
		c = c.add(new ScriptDialog("dialog.3", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptUnitMove("rightIn", "crystall"));
		c = c.add(new ScriptDialog("dialog.4", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptSetCameraSpeed(3));
		//c = c.add(new ScriptCameraMove(0, 0));
		//c = c.add(new ScriptSetCoordinateFromOther("crystallJ", "crystallJ-1", -1));
		//c = c.add(new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateName("crystallJ"), new CoordinateName("crystallI"), new CoordinateName("crystallJ-1")));
		c = c.add(new ScriptUnitMove("crystall", "crystallLeft"));
		c = c.add(new ScriptUnitMove("crystallLeft", "leftOut"),
				new ScriptUnitMove("crystall", "leftOut"),
				new ScriptCameraMove("leftIn"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptSetNamedBoolean("part1", false));
		c = c.add(new ScriptRemoveUnit("leftOut"),
				new ScriptRemoveUnit("leftOut"));
		c = c.add(new ScriptSetCameraSpeed(6));
		c = c.add(new ScriptCameraMove(9, 1));

		/*
		c = c.add(new ScriptSetCoordinateNamedUnitI("crystallI", "crystall"));
		c = c.add(new ScriptSetCoordinateNamedUnitJ("crystallJ", "crystall"));
		c = c.add(new ScriptUnitCreate(new CoordinateName("crystallI"), new CoordinateInteger(w + 1), "DRAGON", 1));
		c = c.add(new ScriptCameraMove(new CoordinateName("crystallI"), new CoordinateInteger(w)));
		c = c.add(new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateInteger(w + 1), new CoordinateName("crystallI"), new CoordinateInteger(w)));
		c = c.add(new ScriptDialog("dialog.3", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateInteger(w), new CoordinateName("crystallI"), new CoordinateName("crystallJ")));
		c = c.add(new ScriptDialog("dialog.4", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptSetCameraSpeed(3));
		c = c.add(new ScriptCameraMove(0, 0));
		c = c.add(new ScriptSetCoordinateFromOther("crystallJ", "crystallJ-1", -1));
		c = c.add(new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateName("crystallJ"), new CoordinateName("crystallI"), new CoordinateName("crystallJ-1")));
		//*/

		/*
		c = c.add(new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateName("crystallJ-1"), new CoordinateName("crystallI"), new CoordinateInteger(-2)));
		c = c.add(new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateName("crystallJ"), new CoordinateName("crystallI"), new CoordinateInteger(-1)));
		c = c.add(new ScriptSetCameraSpeed(6));
		c = c.add(new ScriptRemoveUnit(new CoordinateName("crystallI"), new CoordinateInteger(-1)));
		c = c.add(new ScriptRemoveUnit(new CoordinateName("crystallI"), new CoordinateInteger(-2)));
		c = c.add(new ScriptCameraMove(9, 1));
		//*/

		c = c.add(new ScriptUnitCreateAndMove(1, "KING", 8, -2, 8, 0),
				new ScriptUnitCreateAndMove(1, "SOLDIER", 8, -1, 8, 3),
				new ScriptUnitCreateAndMove(1, "SOLDIER", 10, -1, 10, 1),
				new ScriptUnitCreateAndMove(1, "DRAGON", 7, -3, 7, 4, 8, 4),
				new ScriptUnitCreateAndMove(1, "DRAGON", 11, -3, 11, 2, 10, 2),
				new ScriptUnitCreateAndMove(1, "WISP", 9, -2, 9, 2),
				new ScriptUnitCreateAndMove(1, "GOLEM", 9, -4, 9, 4),
				new ScriptUnitCreateAndMove(1, "GOLEM", 9, -6, 9, 5, 10, 5));
		c = c.add(new ScriptDialog("dialog.5", "campaign/portrait/3.png", "LEFT"));
		c = c.add(new ScriptCameraMove(14, 13));
		c = c.add(new ScriptUnitCreateAndMove(1, "SOLDIER", 14, 13, 14, 12));
		c = c.add(new ScriptUnitCreateAndMove(1, "GOLEM", 14, 13, 14, 14));
		c = c.add(new ScriptUnitCreateAndMove(1, "ELEMENTAL", 14, 13, 12, 13));
		c = c.add(new ScriptUnitCreateAndMove(1, "SORCERESS", 14, 13, 15, 13));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.6", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.7", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptCameraMove(17, 13));
		c = c.add(new ScriptUnitCreateAndMove(0, "KING", 18, 13, 16, 13),
				new ScriptUnitCreateAndMove(0, "GOLEM", 18, 12, 16, 12),
				new ScriptUnitCreateAndMove(0, "DRAGON", 19, 14, 16, 14),
				new ScriptUnitCreateAndMove(0, "WISP", 19, 13, 17, 13),
				new ScriptUnitCreateAndMove(0, "ARCHER", 19, 12, 17, 12));
		c = c.add(new ScriptDialog("dialog.8", "campaign/portrait/1.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptEnableActiveGame());

		c = c.add(new ConditionUnitNumber(1, 0, 0),
				new ConditionCastleNumber(1, 0, 0));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptToastMissionComplete("missionComplete"));
		c = c.add(new ScriptCloseMission());

		c = c0.add(createConditionOr(c0.add(
				createConditionAnd(c0.add(
						new ConditionNamedBoolean("part1"),
						new ConditionNamedUnitDead("crystall"))),
				createConditionAnd(c0.add(
						new ConditionCastleNumber(0, 0, 0),
						new ConditionNamedUnitDead("king"))))));
		c = c.add(new ScriptGameOver());
	}

	public void mission7() throws IOException
	{
		ContainerList c0 = addRoot(new ScriptDisableActiveGame());
		c0 = c0.add(new ScriptBlackScreen());
		c0 = c0.add(new ScriptHideInfoImmediately());
		c0 = c0.add(new ScriptHideCursor());
		c0 = c0.add(new ScriptSetUnitSpeed(12));
		c0 = c0.add(new ScriptSetCameraSpeed(6)); //?
		c0 = c0.add(new ScriptSetNamedUnit(15, 8, "galamar"));
		c0 = c0.add(new ScriptSetNamedUnit(15, 6, "valadorn"));
		c0 = c0.add(new ScriptSetNamedUnit(4, 7, "saeth"));

		ContainerList c = c0.add(new ScriptDialogIntro("intro.0", "campaign/intro/4.png"));
		c = c.add(new ScriptToastTitle("title"));
		c = c.add(new ScriptHideBlackScreen());
		c = c.add(new ScriptDialog("dialog.0", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptCameraMove(2, 7));
		c = c.add(new ScriptDialog("dialog.1", "campaign/portrait/4.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.2", "campaign/portrait/1.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.3", "campaign/portrait/4.png", "LEFT"));

		c = c.add(new ScriptUnitMove(2, 5, 2, 7));
		c = c.add(new ScriptUnitActivateStruct(2, 7));
		c = c.add(new ScriptUnitMove(3, 7, 2, 7));
		c = c.add(new ScriptUnitActivateStruct(2, 7));
		c = c.add(new ScriptUnitMove(2, 9, 2, 7));
		c = c.add(new ScriptUnitActivateStruct(2, 7));
		c = c.add(new ScriptUnitMove(4, 7, 2, 7));
		c = c.add(new ScriptDelay(666));

		c = c.add(new ScriptShowCursor());
		c = c.add(new ScriptCameraMove(15, 9));
		c = c.add(new ScriptToast("dialog.8"));
		c = c.add(new ScriptToast("dialog.9"));
		c = c.add(new ScriptCitadelAttack(15, 9));

		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptDialog("dialog.4", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialog("dialog.5", "campaign/portrait/4.png", "LEFT"));
		c = c.add(new ScriptDialog("dialog.6", "campaign/portrait/1.png", "LEFT"));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptShowCursor());
		c = c.add(new ScriptSetCameraSpeed(2));
		c = c.add(new ScriptCameraMove(9, 3));
		c = c.add(new ScriptCameraMove(4, 13));
		c = c.add(new ScriptSetCameraSpeed(6));
		c = c.add(new ScriptDialog("dialog.7", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptCameraMoveOnKing(0));
		c = c.add(new ScriptDialogTarget("name", "target"));
		c = c.add(new ScriptEnableActiveGame());

		c = c.add(new ConditionNamedUnitDead("saeth"));
		c = c.add(new ScriptDisableActiveGame());
		c = c.add(new ScriptHideCursor());
		c = c.add(new ScriptDelay(1333));

		c = c.add(new ScriptShowBlackScreen());
		c = c.add(new ScriptSetMapPosition(2, 7));
		c = c.add(new ScriptRemoveAllUnits());
		c = c.add(new ScriptUnitCreate(1, "KING_SAETH", 2, 7));
		c = c.add(new ScriptUnitCreate(0, "KING_GALAMAR", 3, 6));
		c = c.add(new ScriptUnitCreate(0, "KING_VALADORN", 3, 8));
		c = c.add(new ScriptUnitCreate(0, "SOLDIER", 1, 6));
		c = c.add(new ScriptUnitCreate(0, "SOLDIER", 1, 8));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptHideBlackScreen());
		c = c.add(new ScriptDialog("dialog.10", "campaign/portrait/4.png", "LEFT"));
		c = c.add(new ScriptDelay(1000));
		c = c.add(new ScriptDialog("dialog.11", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptDelay(533));
		c = c.add(new ScriptDialog("dialog.12", "campaign/portrait/4.png", "LEFT"));
		c = c.add(new ScriptDelay(1000));
		c = c.add(new ScriptRemoveUnit(2, 7));
		c = c.add(new ScriptSparkDefault(2, 7));
		c = c.add(new ScriptDelay(1000));
		c = c.add(new ScriptVibrate());
		c = c.add(new ScriptSnakeMap(666));
		c = c.add(new ScriptDialog("dialog.13", "campaign/portrait/5.png", "LEFT"));
		c = c.add(new ScriptSnakeMap(333));
		c = c.add(new ScriptDialog("dialog.14", "campaign/portrait/2.png", "LEFT"));
		c = c.add(new ScriptSnakeMap(333));
		c = c.add(new ScriptDialog("dialog.15", "campaign/portrait/1.png", "LEFT"));
		c = c.add(new ScriptSnakeMap(333));
		c = c.add(new ScriptDialog("dialog.16", "campaign/portrait/0.png", "LEFT"));
		c = c.add(new ScriptShowBlackScreen(),
				new ScriptSnakeMap(1333));
		c = c.add(new ScriptDelay(666));
		c = c.add(new ScriptDialogIntro("intro.1", "campaign/intro/5.png"));
		c = c.add(new ScriptToastMissionComplete("missionComplete"));
		c = c.add(new ScriptCloseMission());

		c = c0.add(new ConditionCastleNumber(0, 0, 0),
				new ConditionNamedUnitDead("galamar"),
				new ConditionNamedUnitDead("valadorn"));
		c = c.add(new ScriptGameOver());
	}
	
	/*
	public void mission1(String file) throws IOException
	{
		// new Script()).addDependent(
		Script[] endScripts =
		{
				new ScriptSetNamedUnit(11, 8, "king"),
				new ScriptBlackScreen(),
				new ScriptHideInfoBar(),
				new ScriptDisableActiveGame(),
				new ScriptSetUnitSpeed(12), // ).addDependent(
				new ScriptSetCameraSpeed(1), // ).addDependent(
				new ScriptTitle("title").addDependent(
						new ScriptHideBlackScreen(),
						// allowedUnits = 1;
						// money[0] = 300;
						// money[1] = 50;
						// Unit.speed = 4;
						// mapStepMax = 2;
						// fractionKings[0].setKingVariant(2);
						// moveMapShowPoint(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
						// moveCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
						new ScriptUnitMove(12, 7, 10, 7), // ).addDependent(
						// getUnit(7, 12, 0).plotRoute(7, 10, false);
						new ScriptUnitMove(11, 8, 9, 8), // ).addDependent(
						// getUnit(8, 11, 0).plotRoute(8, 9, false);
						new ScriptUnitMove(12, 9, 10, 9), // ).addDependent(
						// getUnit(9, 12, 0).plotRoute(9, 10, false);
						new ScriptCameraMove(3, 7)
				// moveMapAndCursor(7, 3);
				// PaintableObject.currentRenderer.setCurrentDisplayable(auxMapNameMessage);
				//
				// case 1:
				).addDependent(
						new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 2:
				).addDependent(
						new ScriptSetCameraSpeed(2), // ).addDependent(
						// mapStepMax = 4;
						new ScriptCameraMove(3, 12)
				// moveMapAndCursor(12, 3);
				// break;
				// case 3:
				).addDependent(
						new ScriptDialog("dialog.0", "campaign/portrait/1.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(231), 1, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 4:
				).addDependent(
						new ScriptCameraMoveOnKing(0)).addDependent(
								new ScriptDialog("dialog.1", "campaign/portrait/3.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(232), 3, 4);
				// moveMapAndCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// break;
				// case 5:
				).addDependent(
						new ScriptCameraMove(3, 7)).addDependent(
								new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(233), 5, 4);
				// moveMapAndCursor(7, 3);
				// break;
				// case 6:
				).addDependent(
						new ScriptSetUnitSpeed(24), // ).addDependent(
						// Unit.speed = 2;
						//
						new ScriptUnitCreate(3, 7, "SOLDIER", 1),
						new ScriptUnitMove(3, 7, 3, 6)).addDependent(
								// crystallEscortLeader = Unit.createUnit(0, 1, 7, 3);
								new ScriptUnitCreate(3, 7, "CRYSTAL", 1),
								new ScriptUnitMove(3, 6, -2, 6),
								new ScriptUnitMove(3, 7, 3, 6))
						.addDependent(
								// crystallEscortCrystall = Unit.createUnit(11, 1, 7, 3);
								new ScriptUnitCreate(3, 7, "SOLDIER", 1),
								new ScriptUnitMove(3, 6, 0, 6),
								new ScriptUnitMoveExtended(3, 7, 3, 6, 1, 6)
				// crystallEscortFollower = Unit.createUnit(0, 1, 7, 3);
				//
				// crystallEscortLeader.followerUnit = crystallEscortCrystall;
				// crystallEscortCrystall.followerUnit = crystallEscortFollower;
				//
				// Unit.createUnit(0, 1, 7, 3).plotRoute(6, -2, false);
				//
				// new ScriptDelay(2000)
				// setScriptDelay(30);
				// ++scriptState;
				// break;
				// case 7:
				).addDependent(
						// if(crystallEscortFollower.currentMapPosX == 6 && crystallEscortFollower.currentMapPosY == 1)
						// {
						new ScriptDialog("dialog.3", "campaign/portrait/2.png", "LEFT")).addDependent(
								// Unit.speed = 4;
								new ScriptSetUnitSpeed(12), // ).addDependent(
								new ScriptUnitMove(0, 6, -1, 6),
								new ScriptUnitCreate(3, 7, "SOLDIER", 0))
						.addDependent(
								// templeWarrior = Unit.createUnit(0, 0, 7, 3);
								new ScriptUnitMoveExtended(3, 7, 3, 6, 2, 6))
						.addDependent(
								new ScriptUnitAttack(2, 6)
				// Unit.createUnit(0, 0, 7, 3).plotRoute(6, 2, false);
				// crystallEscortCrystall.followerUnit = null;
				// createDialogScreen(PaintableObject.getLocaleString(234), 2, 4);
				// ++scriptState;
				// }
				// break;
				// case 8:
				).addDependent(
						new ScriptRemoveUnit(-2, 6),
						new ScriptRemoveUnit(-1, 6)
				// if(templeWarrior.unitState != 1)
				// {
				// Renderer.vibrate(100);
				// templeWarrior.scheduleIdleAnimationStop(400);
				// createSimpleSparkSprite(sprRedSpark, templeWarrior.currentX, templeWarrior.currentY, 0, 0, 2, 50);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// }
				// break;
				// case 9:
				).addDependent(
						new ScriptDialog("dialog.4", "campaign/portrait/2.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(235), 2, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 10:
				).addDependent(
						// createSimpleSparkSprite(sprSmoke, templeWarrior.currentX, templeWarrior.currentY, 0, -3, 1, 100);
						// createSimpleSparkSprite(sprSpark, templeWarrior.currentX, templeWarrior.currentY, 0, 0, 1, 50);
						// templeWarrior.removeThisUnit();
						// templeWarrior = null;
						new ScriptUnitDie(2, 6)
				// new ScriptDelay(1000)
				// setScriptDelay(15);
				// ++scriptState;
				// break;
				// case 11:
				).addDependent(
						new ScriptSetUnitSpeed(8), // ).addDependent(
						// Unit.speed = Unit.UNIT_SPEED_SLOW;
						// crystallEscortLeader.removeThisUnit();
						// crystallEscortCrystall.removeThisUnit();
						// crystallEscortLeader = null;
						// crystallEscortCrystall = null;
						// crystallEscortFollower = null;
						new ScriptSetCameraSpeed(6), // ).addDependent(
						// mapStepMax = 12;
						new ScriptCameraMoveOnKing(0)
				// moveMapAndCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// break;
				// case 12:
				).addDependent(
						new ScriptDialog("dialog.5", "campaign/portrait/5.png", "LEFT")
				// createDialogScreen(PaintableObject.getLocaleString(236), 5, 4);
				// ++scriptState;
				// break;
				// case 13:
				).addDependent(
						new ScriptDialog("dialog.6", "campaign/portrait/1.png", "LEFT")
				// createDialogScreen(PaintableObject.getLocaleString(237), 1, 4);
				// ++scriptState;
				// break;
				// case 14:
				).addDependent(
						new ScriptCameraMove(5, 3)
				// moveMapAndCursor(3, 5);
				// break;
				// case 15:
				).addDependent(
						new ScriptDialog("dialog.7", "campaign/portrait/5.png", "LEFT")
				// createDialogScreen(PaintableObject.getLocaleString(238), 5, 4);
				// ++scriptState;
				// break;
				// case 16:
				).addDependent(
						new ScriptCameraMoveOnKing(0)
				// moveMapAndCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// break;
				// case 17:
				).addDependent(
				// currentHelp = 8;
				// ++scriptState;
				// break;
				// case 18:
				).addDependent(
						// currentHelp = 9;
						// ++scriptState;
						// break;
						new ScriptShowTarget("name", "target")
				// case 19:
				).addDependent(
						// new ScriptShowCursor(), // ).addDependent(
						new ScriptShowInfoBar(), // ).addDependent(
						new ScriptEnableActiveGame(), // ).addDependent(
						new ScriptSetUnitSpeed(8), // ).addDependent(
						new ScriptSetCameraSpeed(6)
				// startNormalPlay();
				// ++scriptState;
				// break;
				// case 20:
				).addDependent(
				// if(gameState == 9 && currentTurningPlayer == 0)
				// {
				// currentHelp = 10;
				// ++scriptState;
				// }
				// break;
				// case 21:
				).addDependent(
				// currentHelp = 11;
				// ++scriptState;
				// break;
				// case 22:
				).addDependent(
				// if(fractionKings[0].unitState == 3 || fractionKings[1].unitState == 3)
				// {
				// currentHelp = 12;
				// ++scriptState;
				// }
				// break;
				// case 23:
				).addDependent(
						// if(countUnits(-1, -1, 1) == 0 && countCastles(1) == 0)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionUnitNumber(1, 0, 0), new ConditionCastleNumber(1, 0, 0))).addDependent(
								// activeFlag = false;
								new ScriptDelay(1333)
				// setScriptDelay(20);
				// ++scriptState;
				// }
				// break;
				// case 24:
				).addDependent(
						new ScriptCameraMoveOnKing(0)
				// moveMapAndCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// break;
				// case 25:
				).addDependent(
						new ScriptDialog("dialog.8", "campaign/portrait/1.png", "LEFT")
				// createDialogScreen(PaintableObject.getLocaleString(239), 1, 4);
				// ++scriptState;
				// break;
				// case 26:
				).addDependent(
						new ScriptDialog("dialog.9", "campaign/portrait/5.png", "LEFT")).addDependent(
								// createDialogScreen(PaintableObject.getLocaleString(240), 5, 4);
								new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 27:
				).addDependent(
				// completeMission();
				// case end
				).addDependent(
						new ScriptMissionComplete("missionComplete")).addDependent(
								new ScriptCloseMission())
						.getStartScript(),
				new ScriptGameOver().setConditions(new ConditionCastleNumber(0, 0, 0), new ConditionNamedUnitDead("king"))
		};
		save(endScripts, file);
	}
	*/

	public void convert(int iMission) throws IOException
	{
		//for (int iMission = iMission2; iMission <= iMission2; iMission++)
		{
			Scanner input = new Scanner(new File("../campaigns/" + iMission + ".txt"));
			ArrayList<String> stringsArrayList = new ArrayList<String>();
			while (input.hasNext())
				stringsArrayList.add(input.nextLine().replaceAll("\\(byte\\)", "").replaceAll("\t", ""));
			stringsArrayList.add("case end");
			String[] strings = stringsArrayList.toArray(new String[0]);
			
			/*
			System.out.println("private static ArrayList<Pair> mapLevel" + iMission + "(int i)");
			System.out.println("{");
			System.out.println("ArrayList<Pair> map = new ArrayList<Pair>();");
			System.out.println("map.add(new Pair(121 + i, \"name\"));");
			System.out.println("map.add(new Pair(113 + i, \"title\"));");
			System.out.println("map.add(new Pair(129 + i, \"target\"));");
			System.out.println("map.add(new Pair(121 + i, \"name\"));");
			*/

			// int n = 0;
			if (0 == 1)
			{
				for (String s : strings)
					// if (!s.contains("getUnit") && !s.contains("createUnit") && s.contains("plotRoute"))
					if (s.contains("mapStepMax"))
						System.out.println(iMission + " " + s);
				//continue;
			}
			// System.out.println("map.add(new Pair(" + string.replaceAll("\t", "").replace("createDialogScreen(PaintableObject.getLocaleString(", "").substring(0, 3) + ", \"dialog." + n++ + "\"));");
			
			/*
			System.out.println("map.add(new Pair(72, \"missionComplete\"));");
			System.out.println("return map;");
			System.out.println("}");
			*/

			String alphaChange = "Show";

			int n = 0;
			ArrayList<ArrayList<String>> cases = new ArrayList<ArrayList<String>>();
			for (int iStart = 0, iEnd = 0; iStart < strings.length; iStart = iEnd + 2, iEnd = iStart)
			{
				while (!strings[iEnd + 1].contains("case"))
					iEnd++;

				ArrayList<String> scriptsStrings = new ArrayList<String>();
				for (int i = iStart; i <= iEnd; i++)
				{
					String s = strings[i];
					if (s.equals("\tbreak;"))
						continue;
					if (s.contains("drawCursorFlag = false;"))
						scriptsStrings.add("new ScriptHideCursor()");
					else if (s.contains("drawCursorFlag = true;"))
						scriptsStrings.add("new ScriptShowCursor()");
					else if (s.contains("setScriptDelay("))
						scriptsStrings.add("new ScriptDelay(" + Integer.valueOf(s.replace("setScriptDelay(", "").replace(");", "").trim()) * 1000 / 15 + ")");
					else if (s.contains("updateAlphaDrap = true;") || s.contains("drawAlphaDrap = true;") || s.contains("inverseDrapAlpha = true;"))
					{
						scriptsStrings.add("new Script" + alphaChange + "BlackScreen()");
						alphaChange = alphaChange == "Hide" ? "Show" : "Hide";
					}
					else if (s.contains("getUnit") && s.contains("plotRoute"))
					{
						String[] args = s.replace("getUnit(", "").replace("0).plotRoute(", "").replace(", false);", "").replace(", true);", "").split(",");
						scriptsStrings.add("new ScriptUnitMove(" + args[1] + ", " + args[0] + ", " + args[3] + ", " + args[2] + ")");
					}
					else if (s.contains("moveMapAndCursor(fraction"))
						scriptsStrings.add("new ScriptCameraMoveOnKing(" + (s.contains("0") ? 0 : 1) + ")");
					else if (s.contains("moveMapAndCursor") && !s.contains("crystall"))
					{
						String[] args = s.replace("moveMapAndCursor(", "").replace(");", "").split(",");
						scriptsStrings.add("new ScriptCameraMove(" + args[1] + ", " + args[0] + ")");
					}
					else if (s.contains("createDialogScreen"))
						scriptsStrings.add("new ScriptDialog(\"dialog." + n++ + "\", \"campaign/portrait/" + s.charAt(57) + ".png\", \"LEFT\")");
						// createDialogScreen(PaintableObject.getLocaleString(221), (byte)2, (byte)4);
						// new ScriptDialog("dialog.0", "campaign/portrait/2.png", "LEFT")).addDependent(
					else if (s.contains("moveMapAndCursor(fractionKings"))
						scriptsStrings.add("new ScriptCameraMoveOnKing(" + s.replace("moveMapAndCursor(fractionKings[", "").charAt(0) + ")");
					else if (s.contains("startNormalPlay"))
					{
						scriptsStrings.add("new ScriptShowTarget(\"name\", \"target\")).addDependent(");
						scriptsStrings.add("new ScriptShowCursor()");
						scriptsStrings.add("new ScriptShowInfoBar()");
						scriptsStrings.add("new ScriptEnableActiveGame()");
						scriptsStrings.add("new ScriptSetUnitSpeed(8)");
						scriptsStrings.add("new ScriptSetCameraSpeed(6)");
					}
					else if (s.contains("if(countUnits(-1, -1, (byte)1) == 0)"))
						scriptsStrings.add("\t//.setConditions(new ConditionUnitNumber(1, 0, 0))");
					else if (s.contains("if(countUnits(-1, -1, (byte)1) == 0 && countCastles(1) == 0)"))
						scriptsStrings.add("\t//.setConditions(new ConditionUnitNumber(1, 0, 0), new ConditionCastleNumber(1, 0, 0))");
					else if (s.contains("createUnit(") && !s.contains("MapPos"))
					{
						String[] args = s.replaceFirst(".*createUnit\\(", "").split(",");
						scriptsStrings.add("new ScriptUnitCreate(" + args[3].split("\\)")[0] + ", " + args[2] + ", \"" + GameConverter.getUnitTypeName(Integer.valueOf(args[0]), 0) + "\", " + args[1] + ")");
						if (s.contains("plotRoute"))
						{
							String[] args2 = s.split("plotRoute")[1].replace("Ex", "").substring(1).replace(");", "").split(",");
							scriptsStrings.add("new ScriptUnitMoveAbout(" + args[3].split("\\)")[0] + ", " + args[2] + ", " + args2[1] + ", " + args2[0] + ")");
							// new ScriptUnitCreate(1, 1, "ARCHER", 1),
							// new ScriptUnitMoveAbout(1, 1, 2, 1)
						}
					}
					else if (s.contains("activeFlag"))
						scriptsStrings.add("new Script" + (s.contains("true") ? "Enable" : "Disable") + "ActiveGame()");
					else if (s.contains("Unit.speed"))
					{
						String speed = s.replace("Unit.speed = ", "").replace(";", "");
						if (speed.contains("SLOW"))
							speed = "8";
						else if (speed.contains("FAST"))
							speed = "4";
						else
							speed = "" + 48 / Integer.valueOf(speed);
						scriptsStrings.add("new ScriptSetUnitSpeed(" + speed + ")");
					}
					else if (s.contains("mapStepMax"))
					{
						String speed = s.replace("mapStepMax = ", "").replace(";", "");
						speed = "" + Integer.valueOf(speed) / 2;
						scriptsStrings.add("new ScriptSetCameraSpeed(" + speed + ")");
					}
					scriptsStrings.add("\t//" + s);
					// " + s.replace("", "").replace("", "") + "
				}
				int lastI = -1;
				for (int i = 0; i < scriptsStrings.size() - 1; i++)
					if (!scriptsStrings.get(i).startsWith("\t//"))
					{
						scriptsStrings.set(i, scriptsStrings.get(i) + ",// ).addDependent(");
						lastI = i;
					}
				if (lastI != -1)
					scriptsStrings.set(lastI, scriptsStrings.get(lastI).replace(",// ).addDependent(", ""));
				scriptsStrings.add("//" + strings[iEnd + 1]);
				cases.add(scriptsStrings);
			}

			System.out.println("public void mission" + iMission + "(String file) throws IOException");
			System.out.println("{");
			System.out.println("// new Script()).addDependent(");
			System.out.println("Script[] endScripts =");
			System.out.println("{");

			System.out.println("new ScriptSetNamedUnit(, , \"king\"),");
			System.out.println("new ScriptBlackScreen(),");
			System.out.println("new ScriptHideInfoBar(),");
			System.out.println("new ScriptDisableActiveGame(),");
			// cases.add(0, new ArrayList<String>(Arrays.asList("new ScriptDisableActiveGame(")));
			// cases.add(0, new ArrayList<String>(Arrays.asList("new ScriptHideInfoBar(")));
			// cases.add(0, new ArrayList<String>(Arrays.asList("new ScriptBlackScreen(")));
			cases.add(0, new ArrayList<String>(Arrays.asList("new ScriptTitle(\"title\"")));
			cases.get(1).add(0, "new ScriptHideBlackScreen(),// ).addDependent(");
			for (ArrayList<String> arrayList : cases)
			{
				for (String string : arrayList)
					System.out.println(string);
				System.out.println(").addDependent(");
			}

			System.out.println("new ScriptMissionComplete(\"missionComplete\")).addDependent(");
			System.out.println("new ScriptCloseMission()).getStartScript(), ");
			System.out.println("new ScriptGameOver().setConditions(new ConditionCastleNumber(0, 0, 0), new ConditionNamedUnitDead(\"king\"))");
			System.out.println("};");
			System.out.println("save(endScripts, file);");
			System.out.println("}");
			input.close();
			// System.out.println("");
		}
	}
	
	/*
	public void mission2(String file) throws IOException
	{
		// new Script()).addDependent(
		Script[] endScripts =
		{
				new ScriptSetNamedUnit(16, 8, "king"),
				new ScriptBlackScreen(),
				new ScriptHideCursor(),
				new ScriptHideInfoBar(),
				new ScriptDisableActiveGame(),
				new ScriptSetUnitSpeed(12),
				new ScriptTitle("title").addDependent(
						new ScriptHideBlackScreen(), // ).addDependent(
						// allowedUnits = 0;
						// money[0] = 0;
						// money[1] = 0;
						// Unit.speed = 4;
						// money[0] = 0;
						// moveMapShowPoint(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
						// moveCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
						// crystallEscortLeader = getUnit(8, 17, 0);
						// crystallEscortCrystall = getUnit(8, 18, 0);
						// crystallEscortFollower = getUnit(8, 19, 0);
						new ScriptUnitMove(16, 8, 14, 8), // ).addDependent(
						new ScriptUnitMoveExtended(17, 8, 15, 8, 15, 7, 14, 7), // ).addDependent(
						// getUnit(8, 17, 0).plotRoute(8, 15, false);
						new ScriptUnitMoveExtended(18, 8, 15, 8, 15, 7), // ).addDependent(
						// getUnit(8, 18, 0).plotRoute(8, 15, false);
						new ScriptUnitMove(19, 8, 15, 8)
				// getUnit(8, 19, 0).plotRoute(8, 15, false);
				// getUnit(8, 16, 0).plotRoute(8, 14, false);
				// drawCursorFlag = false;
				// PaintableObject.currentRenderer.setCurrentDisplayable(auxMapNameMessage);
				// scriptState = 0;
				//
				// case 0:
				).addDependent(
				// if(crystallEscortLeader.unitState != 1)
				// {
				// crystallEscortLeader.plotRoute(7, 14, false);
				// ++scriptState;
				// }
				// break;
				// case 1:
				).addDependent(
						// if(crystallEscortCrystall.unitState != 1)
						// {
						// crystallEscortCrystall.plotRoute(7, 15, false);
						new ScriptSetUnitSpeed(8)// ).addDependent(
				// new ScriptDelay(1333)
				// setScriptDelay(20);
				// ++scriptState;
				// }
				// break;
				// case 2:
				).addDependent(
						// Unit.speed = Unit.UNIT_SPEED_SLOW;
						// crystallEscortLeader = null;
						// crystallEscortCrystall = null;
						// crystallEscortFollower = null;
						new ScriptDialog("dialog.0", "campaign/portrait/5.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(241), 5, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 3:
				).addDependent(
						new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(242), 0, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 4:
				).addDependent(
						new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(243), 5, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 5:
				).addDependent(
				// currentHelp = 14;
				// ++scriptState;
				// break;
				// case 6:
				).addDependent(
						new ScriptShowTarget("name", "target")).addDependent(
								// new ScriptShowCursor(), // ).addDependent(
								new ScriptShowInfoBar(), // ).addDependent(
								new ScriptEnableActiveGame(), // ).addDependent(
								new ScriptSetUnitSpeed(8), // ).addDependent(
								new ScriptSetCameraSpeed(6)
				// startNormalPlay();
				// ++scriptState;
				// break;
				// case 7:
				).addDependent(
						// case 8:
						// if(scriptState == 7 && currentTurningPlayer == 0 && statusBarOffset == 0 && countUnits(-1, 3, -1) >= 1)
						// {
						// currentHelp = 15;
						// ++scriptState;
						// }
						//
						// var14 = listUnits(-1, 2, 0);
						// var2 = 0;
						//
						// while(true)
						// {
						// if(var2 >= var14.length)
						// {
						// break label597;
						// }
						//
						// if(var14[var2].currentMapPosX <= 4 || var14[var2].currentMapPosY <= 10)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionUnitIntoBounds(0, new Bounds(0, 0, 10, w), new Bounds(0, 0, h, 4)))).addDependent(
								new ScriptHideCursor(),
								new ScriptDelay(666)// ).addDependent(
				// setScriptDelay(10);
				// activeFlag = false;
				// drawCursorFlag = false;
				// scriptState = 9;
				// break label597;
				// }
				//
				// ++var2;
				// }
				// case 9:
				).addDependent(
						new ScriptCameraMove(8, 0)).addDependent(
								// moveMapAndCursor(0, 8);
								new ScriptUnitCreate(8, -1, "DIRE_WOLF", 1), // ).addDependent(
								new ScriptUnitMove(8, -1, 8, 0), // ).addDependent(
								// Unit.createUnit(5, 1, -1, 8).plotRoute(0, 8, false);
								new ScriptUnitCreate(7, -2, "DIRE_WOLF", 1), // ).addDependent(
								new ScriptUnitMove(7, -2, 7, 1))
						.addDependent(
				// Unit.createUnit(5, 1, -2, 7).plotRoute(1, 7, false);
				// new ScriptDelay(1333)
				// setScriptDelay(20);
				// break;
				// case 10:
				).addDependent(
						new ScriptCameraMove(6, 8)).addDependent(
								// moveMapAndCursor(8, 6);
								new ScriptUnitCreate(6, 11, "DIRE_WOLF", 1), // ).addDependent(
								new ScriptUnitMove(6, 11, 6, 8))
						.addDependent(
				// Unit.createUnit(5, 1, 12, 6).plotRoute(8, 6, false);
				// new ScriptDelay(1333)
				// setScriptDelay(20);
				// break;
				// case 11:
				).addDependent(
						new ScriptCameraMove(1, 2)).addDependent(
								// moveMapAndCursor(2, 1);
								new ScriptUnitCreate(-1, 2, "WISP", 1), // ).addDependent(
								new ScriptUnitMove(-1, 2, 1, 2), // ).addDependent(
								// Unit.createUnit(4, 1, 2, -1).plotRoute(2, 1, false);
								new ScriptUnitCreate(-2, 1, "DIRE_WOLF", 1), // ).addDependent(
								new ScriptUnitMove(-2, 1, 2, 1), // ).addDependent(
								// Unit.createUnit(5, 1, 1, -2).plotRoute(1, 2, false);
								new ScriptUnitCreate(-2, 3, "DIRE_WOLF", 1), // ).addDependent(
								new ScriptUnitMove(-2, 3, 2, 3))
						.addDependent(
				// Unit.createUnit(5, 1, 3, -2).plotRoute(3, 2, false);
				// new ScriptDelay(1333)
				// setScriptDelay(20);
				// break;
				// case 12:
				).addDependent(
						new ScriptDialog("dialog.3", "campaign/portrait/5.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(244), 5, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 13:
				).addDependent(
						new ScriptCameraMove(8, 4)).addDependent(
				// moveMapAndCursor(4, 8);
				// new ScriptDelay(1000)
				// setScriptDelay(15);
				// break;
				// case 14:
				).addDependent(
						new ScriptUnitCreate(8, 3, "ELEMENTAL", 1), // ).addDependent(
						// var11 = Unit.createUnit(2, 1, 3, 8);
						new ScriptUnitCreate(7, 4, "ELEMENTAL", 1), // ).addDependent(
						// var3 = Unit.createUnit(2, 1, 4, 7);
						new ScriptUnitCreate(8, 5, "ELEMENTAL", 1), // ).addDependent(
						// var4 = Unit.createUnit(2, 1, 5, 8);
						// createSimpleSparkSprite(sprSpark, var11.currentX, var11.currentY, 0, 0, 1, 50);
						// createSimpleSparkSprite(sprSpark, var3.currentX, var3.currentY, 0, 0, 1, 50);
						// createSimpleSparkSprite(sprSpark, var4.currentX, var4.currentY, 0, 0, 1, 50);
						new ScriptSparkDefault(8, 3),
						new ScriptSparkDefault(7, 4),
						new ScriptSparkDefault(8, 5)
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 15:
				).addDependent(
						new ScriptDialog("dialog.4", "campaign/portrait/5.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(245), 5, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 16:
				).addDependent(
						new ScriptDialogWithoutImage("dialog.5")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(246), -1, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 17:
				).addDependent(
						new ScriptDialog("dialog.6", "campaign/portrait/0.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(247), 0, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 18:
				).addDependent(
				
				// TODO показать Toast!!!
				
				// PaintableObject.currentRenderer.setCurrentDisplayable(createMessageScreen((String)null, PaintableObject.getLocaleString(248), height_, 1500));
				// ++scriptState;
				// break;
				// case 19:
				).addDependent(
						// getUnit(3, 8, 0).removeThisUnit();
						// getUnit(4, 7, 0).removeThisUnit();
						// getUnit(5, 8, 0).removeThisUnit();
						new ScriptRemoveUnit(8, 3),
						new ScriptRemoveUnit(7, 4),
						new ScriptRemoveUnit(8, 5),
						new ScriptUnitCreate(8, 3, "ELEMENTAL", 0), // ).addDependent(
						// var11 = Unit.createUnit(2, 0, 3, 8);
						new ScriptUnitCreate(7, 4, "ELEMENTAL", 0), // ).addDependent(
						// var3 = Unit.createUnit(2, 0, 4, 7);
						new ScriptUnitCreate(8, 5, "ELEMENTAL", 0), // ).addDependent(
						// var4 = Unit.createUnit(2, 0, 5, 8);
						// createSimpleSparkSprite(sprSpark, var11.currentX, var11.currentY, 0, 0, 1, 50);
						// createSimpleSparkSprite(sprSpark, var3.currentX, var3.currentY, 0, 0, 1, 50);
						// createSimpleSparkSprite(sprSpark, var4.currentX, var4.currentY, 0, 0, 1, 50);
						new ScriptSparkDefault(8, 3),
						new ScriptSparkDefault(7, 4),
						new ScriptSparkDefault(8, 5)
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 20:
				).addDependent(
						new ScriptEnableActiveGame() // ).addDependent(
				// activeFlag = true;
				// new ScriptShowCursor()
				// drawCursorFlag = true;
				// currentHelp = 13;
				// ++scriptState;
				// break;
				// case 21:
				).addDependent(
						// if(countUnits(-1, -1, 1) == 0)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionUnitNumber(1, 0, 0))).addDependent(
								// activeFlag = false;
								new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// }
				// break;
				// case 22:
				).addDependent(
				// completeMission();
				// case end
				).addDependent(
						new ScriptMissionComplete("missionComplete")).addDependent(
								new ScriptCloseMission())
						.getStartScript(),
				new ScriptGameOver().setConditions(new ConditionCastleNumber(0, 0, 0), new ConditionNamedUnitDead("king"))
		};
		save(endScripts, file);
	}
	
	public void mission3(String file) throws IOException
	{
		// new Script()).addDependent(
		Script[] endScripts =
		{
				new ScriptSetNamedUnit(5, 0, "king"),
				new ScriptBlackScreen(),
				new ScriptHideCursor(),
				new ScriptHideInfoBar(),
				new ScriptDisableActiveGame(),
				new ScriptSetUnitSpeed(12), // ).addDependent(
				new ScriptSetCameraSpeed(1), // ).addDependent(
				new ScriptTitle("title").addDependent(
						new ScriptHideBlackScreen(), // ).addDependent(
						// allowedUnits = 7;
						// Unit.speed = 4;
						// money[0] = 400;
						// money[1] = 400;
						new ScriptUnitCreate(5, -1, "SOLDIER", 0), // ).addDependent(
						new ScriptUnitCreate(5, -2, "ELEMENTAL", 0), // ).addDependent(
						new ScriptUnitCreate(5, -3, "SORCERESS", 0), // ).addDependent(
						new ScriptUnitMoveExtended(5, 0, 5, 3, 3, 3), // ).addDependent(
						new ScriptUnitMoveExtended(5, -1, 5, 3, 4, 3), // ).addDependent(
						new ScriptUnitMoveExtended(5, -3, 5, 2, 4, 2), // ).addDependent(
						new ScriptUnitMoveExtended(5, -2, 5, 4, 4, 4)// ).addDependent(
				// crystallEscortLeader = Unit.createUnit(0, 0, -1, 5);
				// crystallEscortCrystall = Unit.createUnit(2, 0, -2, 5);
				// crystallEscortFollower = Unit.createUnit(3, 0, -3, 5);
				// new ScriptUnitCreate(5, -1, "SOLDIER", 0),// ).addDependent(
				// Unit.createUnit(0, 0, -1, 5).plotRoute(3, 4, false);
				// new ScriptUnitCreate(5, -2, "ELEMENTAL", 0),// ).addDependent(
				// Unit.createUnit(2, 0, -2, 5).plotRoute(4, 4, false);
				// new ScriptUnitCreate(5, -3, "SORCERESS", 0),// ).addDependent(
				// Unit.createUnit(3, 0, -3, 5).plotRoute(2, 4, false);
				// getUnit(0, 5, 0).plotRoute(3, 3, false);
				// moveMapShowPoint(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// moveCursor(3, 3);
				// var_31be = fractionKings[0];
				// setIntroMode(3, 3, 3);
				// Renderer.startPlayer_(1, 1);
				// updateAlphaDrap = false;
				// drawCursorFlag = false;
				// scriptState = 0;
				//
				// case 0:
				).addDependent(
						// PaintableObject.currentRenderer.setCurrentDisplayable(auxMapNameMessage);
						new ScriptShowBlackScreen()
				// updateAlphaDrap = true;
				// ++scriptState;
				// break;
				// case 1:
				).addDependent(
						// if(fractionKings[0].unitState != 1)
						// {
						// var_31be = null;
						new ScriptDelay(666)
				// setScriptDelay(20);
				// ++scriptState;
				// }
				// break;
				// case 2:
				).addDependent(
				// new ScriptHideBlackScreen(),// ).addDependent(
				// drawAlphaDrap = true;
				// new ScriptHideBlackScreen()// ).addDependent(
				// updateAlphaDrap = true;
				// drapAlphaValue = 0;
				// new ScriptDelay(1333)
				// setScriptDelay(20);
				// ++scriptState;
				// break;
				// case 3:
				).addDependent(
						new ScriptDialog("dialog.0", "campaign/portrait/2.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(249), 2, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 4:
				).addDependent(
						new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(250), 0, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 5:
				).addDependent(
						new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT")).addDependent(
								new ScriptUnitChangePosition(3, 3, 1, 7),
								new ScriptUnitChangePosition(4, 3, 4, 5),
								new ScriptUnitChangePosition(4, 4, 5, 7),
								new ScriptUnitChangePosition(4, 2, 3, 3),
								new ScriptSetMapPosition(3, 13)
				// createDialogScreen(PaintableObject.getLocaleString(251), 5, 4);
				// moveCursor(13, 3);
				// moveMapShowPointPixels(312, 72);
				// fractionKings[0].setMapPosition(7, 1);
				// crystallEscortLeader.setMapPosition(5, 4);
				// crystallEscortCrystall.setMapPosition(7, 5);
				// crystallEscortFollower.setMapPosition(3, 3);
				// crystallEscortLeader = null;
				// crystallEscortCrystall = null;
				// crystallEscortFollower = null;
				// mapStepMax = 2;
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 6:
				).addDependent(
						// drawAlphaDrap = false;
						new ScriptHideBlackScreen()// ).addDependent(
				// inverseDrapAlpha = true;
				// drapAlphaValue = 0;
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 7:
				).addDependent(
						new ScriptSetCameraSpeed(1), // ).addDependent(
						new ScriptCameraMove(10, 13)
				// moveMapAndCursor(13, 10);
				// break;
				// case 8:
				).addDependent(
						new ScriptSetUnitSpeed(24), // ).addDependent(
						// Unit.speed = 2;
						new ScriptSetCameraSpeed(2), // ).addDependent(
						// mapStepMax = 4;
						// crystallEscortLeader = getUnit(10, 10, 0);
						new ScriptCameraMove(10, 6),
						new ScriptUnitMove(10, 10, 10, 6)// ).addDependent(
				// getUnit(10, 10, 0).plotRoute(6, 10, false);
				// moveCursor(6, 10);
				// moveMapAndCursor(6, 10);
				// break;
				// case 9:
				).addDependent(
						// if(crystallEscortLeader.unitState != 1)
						// {
						// crystallEscortLeader = null;
						new ScriptShowCursor(), // ).addDependent(
						// drawCursorFlag = true;
						new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// }
				// break;
				// case 10:
				).addDependent(
						// moveCursor(4, 9);
						new ScriptCameraMove(9, 4), // ).addDependent(
						// moveMapAndCursor(4, 9);
						// createSimpleSparkSprite(sprRedSpark, 96, 216, 0, 0, 1, 50);
						new ScriptSparkAttack(9, 4)
				// new ScriptDelay(1000)
				// setScriptDelay(15);
				// break;
				// case 11:
				).addDependent(
						// var_16e5 = Unit.createUnitEx(0, 0, 4, 9, false);
						// var_16e5.type = -1;
						// var_16e5.unitState = 4;
						// var_17d5 = 6;
						new ScriptDelay(1333)).addDependent(
								new ScriptCellAttackPartTwo(9, 4)
				// setScriptDelay(20);
				// ++scriptState;
				// break;
				// case 12:
				).addDependent(
						new ScriptDialog("dialog.3", "campaign/portrait/5.png", "LEFT")).addDependent(
								// createDialogScreen(PaintableObject.getLocaleString(252), 5, 4);
								new ScriptCameraMove(1, 7)
				// moveMapAndCursor(7, 1);
				// break;
				// case 13:
				).addDependent(
						new ScriptDialog("dialog.4", "campaign/portrait/0.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(253), 0, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 14:
				).addDependent(
				// currentHelp = 17;
				// ++scriptState;
				// break;
				// case 15:
				).addDependent(
						new ScriptShowTarget("name", "target")).addDependent(// ).addDependent(
								// new ScriptShowCursor(), // ).addDependent(
								new ScriptShowInfoBar(), // ).addDependent(
								new ScriptEnableActiveGame(), // ).addDependent(
								new ScriptSetUnitSpeed(8), // ).addDependent(
								new ScriptSetCameraSpeed(6)
				// startNormalPlay();
				// ++scriptState;
				// break;
				// case 16:
				).addDependent(
						// if(countUnits(-1, -1, 1) == 0 && countCastles(1) == 0)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionUnitNumber(1, 0, 0), new ConditionCastleNumber(1, 0, 0))).addDependent(
								new ScriptDelay(1000)// ).addDependent(
				// setScriptDelay(15);
				// activeFlag = false;
				// ++scriptState;
				// }
				// break;
				// case 17:
				).addDependent(
						new ScriptDialog("dialog.5", "campaign/portrait/5.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(254), 5, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 18:
				).addDependent(
						new ScriptDialog("dialog.6", "campaign/portrait/0.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(255), 0, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 19:
				).addDependent(
				// completeMission();
				// case end
				).addDependent(
						new ScriptMissionComplete("missionComplete")).addDependent(
								new ScriptCloseMission())
						.getStartScript(),
				new ScriptGameOver().setConditions(new ConditionCastleNumber(0, 0, 0), new ConditionNamedUnitDead("king"))
		};
		save(endScripts, file);
	}
	
	public void mission4(String file) throws IOException
	{
		// new Script()).addDependent(
		Script[] endScripts =
		{
				new ScriptSetNamedUnit(-5, 11, "king"),
				new ScriptSetNamedUnit(-7, 11, "crystall"),
				new ScriptBlackScreen(),
				new ScriptHideCursor(),
				new ScriptHideInfoBar(),
				new ScriptDisableActiveGame(),
				new ScriptTitle("title").addDependent(
						new ScriptHideBlackScreen(), // ).addDependent(
						// allowedUnits = 0;
						// money[0] = 0;
						// money[1] = 0;
						// moveMapShowPointPixels(fractionKings[0].currentX + 12, fractionKings[0].currentY + 12);
						// moveCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
						new ScriptSetUnitSpeed(12), // ).addDependent(
						// Unit.speed = 4;
						// (var1 = getUnit(11, 2, 0)).setMapPosition(11, -3);
						new ScriptUnitMove(-3, 11, 2, 11), // ).addDependent(
						// getUnit(11, -3, 0).plotRoute(11, 2, false);
						// (var1 = getUnit(10, 1, 0)).setMapPosition(10, -5);
						new ScriptUnitMove(-5, 10, 1, 10), // ).addDependent(
						// getUnit(10, -5, 0).plotRoute(10, 1, false);
						// (var1 = getUnit(11, 1, 0)).setMapPosition(11, -5);
						new ScriptUnitMove(-5, 11, 1, 11), // ).addDependent(
						// getUnit(11, -5, 0).plotRoute(11, 1, false);
						// (var1 = getUnit(12, 1, 0)).setMapPosition(12, -5);
						new ScriptUnitMove(-5, 12, 1, 12), // ).addDependent(
						// getUnit(12, -5, 0).plotRoute(12, 1, false);
						// (var1 = getUnit(11, 0, 0)).setMapPosition(11, -7);
						new ScriptUnitMove(-7, 11, 0, 11), // ).addDependent(
						// getUnit(11, -7, 0).plotRoute(11, 0, false);
						// (var1 = getUnit(12, 0, 0)).setMapPosition(12, -7);
						new ScriptUnitMove(-7, 12, 0, 12)).addDependent(
				// getUnit(12, -7, 0).plotRoute(12, 0, false);
				// PaintableObject.currentRenderer.setCurrentDisplayable(auxMapNameMessage);
				// drawCursorFlag = false;
				// scriptState = 0;
				//
				// if(var_3c5c == null)
				// {
				// System.out.println("null " + System.currentTimeMillis());
				// var_3c5c = listUnits(11, -1, 0)[0];
				// }
				//
				//
				// if(var_3c5c.unitState == 3)
				// {
				// var_3c5c = null;
				// sub_131d();
				// return;
				// }
				//
				// switch(scriptState)
				// {
				// case 0:
				).addDependent(
						// new ScriptDelay(3333)
						new ScriptDelay(333)
				// setScriptDelay(50);
				// ++scriptState;
				// break;
				// case 1:
				).addDependent(
						new ScriptDialog("dialog.0", "campaign/portrait/5.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(256), 5, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 2:
				).addDependent(
						new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(257), 0, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 3:
				).addDependent(
						new ScriptShowTarget("name", "target")).addDependent(// ).addDependent(
								// new ScriptShowCursor(), // ).addDependent(
								new ScriptShowInfoBar(), // ).addDependent(
								new ScriptEnableActiveGame(), // ).addDependent(
								new ScriptSetUnitSpeed(8), // ).addDependent(
								new ScriptSetCameraSpeed(6)
				// startNormalPlay();
				// ++scriptState;
				// break;
				// case 4:
				).addDependent(
						// var14 = listUnits(-1, -1, 0);
						// var2 = 0;
						//
						// while(true)
						// {
						// if(var2 >= var14.length)
						// {
						// break label580;
						// }
						//
						// if(var14[var2].unitState == 2 && var14[var2].currentMapPosX <= 8)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionUnitIntoBounds(0, new Bounds(0, 0, h, 8)))).addDependent(
								// activeFlag = false;
								new ScriptHideCursor(), // ).addDependent(
								new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break label580;
				// }
				//
				// ++var2;
				// }
				// case 5:
				).addDependent(
						// drawCursorFlag = false;
						new ScriptCameraMove(4, 4)
				// moveMapAndCursor(4, 4);
				// break;
				// case 6:
				).addDependent(
						new ScriptUnitCreate(4, 4, "SKELETON", 1), // ).addDependent(
						new ScriptUnitMove(4, 4, 1, 4)// ).addDependent(
				// Unit.createUnit(10, 1, 4, 4).plotRouteEx(4, 1, false, true);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 7:
				).addDependent(
						new ScriptUnitCreate(4, 4, "ARCHER", 1), // ).addDependent(
						new ScriptUnitMoveExtended(4, 4, 4, 5, 2, 5)// ).addDependent(
				// Unit.createUnit(1, 1, 4, 4).plotRouteEx(5, 2, false, true);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 8:
				).addDependent(
						new ScriptUnitCreate(4, 4, "SKELETON", 1), // ).addDependent(
						new ScriptUnitMove(4, 4, 3, 4)// ).addDependent(
				// Unit.createUnit(10, 1, 4, 4).plotRouteEx(4, 3, false, true);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 9:
				).addDependent(
						new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT")).addDependent(
								// createDialogScreen(PaintableObject.getLocaleString(258), 5, 4);
								new ScriptCameraMoveOnKing(0)
				// moveMapAndCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// break;
				// case 10:
				).addDependent(
						// new ScriptShowCursor(),
						new ScriptEnableActiveGame()// ).addDependent(
				// activeFlag = true;
				// drawCursorFlag = true;
				// ++scriptState;
				// break;
				// case 11:
				).addDependent(
						// var14 = listUnits(-1, -1, 0);
						// var2 = 0;
						//
						// while(true)
						// {
						// if(var2 >= var14.length)
						// {
						// break label580;
						// }
						//
						// if(var14[var2].unitState == 2 && var14[var2].currentMapPosY >= 7)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionUnitIntoBounds(0, new Bounds(7, 0, h, w)))).addDependent(
								// activeFlag = false;
								new ScriptHideCursor(), // ).addDependent(
								// drawCursorFlag = false;
								new ScriptCameraMove(10, 6)
				// moveMapAndCursor(6, 10);
				// break label580;
				// }
				// ++var2;
				// }
				// case 12:
				).addDependent(
						new ScriptUnitCreate(10, 6, "ARCHER", 1), // ).addDependent(
						new ScriptUnitMoveAbout(10, 6, 10, 5)// ).addDependent(
				// Unit.createUnit(1, 1, 6, 10).plotRouteEx(5, 10, false, true);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 13:
				).addDependent(
						new ScriptUnitCreate(10, 6, "DIRE_WOLF", 1), // ).addDependent(
						new ScriptUnitMoveAbout(10, 6, 8, 7)// ).addDependent(
				// Unit.createUnit(5, 1, 6, 10).plotRouteEx(7, 8, false, true);
				// new ScriptDelay(1000)
				// setScriptDelay(15);
				// ++scriptState;
				// break;
				// case 14:
				).addDependent(
						new ScriptUnitCreate(10, 6, "DIRE_WOLF", 1), // ).addDependent(
						new ScriptUnitMoveAbout(10, 6, 9, 7)).addDependent(
								// Unit.createUnit(5, 1, 6, 10).plotRouteEx(7, 9, false, true);
								// new ScriptShowCursor(),
								new ScriptEnableActiveGame()// ).addDependent(
				// activeFlag = true;
				// new ScriptShowCursor()
				// drawCursorFlag = true;
				// ++scriptState;
				// break;
				// case 15:
				).addDependent(
						// var14 = listUnits(-1, -1, 0);
						// var2 = 0;
						//
						// while(true)
						// {
						// if(var2 >= var14.length)
						// {
						// break label580;
						// }
						//
						// if(var14[var2].unitState == 2 && var14[var2].currentMapPosX >= 8 && var14[var2].currentMapPosY >= 6)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionUnitIntoBounds(0, new Bounds(6, 8, h, w)))).addDependent(
								// activeFlag = false;
								new ScriptHideCursor(), // ).addDependent(
								// drawCursorFlag = false;
								new ScriptCameraMove(5, 12)
				// moveMapAndCursor(12, 5);
				// break label580;
				// }
				//
				// ++var2;
				// }
				// case 16:
				).addDependent(
						new ScriptUnitCreate(5, 12, "DIRE_WOLF", 1), // ).addDependent(
						new ScriptUnitMoveAbout(5, 12, 7, 12)// ).addDependent(
				// Unit.createUnit(5, 1, 12, 5).plotRouteEx(12, 7, false, true);
				// new ScriptDelay(1000)
				// setScriptDelay(15);
				// ++scriptState;
				// break;
				// case 17:
				).addDependent(
						new ScriptUnitCreate(5, 12, "GOLEM", 1), // ).addDependent(
						new ScriptUnitMoveAbout(5, 12, 6, 12)// ).addDependent(
				// Unit.createUnit(6, 1, 12, 5).plotRouteEx(12, 6, false, true);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 18:
				).addDependent(
						new ScriptUnitCreate(5, 12, "DIRE_WOLF", 1), // ).addDependent(
						new ScriptUnitMoveAbout(5, 12, 5, 11)).addDependent(
								// Unit.createUnit(5, 1, 12, 5).plotRouteEx(12, 5, false, true);
								// new ScriptShowCursor(),
								new ScriptEnableActiveGame()// ).addDependent(
				// activeFlag = true;
				// drawCursorFlag = true;
				// ++scriptState;
				// break;
				// case 19:
				).addDependent(
						// var14 = listUnits(-1, -1, 0);
						// var2 = 0;
						//
						// while(true)
						// {
						// if(var2 >= var14.length)
						// {
						// break label580;
						// }
						//
						// if(var14[var2].unitState == 2 && var14[var2].currentMapPosX >= 15 && var14[var2].currentMapPosY >= 8)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionUnitIntoBounds(0, new Bounds(8, 15, h, w)))).addDependent(
								// activeFlag = false;
								new ScriptHideCursor(), // ).addDependent(
								// drawCursorFlag = false;
								new ScriptCameraMove(8, 18)
				// moveMapAndCursor(18, 8);
				// break label580;
				// }
				//
				// ++var2;
				// }
				// case 20:
				).addDependent(
						new ScriptUnitCreate(8, 18, "DIRE_WOLF", 1), // ).addDependent(
						new ScriptUnitMoveAbout(8, 18, 10, 16), // ).addDependent(
						// Unit.createUnit(5, 1, 18, 8).plotRouteEx(16, 10, false, true);
						new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 21:
				).addDependent(
						new ScriptUnitCreate(8, 18, "GOLEM", 1), // ).addDependent(
						new ScriptUnitMoveAbout(8, 18, 10, 17), // ).addDependent(
						// Unit.createUnit(6, 1, 18, 8).plotRouteEx(17, 10, false, true);
						new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 22:
				).addDependent(
						new ScriptUnitCreate(8, 18, "DIRE_WOLF", 1), // ).addDependent(
						new ScriptUnitMoveAbout(8, 18, 10, 18), // ).addDependent(
						// Unit.createUnit(5, 1, 18, 8).plotRouteEx(18, 10, false, true);
						new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 23:
				).addDependent(
						new ScriptUnitCreate(8, 18, "ARCHER", 1), // ).addDependent(
						new ScriptUnitMoveAbout(8, 18, 9, 18), // ).addDependent(
						// Unit.createUnit(1, 1, 18, 8).plotRouteEx(18, 9, false, true);
						new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 24:
				).addDependent(
						new ScriptDialog("dialog.3", "campaign/portrait/0.png", "LEFT")).addDependent(
								// createDialogScreen(PaintableObject.getLocaleString(259), 0, 4);
								// new ScriptShowCursor(),
								new ScriptEnableActiveGame() // ).addDependent(
				// activeFlag = true;
				// drawCursorFlag = true;
				// ++scriptState;
				// break;
				// case 25:
				).addDependent(
						// if(scriptState == 25 && var_3c5c.currentMapPosX >= 15 && var_3c5c.currentMapPosY >= 11 && var_3c5c.unitState == 2)
						// {
						// new ScriptDelay(666),// ).addDependent(
						// setScriptDelay(10);
						
				// activeFlag = false;
				// drawCursorFlag = false;
				// scriptState = 26;
				// return;
				// }
				// if(countUnits(-1, -1, 1) == 0)
				// {
						new ScriptDisableActiveGame().setConditions(
								new ConditionOr(new ConditionUnitNumber(1, 0, 0),
										new ConditionNamedUnitIntoBounds("crystall", new Bounds(11, 15, h, w)))))
						.addDependent(
								// activeFlag = false;
								new ScriptHideCursor(), // ).addDependent(
								// drawCursorFlag = false;
								new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// }
				// break;
				// case 26:
				).addDependent(
				// completeMission();
				// ++scriptState;
				// }
				// case end
				).addDependent(
						new ScriptMissionComplete("missionComplete")).addDependent(
								new ScriptCloseMission())
						.getStartScript(),
				new ScriptGameOver().setConditions(new ConditionOr(
						new ConditionNamedUnitDead("king"),
						new ConditionNamedUnitDead("crystall")))
		};
		save(endScripts, file);
	}
	
	public void mission5(String file) throws IOException
	{
		// new Script()).addDependent(
		Script[] endScripts =
		{
				new ScriptSetNamedUnit(17, 5, "king"),
				new ScriptBlackScreen(),
				new ScriptHideCursor(),
				new ScriptHideInfoBar(),
				new ScriptDisableActiveGame(),
				new ScriptSetCameraSpeed(2), // ).addDependent(
				new ScriptTitle("title").addDependent(
						new ScriptHideBlackScreen(), // ).addDependent(
						// allowedUnits = 7;
						// money[0] = 600;
						// money[1] = 600;
						// fractionKings[0].setKingVariant(2);
						// moveMapShowPoint(5, 0);
						// moveCursor(5, 0);
						// mapStepMax = 4;
						new ScriptCameraMoveOnKing(0)// ).addDependent(
				// moveMapAndCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// PaintableObject.currentRenderer.setCurrentDisplayable(auxMapNameMessage);
				// drawCursorFlag = false;
				// scriptState = 0;
				//
				// case 0:
				).addDependent(
						new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 1:
				).addDependent(
						new ScriptDialog("dialog.0", "campaign/portrait/1.png", "LEFT")
				// createDialogScreen(PaintableObject.getLocaleString(260), 1, 4);
				// ++scriptState;
				// break;
				// case 2:
				).addDependent(
						new ScriptShowTarget("name", "target")).addDependent(// ).addDependent(
								// new ScriptShowCursor(), // ).addDependent(
								new ScriptShowInfoBar(), // ).addDependent(
								new ScriptEnableActiveGame(), // ).addDependent(
								new ScriptSetUnitSpeed(8), // ).addDependent(
								new ScriptSetCameraSpeed(6)
				// startNormalPlay();
				// ++scriptState;
				// break;
				// case 3:
				).addDependent(
						// if(countUnits(-1, -1, 1) == 0 && countCastles(1) == 0)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionUnitNumber(1, 0, 0), new ConditionCastleNumber(1, 0, 0))).addDependent(
								// activeFlag = false;
								new ScriptHideCursor(), // ).addDependent(
								// drawCursorFlag = false;
								new ScriptDelay(1000)
				// setScriptDelay(15);
				// ++scriptState;
				// }
				// break;
				// case 4:
				).addDependent(
						new ScriptCameraMoveOnKing(0)
				// moveMapAndCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// break;
				// case 5:
				).addDependent(
						new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(261), 0, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 6:
				).addDependent(
				// completeMission();
				// case end
				).addDependent(
						new ScriptMissionComplete("missionComplete")).addDependent(
								new ScriptCloseMission())
						.getStartScript(),
				new ScriptGameOver().setConditions(new ConditionCastleNumber(0, 0, 0), new ConditionNamedUnitDead("king"))
		};
		save(endScripts, file);
	}
	
	public void mission6(String file) throws IOException
	{
		// new Script()).addDependent(
		Script[] endScripts =
		{
				new ScriptSetNamedUnit(0, 13, "king"),
				// new ScriptUnitCreate(-1, 13, "SOLDIER", 0),// ).addDependent(
				// new ScriptUnitCreate(-2, 13, "ARCHER", 0),// ).addDependent(
				// new ScriptUnitCreate(-3, 13, "SORCERESS", 0),// ).addDependent(
				// new ScriptUnitCreate(-4, 13, "CRYSTAL", 0),// ).addDependent(
				new ScriptSetNamedUnit(-4, 13, "crystall"),
				new ScriptBlackScreen(),
				new ScriptHideCursor(),
				new ScriptHideInfoBar(),
				new ScriptDisableActiveGame(),
				new ScriptSetUnitSpeed(12), // ).addDependent(
				new ScriptTitle("title").addDependent(
						new ScriptHideBlackScreen(), // ).addDependent(
						// allowedUnits = 8;
						// Unit.speed = 4;
						// money[0] = 400;
						// money[1] = 600;
						new ScriptUnitMoveExtended(0, 13, 1, 13, 1, 14, 3, 14), // ).addDependent(
						new ScriptUnitMoveExtended(-1, 13, 1, 13, 1, 14, 2, 14), // ).addDependent(
						new ScriptUnitMoveExtended(-2, 13, 1, 13, 1, 14), // ).addDependent(
						new ScriptUnitMoveExtended(-3, 13, 1, 13), // ).addDependent(
						new ScriptUnitMoveExtended(-4, 13, 0, 13)// ).addDependent(
				// new ScriptUnitMove(-1, 13, 0, 13)
				).addDependent(
				// var1 = Unit.createUnit(0, 0, 13, -1);
				// new ScriptUnitMoveExtended(0, 13, 1, 13, 1, 14, 2, 14),// ).addDependent(
				// new ScriptUnitMove(-1, 13, 0, 13)
				).addDependent(
				// new ScriptUnitMoveExtended(0, 13, 1, 13, 1, 14),// ).addDependent(
				// var2 = Unit.createUnit(1, 0, 13, -1);
				// new ScriptUnitMove(-1, 13, 0, 13)
				).addDependent(
				// new ScriptUnitMove(0, 13, 1, 13),
				// Unit var3 = Unit.createUnit(3, 0, 13, -1);
				// new ScriptUnitMove(-1, 13, 0, 13)// ).addDependent(
				// Unit var4 = Unit.createUnit(11, 0, 13, -1);
				// fractionKings[0].followerUnit = var1;
				// var1.followerUnit = var2;
				// var2.followerUnit = var3;
				// var3.followerUnit = var4;
				// fractionKings[0].fillMoveRangeData(mapAlphaData);
				// getUnit(13, 0, 0).plotRoute(14, 3, true);
				// moveMapShowPoint(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// moveCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// var_31be = fractionKings[0];
				// PaintableObject.currentRenderer.setCurrentDisplayable(auxMapNameMessage);
				// drawCursorFlag = false;
				// scriptState = 0;
				//
				// if(scriptState <= 10)
				// {
				// if(var_3c5c == null)
				// {
				// var_3c5c = listUnits(11, -1, 0)[0];
				// }
				//
				// if(var_3c5c.unitState == 3)
				// {
				// var_3c5c = null;
				// sub_131d();
				// return;
				// }
				// }
				//
				// switch(scriptState)
				// {
				// case 0:
				).addDependent(
				// if(fractionKings[0].unitState != 1)
				// {
				// var_31be = null;
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// }
				// break;
				// case 1:
				).addDependent(
						new ScriptDialog("dialog.0", "campaign/portrait/5.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(262), 5, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 2:
				).addDependent(
						new ScriptDialog("dialog.1", "campaign/portrait/0.png", "LEFT")// ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(263), 0, 4);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 3:
				).addDependent(
						// var14 = listUnits(-1, -1, 0);
						//
						// for(var2 = 0; var2 < var14.length; ++var2)
						// {
						// var14[var2].followerUnit = null;
						// }
						//
						new ScriptShowTarget("name", "target")).addDependent(// ).addDependent(
								// new ScriptShowCursor(), // ).addDependent(
								new ScriptShowInfoBar(), // ).addDependent(
								new ScriptEnableActiveGame(), // ).addDependent(
								new ScriptSetUnitSpeed(8), // ).addDependent(
								new ScriptSetCameraSpeed(6)
				// startNormalPlay();
				// ++scriptState;
				// break;
				// case 4:
				).addDependent(
						// if(currentTurn >= 2)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionTurn(2))).addDependent(
								new ScriptHideCursor(), // ).addDependent(
								new ScriptDelay(1000))
						.addDependent(
								// setScriptDelay(15);
								// activeFlag = false;
								// drawCursorFlag = false;
								new ScriptCameraMove(7, 11)
				// moveMapAndCursor(11, 7);
				// }
				// break;
				// case 5:
				).addDependent(
						new ScriptUnitCreate(8, 11, "DIRE_WOLF", 1), // ).addDependent(
						// (var11 = Unit.createUnit(5, 1, 11, 8)).fillMoveRangeData(mapAlphaData);
						// new ScriptUnitCreate(8, 11, "DIRE_WOLF", 1),// ).addDependent(
						new ScriptUnitMove(8, 11, 7, 14)// ).addDependent(
				// Unit.createUnit(5, 1, 11, 8).plotRoute(14, 7, true);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 6:
				).addDependent(
						new ScriptUnitCreate(8, 11, "SOLDIER", 1), // ).addDependent(
						// (var3 = Unit.createUnit(0, 1, 11, 8)).fillMoveRangeData(mapAlphaData);
						// new ScriptUnitCreate(8, 11, "SOLDIER", 1),// ).addDependent(
						new ScriptUnitMove(8, 11, 7, 13)// ).addDependent(
				// Unit.createUnit(0, 1, 11, 8).plotRoute(13, 7, true);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 7:
				).addDependent(
						new ScriptUnitCreate(8, 11, "SORCERESS", 1), // ).addDependent(
						// (var4 = Unit.createUnit(3, 1, 11, 8)).fillMoveRangeData(mapAlphaData);
						// new ScriptUnitCreate(8, 11, "SORCERESS", 1),// ).addDependent(
						new ScriptUnitMove(8, 11, 7, 12)// ).addDependent(
				// Unit.createUnit(3, 1, 11, 8).plotRoute(12, 7, true);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 8:
				).addDependent(
						new ScriptUnitCreate(8, 11, "ARCHER", 1), // ).addDependent(
						new ScriptUnitMove(8, 11, 8, 13)).addDependent(
								// Unit.createUnit(1, 1, 11, 8).plotRoute(13, 8, false);
								new ScriptDelay(666)
				// setScriptDelay(20);
				// ++scriptState;
				// break;
				// case 9:
				).addDependent(
						new ScriptDialog("dialog.2", "campaign/portrait/5.png", "LEFT")).addDependent(
								new ScriptCameraMoveOnKing(0))
						.addDependent(
								// new ScriptShowCursor(),// ).addDependent(
								new ScriptEnableActiveGame() // ).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(264), 5, 4);
				// activeFlag = true;
				// drawCursorFlag = true;
				// moveMapAndCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// break;
				// case 10:
				).addDependent(
						// boolean var6 = false;
						// Unit[] var7 = listUnits(-1, 2, 0);
						//
						// for(int var15 = 0; var15 < var7.length; ++var15)
						// {
						// if(var7[var15].currentMapPosX <= 9 || var7[var15].currentMapPosY >= 10)
						// {
						// var6 = true;
						// break;
						// }
						// }
						//
						// if(var6 || countUnits(-1, -1, 1) == 0)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionOr(
								new ConditionUnitIntoBounds(0, new Bounds(10, 0, h, 9)),
								new ConditionUnitNumber(1, 0, 0))))
						.addDependent(
								new ScriptHideCursor(), // ).addDependent(
								// drawCursorFlag = false;
								// activeFlag = false;
								new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// }
				// break;
				// case 11:
				).addDependent(
						new ScriptSetCoordinateNamedUnitI("crystallI", "crystall"),
						new ScriptSetCoordinateNamedUnitJ("crystallJ", "crystall"),
						new ScriptUnitCreate(new CoordinateName("crystallI"), new CoordinateInteger(w + 1), "DRAGON", 1),
						new ScriptCameraMove(new CoordinateName("crystallI"), new CoordinateInteger(w))
				// crystallEscortLeader = listUnits(11, -1, 0)[0];
				// crystallEscortCrystall = Unit.createUnit(8, 1, mapWidth, crystallEscortLeader.currentMapPosY);
				// moveMapAndCursor(mapWidth - 1, crystallEscortLeader.currentMapPosY);
				// new ScriptHideCursor()
				// drawCursorFlag = false;
				// break;
				// case 12:
				).addDependent(
						new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateInteger(w + 1), new CoordinateName("crystallI"), new CoordinateInteger(w))
				// crystallEscortCrystall.plotRoute(crystallEscortLeader.currentMapPosX, crystallEscortLeader.currentMapPosY, false);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 13:
				).addDependent(
						new ScriptDialog("dialog.3", "campaign/portrait/5.png", "LEFT")).addDependent(
								new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateInteger(w), new CoordinateName("crystallI"), new CoordinateName("crystallJ"))
				// createDialogScreen(PaintableObject.getLocaleString(265), 5, 4);
				// var_31be = crystallEscortCrystall;
				// ++scriptState;
				// break;
				// case 14:
				).addDependent(
						// if(crystallEscortCrystall.unitState != 1)
						// {
						new ScriptDialog("dialog.4", "campaign/portrait/0.png", "LEFT")).addDependent(
								// createDialogScreen(PaintableObject.getLocaleString(266), 0, 4);
								// crystallEscortCrystall.plotRoute(-1, crystallEscortCrystall.currentMapPosY, false);
								// new ScriptDelay(200)
								// setScriptDelay(3);
								// ++scriptState;
								new ScriptSetCameraSpeed(3),
								new ScriptCameraMove(0, 0),
								new ScriptSetCoordinateFromOther("crystallJ", "crystallJ-1", -1),
								new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateName("crystallJ"), new CoordinateName("crystallI"), new CoordinateName("crystallJ-1")))
						.addDependent(
								new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateName("crystallJ-1"), new CoordinateName("crystallI"), new CoordinateInteger(-2)),
								new ScriptUnitMove(new CoordinateName("crystallI"), new CoordinateName("crystallJ"), new CoordinateName("crystallI"), new CoordinateInteger(-1))
				// }
				// break;
				// case 15:
				).addDependent(
						new ScriptSetCameraSpeed(6),
						new ScriptRemoveUnit(new CoordinateName("crystallI"), new CoordinateInteger(-1)),
						new ScriptRemoveUnit(new CoordinateName("crystallI"), new CoordinateInteger(-2))
				// crystallEscortLeader.plotRoute(-1, crystallEscortCrystall.currentMapPosY, false);
				// ++scriptState;
				// break;
				// case 16:
				).addDependent(
				// if(crystallEscortCrystall.unitState != 1)
				// {
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// crystallEscortLeader.removeThisUnit();
				// crystallEscortCrystall.removeThisUnit();
				// moveCursor(0, crystallEscortCrystall.currentMapPosY);
				// crystallEscortLeader = null;
				// crystallEscortCrystall = null;
				// var_31be = null;
				// ++scriptState;
				// }
				// break;
				// case 17:
				).addDependent(
						new ScriptCameraMove(9, 1)
				// moveMapAndCursor(1, 9);
				// break;
				// case 18:
				).addDependent(
						// new ScriptUnitCreate(8, -2, "KING", 1),// ).addDependent(
						// fractionKings[1] = Unit.createUnit(9, 1, -2, 8);
						new ScriptUnitCreate(8, -2, "KING", 1), // ).addDependent(
						new ScriptUnitMove(8, -2, 8, 0), // ).addDependent(
						// Unit.createUnit(9, 1, -2, 8).plotRoute(0, 8, false);
						new ScriptUnitCreate(8, -1, "SOLDIER", 1), // ).addDependent(
						new ScriptUnitMove(8, -1, 8, 3), // ).addDependent(
						// Unit.createUnit(0, 1, -1, 8).plotRoute(3, 8, false);
						new ScriptUnitCreate(10, -1, "SOLDIER", 1), // ).addDependent(
						new ScriptUnitMove(10, -1, 10, 1), // ).addDependent(
						// Unit.createUnit(0, 1, -1, 10).plotRoute(1, 10, false);
						new ScriptUnitCreate(7, -3, "DRAGON", 1), // ).addDependent(
						new ScriptUnitMoveExtended(7, -3, 7, 4, 8, 4), // ).addDependent(
						// Unit.createUnit(8, 1, -3, 7).plotRoute(4, 8, false);
						new ScriptUnitCreate(11, -3, "DRAGON", 1), // ).addDependent(
						new ScriptUnitMoveExtended(11, -3, 11, 2, 10, 2), // ).addDependent(
						// Unit.createUnit(8, 1, -3, 11).plotRoute(2, 10, false);
						new ScriptUnitCreate(9, -2, "WISP", 1), // ).addDependent(
						new ScriptUnitMove(9, -2, 9, 2), // ).addDependent(
						// Unit.createUnit(4, 1, -2, 9).plotRoute(2, 9, false);
						new ScriptUnitCreate(9, -4, "GOLEM", 1), // ).addDependent(
						new ScriptUnitMove(9, -4, 9, 4), // ).addDependent(
						// Unit.createUnit(6, 1, -4, 9).plotRoute(4, 9, false);
						new ScriptUnitCreate(9, -6, "GOLEM", 1), // ).addDependent(
						new ScriptUnitMoveExtended(9, -6, 9, 5, 10, 5), // ).addDependent(
						// Unit.createUnit(6, 1, -6, 9).plotRoute(5, 10, false);
						// new ScriptDelay(3333)
						new ScriptDelay(3333)
				// setScriptDelay(50);
				// ++scriptState;
				// break;
				// case 19:
				).addDependent(
						new ScriptDialog("dialog.5", "campaign/portrait/3.png", "LEFT")
				// createDialogScreen(PaintableObject.getLocaleString(267), 3, 4);
				// ++scriptState;
				// break;
				// case 20:
				).addDependent(
						new ScriptCameraMove(14, 13)
				// moveMapAndCursor(13, 14);
				// break;
				// case 21:
				).addDependent(
						new ScriptUnitCreate(14, 13, "SOLDIER", 1), // ).addDependent(
						new ScriptUnitMove(14, 13, 14, 12)// ).addDependent(
				// Unit.createUnit(0, 1, 13, 14).plotRoute(12, 14, false);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 22:
				).addDependent(
						new ScriptUnitCreate(14, 13, "GOLEM", 1), // ).addDependent(
						new ScriptUnitMove(14, 13, 14, 14)// ).addDependent(
				// Unit.createUnit(6, 1, 13, 14).plotRoute(14, 14, false);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 23:
				).addDependent(
						new ScriptUnitCreate(14, 13, "ELEMENTAL", 1), // ).addDependent(
						new ScriptUnitMove(14, 13, 12, 13)// ).addDependent(
				// Unit.createUnit(2, 1, 13, 14).plotRoute(13, 12, false);
				// new ScriptDelay(333)
				// setScriptDelay(5);
				// ++scriptState;
				// break;
				// case 24:
				).addDependent(
						new ScriptUnitCreate(14, 13, "SORCERESS", 1), // ).addDependent(
						new ScriptUnitMove(14, 13, 15, 13)// ).addDependent(
				// Unit.createUnit(3, 1, 13, 14).plotRoute(13, 15, false);
				// new ScriptDelay(1000)
				// setScriptDelay(15);
				// ++scriptState;
				// break;
				// case 25:
				).addDependent(
						new ScriptDialog("dialog.6", "campaign/portrait/5.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(268), 5, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 26:
				).addDependent(
						new ScriptDialog("dialog.7", "campaign/portrait/0.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(269), 0, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 27:
				).addDependent(
						new ScriptCameraMove(17, 13)
				// moveMapAndCursor(13, 17);
				// break;
				// case 28:
				).addDependent(
						// Unit var8;
						// new ScriptUnitCreate(18, 13, "KING", 0),// ).addDependent(
						// (var8 = Unit.createUnit(9, 0, 13, 18)).setKingVariant(2);
						new ScriptUnitCreate(18, 13, "KING", 0), // ).addDependent(
						new ScriptUnitMove(18, 13, 16, 13), // ).addDependent(
						// Unit.createUnit(9, 0, 13, 18).plotRoute(13, 16, false);
						new ScriptUnitCreate(18, 12, "GOLEM", 0), // ).addDependent(
						new ScriptUnitMove(18, 12, 16, 12), // ).addDependent(
						// Unit.createUnit(6, 0, 12, 18).plotRoute(12, 16, false);
						new ScriptUnitCreate(19, 14, "DRAGON", 0), // ).addDependent(
						new ScriptUnitMove(19, 14, 16, 14), // ).addDependent(
						// Unit.createUnit(8, 0, 14, 19).plotRoute(14, 16, false);
						new ScriptUnitCreate(19, 13, "WISP", 0), // ).addDependent(
						new ScriptUnitMove(19, 13, 17, 13), // ).addDependent(
						// Unit.createUnit(4, 0, 13, 19).plotRoute(13, 17, false);
						new ScriptUnitCreate(19, 12, "ARCHER", 0), // ).addDependent(
						new ScriptUnitMove(19, 12, 17, 12), // ).addDependent(
						// Unit.createUnit(1, 0, 12, 19).plotRoute(12, 17, false);
						new ScriptDelay(1333)
				// setScriptDelay(20);
				// ++scriptState;
				// break;
				// case 29:
				).addDependent(
						new ScriptDialog("dialog.8", "campaign/portrait/1.png", "LEFT")).addDependent(
				// createDialogScreen(PaintableObject.getLocaleString(270), 1, 4);
				// new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;
				// break;
				// case 30:
				).addDependent(
						new ScriptCameraMoveOnKing(0)
				// moveMapAndCursor(fractionKings[0].currentMapPosX, fractionKings[0].currentMapPosY);
				// break;
				// case 31:
				).addDependent(
				// currentHelp = 18;
				// ++scriptState;
				// break;
				// case 32:
				).addDependent(
						new ScriptEnableActiveGame() // ).addDependent(
				// activeFlag = true;
				// new ScriptShowCursor()
				// drawCursorFlag = true;
				// auxObjectivesMessage = createMessageScreen(PaintableObject.getLocaleString(121 + currentMission), PaintableObject.getLocaleString(138), height_, -1);
				// auxObjectivesMessage.setDrawSoftButtonFlag(0, true);
				// auxObjectivesMessage.setNextDisplayable((PaintableObject)null);
				// PaintableObject.currentRenderer.setCurrentDisplayable(auxObjectivesMessage);
				// ++scriptState;
				// break;
				// case 33:
				).addDependent(
						// if(countUnits(-1, -1, 1) == 0 && countCastles(1) == 0)
						// {
						new ScriptDisableActiveGame().setConditions(new ConditionAnd(
								new ConditionUnitNumber(1, 0, 0),
								new ConditionCastleNumber(1, 0, 0))))
						.addDependent(
								// activeFlag = false;
								new ScriptHideCursor(), // ).addDependent(
								// drawCursorFlag = false;
								new ScriptDelay(666)
				// setScriptDelay(10);
				// ++scriptState;deb
				// }
				// break;deb
				// case 34:
				).addDependent(deb
				// completeMission();
				// case end
				).addDependent(
						new ScriptMissionComplete("missionComplete")).addDependent(
								new ScriptCloseMission())
						.getStartScript(),
				new ScriptGameOver().setConditions(
						new ConditionOr(
								new ConditionNamedUnitDead("crystall"),
								new ConditionAnd(
										new ConditionCastleNumber(0, 0, 0),
										new ConditionNamedUnitDead("king"))))
		};
		save(endScripts, file);
	}
	//*/

}
