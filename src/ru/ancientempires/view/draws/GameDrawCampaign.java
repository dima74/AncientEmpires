package ru.ancientempires.view.draws;

import java.util.ArrayList;

import ru.ancientempires.IDrawCampaign;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptDelay;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.campaign.scripts.ScriptDisableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptEnableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptGameOver;
import ru.ancientempires.campaign.scripts.ScriptHideBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptHideCursor;
import ru.ancientempires.campaign.scripts.ScriptHideInfoBar;
import ru.ancientempires.campaign.scripts.ScriptIntro;
import ru.ancientempires.campaign.scripts.ScriptSetCameraSpeed;
import ru.ancientempires.campaign.scripts.ScriptSetUnitSpeed;
import ru.ancientempires.campaign.scripts.ScriptShowBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptShowCursor;
import ru.ancientempires.campaign.scripts.ScriptShowInfoBar;
import ru.ancientempires.campaign.scripts.ScriptShowTarget;
import ru.ancientempires.campaign.scripts.ScriptUnitAttack;
import ru.ancientempires.campaign.scripts.ScriptUnitCreate;
import ru.ancientempires.campaign.scripts.ScriptUnitDie;
import ru.ancientempires.campaign.scripts.ScriptUnitMove;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveAbout;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.Point;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.view.algortihms.InputAlgorithmUnitMove;
import ru.ancientempires.view.draws.campaign.DialogGameOver;
import ru.ancientempires.view.draws.campaign.DialogShowIntro;
import ru.ancientempires.view.draws.campaign.DialogShowTarget;
import ru.ancientempires.view.draws.campaign.GameDrawCameraMove;
import ru.ancientempires.view.draws.campaign.GameDrawUnitAttack;
import ru.ancientempires.view.draws.campaign.GameDrawUnitDie;
import ru.ancientempires.view.draws.campaign.MyDialogFragment;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMove;
import android.app.DialogFragment;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.os.Handler;
import android.widget.Toast;

public class GameDrawCampaign extends GameDrawOnFramesGroup implements IDrawCampaign
{
	
	public ArrayList<Script>	scripts	= new ArrayList<Script>();
	private Script				blackScreenScript;
	
	public GameDrawCampaign(GameDrawMain gameDraw)
	{
		super(gameDraw);
		Campaign.iDrawCampaign = this;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.setDrawFilter(new DrawFilter());
		int j = 0;
		for (int i = 0; i < this.draws.size(); i++)
		{
			GameDrawOnFrames gameDraw = this.draws.get(i);
			Script script = this.scripts.get(i);
			gameDraw.draw(canvas);
			if (gameDraw.isEndDrawing)
				Campaign.finish(script);
			else
			{
				this.draws.set(j, gameDraw);
				this.scripts.set(j, script);
				j++;
			}
		}
		
		while (this.draws.size() > j)
		{
			this.draws.remove(this.draws.size() - 1);
			this.scripts.remove(this.scripts.size() - 1);
		}
		
		if (this.blackScreenScript != null && this.gameDraw.gameDrawBlackScreen.isEndDrawing)
		{
			Campaign.finish(this.blackScreenScript);
			this.blackScreenScript = null;
		}
	}
	
	@Override
	public void updateCampaign()
	{
		GameActivity.gameView.thread.needUpdateCampaign = true;
	}
	
