package ru.ancientempires.view.inputs;

import ru.ancientempires.action.ActionType;

public class InputAlgorithmUnitRaise extends InputAlgorithmUnitRange
{
	
	public InputAlgorithmUnitRaise(InputPlayer main)
	{
		super(main);
		// this.gameDrawZone = this.main.gameDraw.gameDrawZoneRaise;
		actionType = ActionType.ACTION_UNIT_RAISE;
		actionTypeGet = ActionType.GET_UNIT_RAISE;
	}
	
	@Override
	public void end()
	{
		super.end();
		
		if (isChanged)
		{
			InputBase.gameDraw.gameDrawInfo.update();
			InputBase.gameDraw.gameDrawUnitRaise.start(endI, endJ);
		}
	}
	
}
