package ru.ancientempires.view.draws;

import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.widget.Toast;
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
import ru.ancientempires.campaign.scripts.ScriptUnitDie;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveExtended;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.view.draws.campaign.DialogGameOver;
import ru.ancientempires.view.draws.campaign.DialogShowIntro;
import ru.ancientempires.view.draws.campaign.DialogShowTarget;
import ru.ancientempires.view.draws.campaign.GameDrawCameraMove;
import ru.ancientempires.view.draws.campaign.GameDrawUnitAttack;
import ru.ancientempires.view.draws.campaign.GameDrawUnitDie;
import ru.ancientempires.view.draws.campaign.MyDialog;
import ru.ancientempires.view.draws.campaign.MyDialogWithoutImage;
import ru.ancientempires.view.draws.onframes.GameDrawBitmaps;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMove;

public class GameDrawCampaign extends GameDraw implements IDrawCampaign
{
	
	public ArrayList<GameDrawOnFrames>	draws	= new ArrayList<GameDrawOnFrames>();
	public ArrayList<Script>			scripts	= new ArrayList<Script>();
	private Script						blackScreenScript;
	
	public GameDrawCampaign()
	{
		Client.getGame().campaign.iDrawCampaign = this;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		int j = 0;
		for (int i = 0; i < draws.size(); i++)
		{
			GameDrawOnFrames gameDraw = draws.get(i);
			Script script = scripts.get(i);
			gameDraw.draw(canvas);
			if (gameDraw.isEndDrawing)
				script.finish();
			else
			{
				draws.set(j, gameDraw);
				scripts.set(j, script);
				j++;
			}
		}
		
		while (draws.size() > j)
		{
			draws.remove(draws.size() - 1);
			scripts.remove(scripts.size() - 1);
		}
		
		if (blackScreenScript != null
				&& main.gameDrawBlackScreen.isEndDrawing)
		{
			blackScreenScript.finish();
			blackScreenScript = null;
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
		main.gameDrawBlackScreen.startShow();
		blackScreenScript = script;
	}
	
	@Override
	public void hideBlackScreen(ScriptHideBlackScreen script)
	{
		main.gameDrawBlackScreen.startHide();
		blackScreenScript = script;
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
		GameDrawCameraMove.delta = delta;
	}
	
	@Override
	public void cameraMove(int iEnd, int jEnd, Script script)
	{
		main.inputPlayer.tapWithoutAction(iEnd, jEnd);
		GameDrawCameraMove gameDraw = new GameDrawCameraMove();
		gameDraw.start(iEnd, jEnd);
		draws.add(gameDraw);
		scripts.add(script);
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
		GameDrawUnitMove.framesForCell = framesForCell;
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
		
		GameDrawUnitMove gameDraw = new GameDrawUnitMove();
		gameDraw.init(iStart, jStart);
		gameDraw.start(points, null);
		draws.add(gameDraw);
		scripts.add(script);
		
		script.performAction();
	}
	
	@Override
	public void unitMove(Point[] keyPoints, ScriptUnitMoveExtended script)
	{
		Point start = keyPoints[0];
		Point end = keyPoints[keyPoints.length - 1];
		
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
		points[points.length - 1] = new Point(end.i, end.j);
		
		GameDrawUnitMove gameDraw = new GameDrawUnitMove();
		gameDraw.init(start.i, start.j);
		gameDraw.start(points, null);
		draws.add(gameDraw);
		scripts.add(script);
		
		script.performAction();
	}
	
	@Override
	public void unitAttack(int i, int j, ScriptUnitAttack script)
	{
		GameDrawUnitAttack gameDraw = new GameDrawUnitAttack();
		gameDraw.start(i, j);
		draws.add(gameDraw);
		scripts.add(script);
	}
	
	@Override
	public void unitDie(int i, int j, ScriptUnitDie script)
	{
		script.performAction();
		GameDrawUnitDie gameDraw = new GameDrawUnitDie();
		gameDraw.start(i, j);
		draws.add(gameDraw);
		scripts.add(script);
	}
	
	@Override
	public void unitCreate(int i, int j, UnitType unitType, Player player, ScriptUnitCreate script)
	{
		script.performAction();
		if (game.checkCoordinates(i, j))
			main.gameDrawUnits.updateUnit(i, j);
	}
	
	@Override
	public void removeUnit(int i, int j, ScriptRemoveUnit script)
	{
		script.performAction();
		if (game.checkCoordinates(i, j))
			main.gameDrawUnits.updateUnit(i, j);
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
		GameDrawBitmaps gameDraw = new GameDrawBitmaps()
				.setBitmaps(SparksImages().bitmapsDefault)
				.setYX(i * A, j * A).animateRepeat(1);
		draws.add(gameDraw);
		scripts.add(script);
	}
	
	@Override
	public void sparksAttack(int i, int j, ScriptSparkAttack script)
	{
		GameDrawBitmaps gameDraw = new GameDrawBitmaps()
				.setBitmaps(SparksImages().bitmapsAttack)
				.setYX(i * A, j * A).animateRepeat(1);
		draws.add(gameDraw);
		scripts.add(script);
	}
	
	@Override
	public void cellAttackPartTwo(int i, int j, ScriptCellAttackPartTwo script)
	{
		// TODO
		Cell targetCell = game.fieldCells[i][j];
		if (targetCell.player != null)
			game.currentEarns[targetCell.player.ordinal] -= targetCell.type.earn;
		targetCell.isDestroying = true;
		targetCell.isCapture = false;
		targetCell.player = null;
		
		main.gameDrawBuildingSmokes.update();
		
		main.gameDrawCells.updateOneCell(i, j);
		main.gameDrawCellDual.updateOneCell(i, j);
		GameDrawOnFrames gameDraw = new GameDrawBitmaps()
				.setYX(i * A, j * A)
				.setBitmaps(SparksImages().bitmapsDefault).setFramesForBitmap(4)
				.animateRepeat(1);
		draws.add(gameDraw);
		scripts.add(script);
	}
	
	//
	@Override
	public void enableActiveGame(ScriptEnableActiveGame script)
	{
		main.isActiveGame = true;
		main.isDrawCursor = false;
		GameDrawUnitMove.framesForCell = 8;
		GameDrawCameraMove.delta = 6;
		main.gameDrawInfoMove.startShow();
		GameActivity.activity.invalidateOptionsMenu();
		
		try
		{
			Client.getGame().saver.saveSnapshot();
		}
		catch (IOException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
	@Override
	public void disableActiveGame(ScriptDisableActiveGame script)
	{
		main.isActiveGame = false;
		main.isDrawCursor = false;
		GameActivity.activity.invalidateOptionsMenu();
	}
	
	@Override
	public void gameOver(ScriptGameOver script)
	{
		new DialogGameOver().createDialog();
	}
	
	@Override
	public void closeMission() throws Exception
	{
		Client.client.stopGame();
	}
	
	@Override
	public void updateCampaign()
	{
		GameActivity.activity.view.thread.needUpdateCampaign = true;
	}
	
}
