package ru.ancientempires.draws;

import java.util.HashSet;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.widget.Toast;
import ru.ancientempires.GameThread;
import ru.ancientempires.IDrawCampaign;
import ru.ancientempires.Point;
import ru.ancientempires.activity.GameActivity;
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
import ru.ancientempires.campaign.scripts.ScriptUnitCreateAndMove;
import ru.ancientempires.campaign.scripts.ScriptUnitDie;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveExtended;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.campaign.DialogGameOver;
import ru.ancientempires.draws.campaign.DialogShowIntro;
import ru.ancientempires.draws.campaign.DialogShowTarget;
import ru.ancientempires.draws.campaign.DrawCameraMove;
import ru.ancientempires.draws.campaign.DrawUnitAttack;
import ru.ancientempires.draws.campaign.DrawUnitDie;
import ru.ancientempires.draws.campaign.MyDialog;
import ru.ancientempires.draws.campaign.MyDialogWithoutImage;
import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawOnFrames;
import ru.ancientempires.draws.onframes.DrawUnitMove;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;

public class DrawCampaign extends Draw implements IDrawCampaign
{
	
	private class SimpleDraw
	{
		public DrawOnFrames	draw;
		public Script		script;
		
		public SimpleDraw(DrawOnFrames draw, Script script)
		{
			this.draw = draw;
			this.script = script;
		}
	}
	
	public HashSet<SimpleDraw> draws = new HashSet<>();
	
	// public ArrayList<DrawOnFrames> draws = new ArrayList<DrawOnFrames>();
	// public ArrayList<Script> scripts = new ArrayList<Script>();
	// private Script blackScreenScript;
	
	public DrawCampaign()
	{
		game.campaign.iDrawCampaign = this;
	}
	
	private void add(DrawOnFrames draw, Script script)
	{
		main.add(draw);
		draws.add(new SimpleDraw(draw, script));
	}
	
	private void addWithoutMain(DrawOnFrames draw, Script script)
	{
		draws.add(new SimpleDraw(draw, script));
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (Iterator iterator = draws.iterator(); iterator.hasNext();)
		{
			SimpleDraw simple = (SimpleDraw) iterator.next();
			if (simple.draw.isEndDrawing)
			{
				simple.script.finish();
				iterator.remove();
			}
		}
	}
	
	@Override
	public void showIntro(Bitmap bitmap, String text, ScriptIntro script)
	{
		new DialogShowIntro().showDialog(bitmap, text, script);
	}
	
