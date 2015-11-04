package ru.ancientempires.view.algortihms;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.view.draws.GameDrawRange;

public class InputAlgorithmUnitRange extends InputAlgorithm
{
	
	public InputAlgorithmUnitRange(InputAlgorithmMain main)
	{
		super(main);
	}
	
	public int				startI;
	public int				startJ;
	
	public boolean[][]		visibleRange;
	public boolean[][]		realRange;
	public int				size;
	public int				radius;
	
	public boolean			isChanged;
	
	public int				endI;
	public int				endJ;
	public GameDrawRange	gameDrawZone;
	
	public ActionType		actionTypeGet;
	public ActionType		actionType;
	public ActionResult		resultGet;
	public ActionResult		result;
	
	@Override
	public void start(int i, int j)
	{
		this.startI = i;
		this.startJ = j;
		this.isChanged = false;
		
		Action action = new Action(this.actionTypeGet);
		action.setProperty("i", i);
		action.setProperty("j", j);
		this.resultGet = Client.action(action);
		
		this.visibleRange = (boolean[][]) this.resultGet.getProperty("visibleField");
		this.realRange = (boolean[][]) this.resultGet.getProperty("realField");
		this.size = this.visibleRange.length;
		this.radius = this.visibleRange.length / 2;
		
		this.gameDrawZone.startAnimate(this);
	}
	
	@Override
	public boolean tap(int i, int j)
	{
		this.endI = i;
		this.endJ = j;
		
		int relI = this.radius + i - this.startI;
		int relJ = this.radius + j - this.startJ;
		boolean boundsNorm = relI >= 0 && relI < this.size && relJ >= 0 && relJ < this.size;
		if (boundsNorm && this.realRange[relI][relJ])
		{
			this.isChanged = true;
			return true;
		}
		else
		{
			this.isChanged = false;
			destroy();
			//this.main.gameDraw.gameDrawUnits.updateOneUnit(this.startI, this.startJ);
			return false;
		}
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		this.main.currentInputAlgorithmUnitRange = null;
		this.gameDrawZone.startReverseAnimate(this);
	}
	
	public void revertState()
	{
		destroy();
		this.main.game.currentPlayer.cursorI = this.startI;
		this.main.game.currentPlayer.cursorJ = this.startJ;
		this.main.gameDraw.updateCursors(this.main.game);
		this.main.lastTapI = this.startI;
		this.main.lastTapJ = this.startJ;
	}
	
	@Override
	public void end()
	{
		super.end();
		if (this.isChanged)
		{
			final Action action = new Action(this.actionType);
			action.setProperty("i", this.startI);
			action.setProperty("j", this.startJ);
			action.setProperty("targetI", this.endI);
			action.setProperty("targetJ", this.endJ);
			this.result = Client.action(action);
		}
	}
	
}
