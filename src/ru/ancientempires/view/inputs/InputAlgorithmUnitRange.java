package ru.ancientempires.view.inputs;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.view.draws.GameDrawRange;

public class InputAlgorithmUnitRange extends InputAlgorithm
{
	
	public InputAlgorithmUnitRange(InputPlayer main)
	{
		super(main);
	}
	
	public int	startI;
	public int	startJ;
	
	public boolean[][]	visibleRange;
	public boolean[][]	realRange;
	public int			size;
	public int			radius;
	
	public boolean isChanged;
	
	public int				endI;
	public int				endJ;
	public GameDrawRange	gameDrawZone;
	
	public ActionType	actionTypeGet;
	public ActionType	actionType;
	public ActionResult	resultGet;
	public ActionResult	result;
	
	@Override
	public void start(int i, int j)
	{
		startI = i;
		startJ = j;
		isChanged = false;
		
		Action action = new Action(actionTypeGet);
		action.setProperty("i", i);
		action.setProperty("j", j);
		resultGet = Client.action(action);
		
		visibleRange = (boolean[][]) resultGet.getProperty("visibleField");
		realRange = (boolean[][]) resultGet.getProperty("realField");
		size = visibleRange.length;
		radius = visibleRange.length / 2;
		
		gameDrawZone.startAnimate(this);
	}
	
	@Override
	public boolean tap(int i, int j)
	{
		endI = i;
		endJ = j;
		
		int relI = radius + i - startI;
		int relJ = radius + j - startJ;
		boolean boundsNorm = relI >= 0 && relI < size && relJ >= 0 && relJ < size;
		if (boundsNorm && realRange[relI][relJ])
		{
			isChanged = true;
			return true;
		}
		else
		{
			isChanged = false;
			destroy();
			// this.main.gameDraw.gameDrawUnits.updateOneUnit(this.startI, this.startJ);
			return false;
		}
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		// this.main.currentInputAlgorithmUnitRange = null;
		gameDrawZone.startReverseAnimate(this);
	}
	
	public void revertState()
	{
		destroy();
		InputBase.game.currentPlayer.cursorI = startI;
		InputBase.game.currentPlayer.cursorJ = startJ;
		InputBase.gameDraw.updateCursors();
		main.lastTapI = startI;
		main.lastTapJ = startJ;
	}
	
	@Override
	public void end()
	{
		super.end();
		if (isChanged)
		{
			final Action action = new Action(actionType);
			action.setProperty("i", startI);
			action.setProperty("j", startJ);
			action.setProperty("targetI", endI);
			action.setProperty("targetJ", endJ);
			result = Client.action(action);
		}
	}
	
}