	@Override
	public void showTitle(final String text, final Script script)
	{
		GameActivity.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(GameActivity.activity, text, Toast.LENGTH_SHORT)
						.show();
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						script.finish();
					}
				}, 2000);
			}
		});
	}
	
	@Override
	public void showDialog(Bitmap bitmap, String text, ScriptDialog script)
	{
		new MyDialog().showDialog(bitmap, text, script);
	}
	
	@Override
	public void showDialog(String text, ScriptDialogWithoutImage script)
	{
		new MyDialogWithoutImage().showDialog(text, script);
	}
	
	@Override
	public void showTarget(String textTitle, String textTarget, ScriptShowTarget script)
	{
		new DialogShowTarget().showDialog(textTitle, textTarget, script);
	}
	
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
	
	//
	@Override
	public void setCameraSpeed(int delta, ScriptSetCameraSpeed script)
	{
		DrawCameraMove.delta = delta;
	}
	
	@Override
	public void cameraMove(int iEnd, int jEnd, Script script)
	{
		main.inputPlayer.tapWithoutAction(iEnd, jEnd);
		DrawCameraMove draw = new DrawCameraMove();
		draw.start(iEnd, jEnd);
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
	
	//
	@Override
	public void unitMove(int iStart, int jStart, int iEnd, int jEnd, Script script)
	{
		Point[] points = new Point[Math.abs(iEnd - iStart) + Math.abs(jEnd - jStart) + 1];
		for (int i = iStart; i != iEnd; i += Math.signum(iEnd - iStart))
			points[Math.abs(i - iStart)] = new Point(i, jStart);
		for (int j = jStart; j != jEnd; j += Math.signum(jEnd - jStart))
			points[Math.abs(iEnd - iStart) + Math.abs(j - jStart)] = new Point(iEnd, j);
		points[points.length - 1] = new Point(iEnd, jEnd);
		
		DrawUnitMove draw = new DrawUnitMove();
		draw.start(points, null, true);
		add(draw, script);
		
		script.performAction();
	}
	
	@Override
	public void unitMove(Point[] keyPoints, ScriptUnitMoveExtended script)
	{
		Point[] points = getPoints(keyPoints);
		DrawUnitMove draw = new DrawUnitMove();
		draw.start(points, null, true);
		add(draw, script);
		script.performAction();
	}
	
	private Point[] getPoints(Point[] keyPoints)
	{
		int length = 1;
		for (int i = 1; i < keyPoints.length; i++)
			length += Math.abs(keyPoints[i].i - keyPoints[i - 1].i)
					+ Math.abs(keyPoints[i].j - keyPoints[i - 1].j);
					
		Point[] points = new Point[length];
		int nPoint = 0;
		for (int iKeyPoint = 1; iKeyPoint < keyPoints.length; iKeyPoint++)
		{
			int iStart = keyPoints[iKeyPoint - 1].i;
			int jStart = keyPoints[iKeyPoint - 1].j;
			int iEnd = keyPoints[iKeyPoint].i;
			int jEnd = keyPoints[iKeyPoint].j;
			for (int i = iStart; i != iEnd; i += Math.signum(iEnd - iStart))
				points[nPoint + Math.abs(i - iStart)] = new Point(i, jStart);
			for (int j = jStart; j != jEnd; j += Math.signum(jEnd - jStart))
				points[nPoint + Math.abs(iEnd - iStart) + Math.abs(j - jStart)] = new Point(iEnd, j);
			nPoint += Math.abs(iEnd - iStart) + Math.abs(jEnd - jStart);
		}
		points[points.length - 1] = keyPoints[keyPoints.length - 1];
		return points;
	}
	
	@Override
	public void unitCreateAndMove(ScriptUnitCreateAndMove script)
	{
		script.performAction();
		DrawUnitMove draw = new DrawUnitMove();
		draw.start(getPoints(script.keyPoints), null, false);
		draw.unitBitmap.hanlers = script.getHandlers();
		add(draw, script);
	}
	
	@Override
	public void unitAttack(int i, int j, ScriptUnitAttack script)
	{
		DrawUnitAttack draw = new DrawUnitAttack();
		draw.start(i, j);
		add(draw, script);
	}
	
	@Override
	public void unitDie(int i, int j, ScriptUnitDie script)
	{
		script.performAction();
		DrawUnitDie draw = new DrawUnitDie();
		draw.start(i, j);
		add(draw, script);
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
		
		main.cells.updateOneCell(i, j);
		main.cellsDual.updateOneCell(i, j);
		DrawOnFrames draw = new DrawBitmaps()
				.setYX(i * A, j * A)
				.setBitmaps(SparksImages().bitmapsDefault).setFramesForBitmap(4)
				.animateRepeat(1);
		add(draw, script);
	}
	
	//
	@Override
	public void enableActiveGame(ScriptEnableActiveGame script)
	{
		main.isActiveGame = true;
		main.isDrawCursor = false;
		DrawUnitMove.framesForCell = 8;
		DrawCameraMove.delta = 6;
		if (main.infoY != 0)
			main.infoMove.startShow();
		GameActivity.activity.invalidateOptionsMenu();
		script.performAction();
	}
	
	@Override
	public void disableActiveGame(ScriptDisableActiveGame script)
	{
		main.isActiveGame = false;
		GameActivity.activity.invalidateOptionsMenu();
	}
	
	@Override
	public void hideInfoImmediately(Script script)
	{
		main.infoY = main.info.h;
	}
	
	@Override
	public void gameOver(ScriptGameOver script)
	{
		new DialogGameOver().createDialog();
	}
	
	@Override
	public void closeMission() throws Exception
	{
		Client.client.stopGame(true);
	}
	
	@Override
	public void updateCampaign()
	{
		GameThread.thread.needUpdateCampaign = true;
	}
	
}
