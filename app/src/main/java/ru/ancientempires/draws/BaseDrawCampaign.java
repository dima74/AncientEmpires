package ru.ancientempires.draws;

import android.graphics.Canvas;

import java.util.HashSet;
import java.util.Iterator;

import ru.ancientempires.Point;
import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.campaign.IDrawCampaign;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.campaign.scripts.AbstractScriptOnePoint;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptCellAttackPartTwo;
import ru.ancientempires.campaign.scripts.ScriptDelay;
import ru.ancientempires.campaign.scripts.ScriptEnableActiveGame;
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
import ru.ancientempires.campaign.scripts.ScriptSparkAttack;
import ru.ancientempires.campaign.scripts.ScriptSparkDefault;
import ru.ancientempires.campaign.scripts.ScriptUnitAttack;
import ru.ancientempires.campaign.scripts.ScriptUnitChangePosition;
import ru.ancientempires.campaign.scripts.ScriptUnitCreate;
import ru.ancientempires.campaign.scripts.ScriptUnitDie;
import ru.ancientempires.campaign.scripts.ScriptUnitMove;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveHandler;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.campaign.DrawCameraMove;
import ru.ancientempires.draws.campaign.DrawUnitAttack;
import ru.ancientempires.draws.campaign.DrawUnitDie;
import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawCitadelAttack;
import ru.ancientempires.draws.onframes.DrawOnFrames;
import ru.ancientempires.draws.onframes.DrawUnitMove;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;

public abstract class BaseDrawCampaign extends Draw implements IDrawCampaign
{
	
	private class SimpleDraw
	{
		public DrawOnFrames draw;
		public Script       script;

		public SimpleDraw(DrawOnFrames draw, Script script)
		{
			this.draw = draw;
			this.script = script;
		}
	}
	
	public HashSet<SimpleDraw> draws = new HashSet<>();
	
	public BaseDrawCampaign()
	{
		game.campaign.iDrawCampaign = this;
	}
	
	public void add(DrawOnFrames draw, Script script)
	{
		main.add(draw);
		draws.add(new SimpleDraw(draw, script));
	}
	
	public void addWithoutMain(DrawOnFrames draw, Script script)
	{
		draws.add(new SimpleDraw(draw, script));
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (Iterator<SimpleDraw> iterator = draws.iterator(); iterator.hasNext(); )
		{
			SimpleDraw simple = iterator.next();
			if (simple.draw.isEndDrawing)
			{
				simple.script.finish();
				iterator.remove();
			}
		}
	}
	
