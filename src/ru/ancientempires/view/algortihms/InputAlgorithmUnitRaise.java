package ru.ancientempires.view.algortihms;

import ru.ancientempires.action.ActionType;

public class InputAlgorithmUnitRaise extends InputAlgorithmUnitRange
{
	
	public InputAlgorithmUnitRaise(InputAlgoritmMain main)
	{
		super(main);
		this.gameDrawZone = this.main.gameDraw.gameDrawZoneRaise;
		this.actionType = ActionType.ACTION_UNIT_RAISE;
		this.actionTypeGet = ActionType.GET_UNIT_RAISE;
	}
	
	@Override
	public void end()
	{
		super.end();
		this.main.gameDraw.gameDrawInfo.update(InputAlgorithm.game);
		this.main.gameDraw.gameDrawUnit.update(InputAlgorithm.game);
	}
	
}
