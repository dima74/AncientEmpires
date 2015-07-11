package ru.ancientempires.view.draws;

import java.util.ArrayList;

import ru.ancientempires.DialogShowIntro;
import ru.ancientempires.DialogShowTarget;
import ru.ancientempires.IDrawCampaign;
import ru.ancientempires.MyDialogFragment;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptCameraMove;
import ru.ancientempires.campaign.scripts.ScriptDelay;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.campaign.scripts.ScriptHideCursor;
import ru.ancientempires.campaign.scripts.ScriptIntro;
import ru.ancientempires.campaign.scripts.ScriptShowCursor;
import ru.ancientempires.campaign.scripts.ScriptShowTarget;
import ru.ancientempires.campaign.scripts.ScriptTitle;
import ru.ancientempires.campaign.scripts.ScriptUnitAttack;
import ru.ancientempires.campaign.scripts.ScriptUnitDie;
import ru.ancientempires.campaign.scripts.ScriptUnitMove;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.Point;
import ru.ancientempires.view.algortihms.InputAlgorithmUnitMove;
import ru.ancientempires.view.draws.campaign.GameDrawCameraMove;
import ru.ancientempires.view.draws.campaign.GameDrawUnitAttack;
import ru.ancientempires.view.draws.campaign.GameDrawUnitDie;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMove;
import android.app.DialogFragment;
import android.graphics.Canvas;
import android.os.Handler;
import android.widget.Toast;

public class GameDrawCampaign extends GameDrawOnFramesGroup implements IDrawCampaign
{
	
	public ArrayList<Script>	scripts	= new ArrayList<Script>();
	
	public GameDrawCampaign(GameDrawMain gameDraw)
	{
		super(gameDraw);
		Campaign.iDrawCampaign = this;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
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
	}
	
	@Override
	public void showDialog(String imagePath, String text, ScriptDialog script)
	{
		DialogFragment dialogFragment = new MyDialogFragment(imagePath, text, script);
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "MyDialogFragment");
	}
	
	@Override
	public void showTitle(final String text, final ScriptTitle script)
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
		this.gameDraw.game.fieldUnits[iEnd][jEnd] = this.gameDraw.game.fieldUnits[iStart][jStart];
		this.gameDraw.game.fieldUnits[iStart][jStart] = null;
		
		GameDrawUnitMove gameDraw = new GameDrawUnitMove(this.gameDraw);
		gameDraw.init(iStart, jStart);
		gameDraw.start(ways, null);
		this.draws.add(gameDraw);
		this.scripts.add(script);
	}
	
	@Override
	public void cameraMove(int iEnd, int jEnd, ScriptCameraMove script)
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
					e.printStackTrace();
				}
				Campaign.finish(script);
			}
		}).run();
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
	
}