	//
	@Override
	public void delay(final int milliseconds, final ScriptDelay script)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(milliseconds);
				}
				catch (InterruptedException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
				script.finish();
			}
		}).start();
	}
	
	//
	@Override
	public void showBlackScreen(ScriptShowBlackScreen script)
	{
		main.blackScreen.startShow();
		addWithoutMain(main.blackScreen, script);
	}
	
	@Override
	public void hideBlackScreen(ScriptHideBlackScreen script)
	{
		main.blackScreen.startHide();
		addWithoutMain(main.blackScreen, script);
	}
	
	@Override
	public void blackScreen(ScriptBlackScreen script)
	{
		main.isBlackScreen = true;
	}
	
	//
	@Override
	public void hideCursor(ScriptHideCursor script)
	{
		main.isDrawCursor = false;
	}
	
	@Override
	public void showCursor(ScriptShowCursor script)
	{
		main.isDrawCursor = true;
	}
	
	@Override
	public void setCursorPosition(int i, int j, ScriptSetCursorPosition script)
	{
		main.inputPlayer.tapWithoutAction(i, j);
	}
	
	//
	@Override
	public void setCameraSpeed(int delta, ScriptSetCameraSpeed script)
	{
		DrawCameraMove.delta = delta;
	}
	
	@Override
	public void cameraMove(AbstractScriptOnePoint script)
	{
		int targetI = script.i();
		int targetJ = script.j();
		main.inputPlayer.tapWithoutAction(targetI, targetJ);
		DrawCameraMove draw = new DrawCameraMove();
		draw.start(targetI, targetJ);
		add(draw, script);
	}
	
	@Override
	public void setMapPosition(int i, int j, ScriptSetMapPosition script)
	{
		main.focusOnCell(i, j);
	}
	
	//
	@Override
	public void setUnitSpeed(int framesForCell, ScriptSetUnitSpeed script)
	{
		DrawUnitMove.framesForCell = framesForCell;
	}
	
	@Override
	public void unitMove(ScriptUnitMove script, boolean initFromStart)
	{
		if (!initFromStart)
			script.performAction();
		DrawUnitMove draw = new DrawUnitMove().setMakeSmoke(script.makeSmoke).start(getPoints(script.points), null, initFromStart);
		if (script.handlers != null)
		{
			draw.unitBitmap.handlers = new ScriptUnitMoveHandler[script.handlers.length];
			for (int i = 0; i < script.handlers.length; i++)
				draw.unitBitmap.handlers[i] = ((ScriptUnitMoveHandler) script.handlers[i]);
		}
		add(draw, script);
		if (initFromStart)
			script.performAction();
	}
	
	public Point[] getPoints(AbstractPoint[] keyPoints)
	{
		int length = 1;
		for (int i = 1; i < keyPoints.length; i++)
			length += Math.abs(keyPoints[i].getI() - keyPoints[i - 1].getI())
			          + Math.abs(keyPoints[i].getJ() - keyPoints[i - 1].getJ());

		Point[] points = new Point[length];
		int nPoint = 0;
		for (int iKeyPoint = 1; iKeyPoint < keyPoints.length; iKeyPoint++)
		{
			int iStart = keyPoints[iKeyPoint - 1].getI();
			int jStart = keyPoints[iKeyPoint - 1].getJ();
			int iEnd = keyPoints[iKeyPoint].getI();
			int jEnd = keyPoints[iKeyPoint].getJ();
			for (int i = iStart; i != iEnd; i += Math.signum(iEnd - iStart))
				points[nPoint + Math.abs(i - iStart)] = new Point(i, jStart);
			for (int j = jStart; j != jEnd; j += Math.signum(jEnd - jStart))
				points[nPoint + Math.abs(iEnd - iStart) + Math.abs(j - jStart)] = new Point(iEnd, j);
			nPoint += Math.abs(iEnd - iStart) + Math.abs(jEnd - jStart);
		}
		points[points.length - 1] = new Point(keyPoints[keyPoints.length - 1]);
		return points;
	}
	
	//
	
	@Override
	public void unitAttack(int i, int j, ScriptUnitAttack script)
	{
		vibrate();
		add(new DrawUnitAttack(i, j), script);
	}
	
	@Override
	public void unitDie(int i, int j, ScriptUnitDie script)
	{
		script.performAction();
		add(new DrawUnitDie(i, j), script);
	}
	
	@Override
	public void unitCreate(int i, int j, UnitType unitType, Player player, ScriptUnitCreate script)
	{
		script.performAction();
		if (game.checkCoordinates(i, j))
			main.units.updateUnit(i, j);
	}
	
	@Override
	public void removeUnit(int i, int j, ScriptRemoveUnit script)
	{
		script.performAction();
		if (game.checkCoordinates(i, j))
			main.units.updateUnit(i, j);
	}
	
	@Override
	public void unitChangePosition(int i, int j, int iNew, int jNew, ScriptUnitChangePosition script)
	{
		script.performAction();
	}

	@Override
	public void citadelAttack(ScriptOnePoint script)
	{
		add(new DrawCitadelAttack(script.i(), script.j()), script);
	}

	//
	@Override
	public void sparksDefault(int i, int j, ScriptSparkDefault script)
	{
		DrawBitmaps draw = new DrawBitmaps()
				.setBitmaps(SparksImages().bitmapsDefault)
				.setYX(i * A, j * A).animateRepeat(1);
		add(draw, script);
	}
	
	@Override
	public void sparksAttack(int i, int j, ScriptSparkAttack script)
	{
		DrawBitmaps draw = new DrawBitmaps()
				.setBitmaps(SparksImages().bitmapsAttack)
				.setYX(i * A, j * A).animateRepeat(1);
		add(draw, script);
	}
	
	@Override
	public void cellAttackPartTwo(int i, int j, ScriptCellAttackPartTwo script)
	{
		script.performAction();
		main.buildingSmokes.update();
		
		add(new DrawCellAttackPartTwo(i, j), script);
	}
	
	@Override
	public void hideInfoImmediately(Script script)
	{
		main.infoY = main.info.h;
	}
	
	@Override
	public void enableActiveGame(ScriptEnableActiveGame script)
	{
		main.isActiveGame = true;
		main.isDrawCursor = false;
		DrawUnitMove.framesForCell = 8;
		DrawCameraMove.delta = 6;
		if (main.infoY != 0)
			main.infoMove.startShow();
		main.saveScreenCenter();
		script.performAction();
	}
	
	@Override
	public void updateCampaign()
	{
		postUpdateCampaign();
	}
	
	@Override
	public void closeMission() throws Exception
	{
		Client.client.stopGame();
		if (game.path.nextGameID != null)
			GameActivity.startGame(game.path.nextGameID, null);
	}

}