	@Override
	public void showDialog(String imagePath, String text, ScriptDialog script)
	{
		DialogFragment dialogFragment = new MyDialogFragment(imagePath, text, script);
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "MyDialogFragment");
	}
	
	@Override
	public void showTitle(final String text, final Script script)
	{
		this.gameDraw.gameActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(GameDrawCampaign.this.gameDraw.gameActivity, text, Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						Campaign.finish(script);
					}
				}, 2000);
			}
		});
	}
	
	@Override
	public void showTarget(String textTitle, String textTarget, ScriptShowTarget script)
	{
		DialogFragment dialogFragment = new DialogShowTarget(textTitle, textTarget, script);
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "DialogShowTarget");
	}
	
	@Override
	public void showIntro(String imagePath, String text, ScriptIntro script)
	{
		DialogFragment dialogFragment = new DialogShowIntro(imagePath, text, script);
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "DialogShowIntro");
	}
	
	@Override
	public void showBlackScreen(ScriptShowBlackScreen script)
	{
		this.gameDraw.gameDrawBlackScreen.start(0, 255);
		this.blackScreenScript = script;
	}
	
	@Override
	public void hideBlackScreen(ScriptHideBlackScreen script)
	{
		this.gameDraw.gameDrawBlackScreen.start(255, 0);
		this.blackScreenScript = script;
	}
	
	@Override
	public void blackScreen(ScriptBlackScreen script)
	{
		this.gameDraw.gameDrawBlackScreen.blackScreen();
		this.blackScreenScript = script;
	}
	
	@Override
	public void showInfoBar(ScriptShowInfoBar script)
	{
		this.gameDraw.gameDrawInfo.isActive = true;
		Campaign.finish(script);
	}
	
	@Override
	public void hideInfoBar(ScriptHideInfoBar script)
	{
		this.gameDraw.gameDrawInfo.isActive = false;
		Campaign.finish(script);
	}
	
	@Override
	public void enableActiveGame(ScriptEnableActiveGame script)
	{
		this.gameDraw.isActiveGame = true;
		Campaign.finish(script);
	}
	
	@Override
	public void disableActiveGame(ScriptDisableActiveGame script)
	{
		this.gameDraw.isActiveGame = false;
		Campaign.finish(script);
	}
	
	@Override
	public void unitMove(int iStart, int jStart, int iEnd, int jEnd, ScriptUnitMove script)
	{
		Action actionGet = new Action(ActionType.GET_UNIT_WAY);
		actionGet.setProperty("i", iStart);
		actionGet.setProperty("j", jStart);
		ActionResult resultGet = Client.action(actionGet);
		
		int[][] fieldPrevI = (int[][]) resultGet.getProperty("prevI");
		int[][] fieldPrevJ = (int[][]) resultGet.getProperty("prevJ");
		Point[] ways = InputAlgorithmUnitMove.getWayLine(iStart, jStart, iEnd, jEnd, fieldPrevI, fieldPrevJ);
		
		// TODO
		Unit unit = this.gameDraw.game.fieldUnits[iStart][jStart];
		this.gameDraw.game.fieldUnits[iEnd][jEnd] = unit;
		this.gameDraw.game.fieldUnits[iStart][jStart] = null;
		unit.i = iEnd;
		unit.j = jEnd;
		
		GameDrawUnitMove gameDraw = new GameDrawUnitMove(this.gameDraw);
		gameDraw.init(iStart, jStart);
		gameDraw.start(ways, null);
		this.draws.add(gameDraw);
		this.scripts.add(script);
	}
	
	@Override
	public void cameraMove(int iEnd, int jEnd, Script script)
	{
		this.gameDraw.inputAlgorithmMain.tapWithoutAction(iEnd, jEnd);
		GameDrawCameraMove gameDraw = new GameDrawCameraMove(this.gameDraw);
		gameDraw.start(iEnd, jEnd);
		this.draws.add(gameDraw);
		this.scripts.add(script);
	}
	
	@Override
	public void hideCursor(ScriptHideCursor script)
	{
		this.gameDraw.isDrawCursor = false;
		Campaign.finish(script);
	}
	
	@Override
	public void showCursor(ScriptShowCursor script)
	{
		this.gameDraw.isDrawCursor = true;
		Campaign.finish(script);
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
				Campaign.finish(script);
			}
		}).start();
	}
	
	@Override
	public void unitAttack(int i, int j, ScriptUnitAttack script)
	{
		GameDrawUnitAttack gameDraw = new GameDrawUnitAttack(this.gameDraw);
		gameDraw.start(i, j);
		this.draws.add(gameDraw);
		this.scripts.add(script);
	}
	
	@Override
	public void unitDie(int i, int j, ScriptUnitDie script)
	{
		GameDrawUnitDie gameDraw = new GameDrawUnitDie(this.gameDraw);
		gameDraw.start(i, j);
		this.draws.add(gameDraw);
		this.scripts.add(script);
	}
	
	@Override
	public void unitCreate(int i, int j, UnitType unitType, Player player, ScriptUnitCreate script)
	{
		Unit unit = Unit.getUnit(Unit.defaultUnit, unitType, player);
		GameHandler.setUnit(i, j, unit);
		player.units.add(unit);
		this.gameDraw.gameDrawUnit.updateOneUnit(i, j);
		Campaign.finish(script);
	}
	
	@Override
	public void unitMoveAbout(int iStart, int jStart, int iEnd, int jEnd, ScriptUnitMoveAbout script)
	{
		Point[] ways = new Point[Math.abs(iEnd - iStart) + Math.abs(jEnd - jStart) + 1];
		for (int i = iStart; i != iEnd; i += Math.signum(iEnd - iStart))
			ways[i - iStart] = new Point(i, jStart);
		for (int j = jStart; j != jEnd; j += Math.signum(jEnd - jStart))
			ways[iEnd - iStart + j - jStart] = new Point(iEnd, j);
		ways[ways.length - 1] = new Point(iEnd, jEnd);
		
		// TODO
		Unit unit = GameHandler.getUnit(iStart, jStart);
		GameHandler.setUnit(iEnd, jEnd, unit);
		GameHandler.removeUnit(iStart, jStart);
		
		GameDrawUnitMove gameDraw = new GameDrawUnitMove(this.gameDraw);
		gameDraw.init(iStart, jStart);
		gameDraw.start(ways, null);
		this.draws.add(gameDraw);
		this.scripts.add(script);
	}
	
	@Override
	public void setUnitSpeed(int framesForCell, ScriptSetUnitSpeed script)
	{
		GameDrawUnitMove.framesForCell = framesForCell;
		Campaign.finish(script);
	}
	
	@Override
	public void setCameraSpeed(int delta, ScriptSetCameraSpeed script)
	{
		GameDrawCameraMove.delta = delta * GameDraw.a;
		Campaign.finish(script);
	}
	
	@Override
	public void closeMission()
	{
		Client.getClient().startGame(Campaign.game.nextMission);
		GameActivity.gameActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				GameActivity.gameActivity.startGameView();
			}
		});
	}
	
	@Override
	public void gameOver(ScriptGameOver script)
	{
		DialogFragment dialogFragment = new DialogGameOver();
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "DialogGameOver");
	}
	
}
