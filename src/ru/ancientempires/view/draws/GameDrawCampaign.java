package ru.ancientempires.view.draws;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;
import ru.ancientempires.IDrawCampaign;
import ru.ancientempires.Point;
import ru.ancientempires.R;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.campaign.Campaign;
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
import ru.ancientempires.campaign.scripts.ScriptHideInfoBar;
import ru.ancientempires.campaign.scripts.ScriptIntro;
import ru.ancientempires.campaign.scripts.ScriptRemoveUnit;
import ru.ancientempires.campaign.scripts.ScriptSetCameraSpeed;
import ru.ancientempires.campaign.scripts.ScriptSetMapPosition;
import ru.ancientempires.campaign.scripts.ScriptSetUnitSpeed;
import ru.ancientempires.campaign.scripts.ScriptShowBlackScreen;
import ru.ancientempires.campaign.scripts.ScriptShowCursor;
import ru.ancientempires.campaign.scripts.ScriptShowInfoBar;
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
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.view.draws.campaign.DialogGameOver;
import ru.ancientempires.view.draws.campaign.DialogShowIntro;
import ru.ancientempires.view.draws.campaign.DialogShowTarget;
import ru.ancientempires.view.draws.campaign.GameDrawCameraMove;
import ru.ancientempires.view.draws.campaign.GameDrawUnitAttack;
import ru.ancientempires.view.draws.campaign.GameDrawUnitDie;
import ru.ancientempires.view.draws.campaign.MyDialogFragment;
import ru.ancientempires.view.draws.campaign.MyDialogWithoutImage;
import ru.ancientempires.view.draws.onframes.GameDrawBitmaps;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMove;

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
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "MyDialog");
	}
	
	@Override
	public void showDialog(String text, ScriptDialogWithoutImage script)
	{
		DialogFragment dialogFragment = new MyDialogWithoutImage(text, script);
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "MyDialogWithoutImage");
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
		Unit unit = new Unit(unitType, player);
		GameHandler.setUnit(i, j, unit);
		player.units.add(unit);
		if (GameHandler.checkCoord(i, j))
			this.gameDraw.gameDrawUnits.updateOneUnit(i, j);
		Campaign.finish(script);
	}
	
	@Override
	public void removeUnit(int i, int j, ScriptRemoveUnit script)
	{
		Unit unit = GameHandler.getUnit(i, j);
		GameHandler.removeUnit(i, j);
		unit.player.units.remove(unit);
		if (GameHandler.checkCoord(i, j))
			this.gameDraw.gameDrawUnits.updateOneUnit(i, j);
		Campaign.finish(script);
	}
	
	@Override
	public void unitMove(int iStart, int jStart, int iEnd, int jEnd, Script script)
	{
		Point[] points = new Point[Math.abs(iEnd - iStart) + Math.abs(jEnd - jStart) + 1];
		for (int i = iStart; i != iEnd; i += Math.signum(iEnd - iStart))
			points[Math.abs(i - iStart)] = new Point(i, jStart);
		for (int j = jStart; j != jEnd; j += Math.signum(jEnd - jStart))
			points[Math.abs(iEnd - iStart) + Math.abs(j - jStart)] = new Point(iEnd, j);
		points[points.length - 1] = new Point(iEnd, jEnd);
		
		GameDrawUnitMove gameDraw = new GameDrawUnitMove(this.gameDraw);
		gameDraw.init(iStart, jStart);
		gameDraw.start(points, null);
		this.draws.add(gameDraw);
		this.scripts.add(script);
		
		// TODO
		Unit unit = GameHandler.getUnit(iStart, jStart);
		GameHandler.removeUnit(iStart, jStart);
		GameHandler.setUnit(iEnd, jEnd, unit);
		
		this.gameDraw.gameDrawUnits.updateOneUnit(iStart, jStart);
	}
	
	@Override
	public void unitMove(Point[] keyPoints, ScriptUnitMoveExtended script)
	{
		Point start = keyPoints[0];
		Point end = keyPoints[keyPoints.length - 1];
		
		int length = 1;
		for (int i = 1; i < keyPoints.length; i++)
			length += Math.abs(keyPoints[i].i - keyPoints[i - 1].i) + Math.abs(keyPoints[i].j - keyPoints[i - 1].j);
			
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
		
		GameDrawUnitMove gameDraw = new GameDrawUnitMove(this.gameDraw);
		gameDraw.init(start.i, start.j);
		gameDraw.start(points, null);
		this.draws.add(gameDraw);
		this.scripts.add(script);
		
		// TODO
		Unit unit = GameHandler.getUnit(start.i, start.j);
		GameHandler.removeUnit(start.i, start.j);
		GameHandler.setUnit(end.i, end.j, unit);
		
		this.gameDraw.gameDrawUnits.updateOneUnit(start.i, start.j);
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
		GameActivity.gameActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				new AsyncTask<Void, Void, Void>()
				{
					private ProgressDialog progressDialog;
					
					@Override
					protected void onPreExecute()
					{
						this.progressDialog = new ProgressDialog(GameActivity.gameActivity);
						this.progressDialog.setMessage(GameActivity.gameActivity.getString(R.string.loading));
						this.progressDialog.show();
					}
					
					@Override
					protected Void doInBackground(Void... params)
					{
						GameActivity.gameView.stopThread();
						Client.getClient().startGame(Campaign.game.nextMission);
						return null;
					}
					
					@Override
					protected void onPostExecute(Void result)
					{
						this.progressDialog.dismiss();
						GameActivity.gameActivity.startGameView();
					}
				}.execute();
			}
		});
	}
	
	@Override
	public void gameOver(ScriptGameOver script)
	{
		DialogFragment dialogFragment = new DialogGameOver();
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "DialogGameOver");
	}
	
	@Override
	public void sparkDefault(int i, int j, ScriptSparkDefault script)
	{
		GameDrawBitmaps gameDraw = new GameDrawBitmaps(this.gameDraw)
				.setBitmaps(SparksImages.bitmapsDefault)
				.setYX(i * GameDraw.A, j * GameDraw.A)
				.animateRepeat(0, 1);
		this.draws.add(gameDraw);
		this.scripts.add(script);
	}
	
	@Override
	public void sparkAttack(int i, int j, ScriptSparkAttack script)
	{
		GameDrawBitmaps gameDraw = new GameDrawBitmaps(this.gameDraw)
				.setBitmaps(SparksImages.bitmapsAttack)
				.setYX(i * GameDraw.A, j * GameDraw.A)
				.animateRepeat(0, 1);
		this.draws.add(gameDraw);
		this.scripts.add(script);
	}
	
	@Override
	public void setMapPosition(int i, int j, ScriptSetMapPosition script)
	{
		this.gameDraw.focusOnCell(i, j);
		Campaign.finish(script);
	}
	
	@Override
	public void unitChangePosition(int i, int j, int iNew, int jNew, ScriptUnitChangePosition script)
	{
		Unit unit = GameHandler.getUnit(i, j);
		GameHandler.removeUnit(i, j);
		GameHandler.setUnit(iNew, jNew, unit);
		this.gameDraw.gameDrawUnits.updateOneUnit(i, j);
		this.gameDraw.gameDrawUnits.updateOneUnit(iNew, jNew);
		Campaign.finish(script);
	}
	
	@Override
	public void cellAttackPartTwo(int i, int j, ScriptCellAttackPartTwo script)
	{
		// TODO
		Cell targetCell = GameHandler.fieldCells[i][j];
		if (targetCell.player != null)
			GameHandler.game.currentEarns[targetCell.player.ordinal] -= targetCell.type.earn;
		targetCell.isDestroying = true;
		targetCell.isCapture = false;
		targetCell.player = null;
		
		this.gameDraw.gameDrawBuildingSmokes.update(this.gameDraw.game);
		
		this.gameDraw.gameDrawCells.updateOneCell(this.gameDraw.game, i, j);
		this.gameDraw.gameDrawCellDual.updateOneCell(this.gameDraw.game, i, j);
		GameDrawOnFrames gameDraw = new GameDrawBitmaps(this.gameDraw)
				.setYX(i * GameDraw.A, j * GameDraw.A)
				.setBitmaps(SparksImages.bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(0, 1);
		this.draws.add(gameDraw);
		this.scripts.add(script);
	}
	
}
