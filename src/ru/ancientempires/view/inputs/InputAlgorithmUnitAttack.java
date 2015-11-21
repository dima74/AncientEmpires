package ru.ancientempires.view.inputs;

import ru.ancientempires.action.ActionType;

public class InputAlgorithmUnitAttack extends InputAlgorithmUnitRange
{
	
	public InputAlgorithmUnitAttack(InputPlayer main)
	{
		super(main);
		// this.gameDrawZone = this.main.gameDraw.gameDrawZoneAttack;
		actionType = ActionType.ACTION_UNIT_ATTACK;
		actionTypeGet = ActionType.GET_UNIT_ATTACK;
	}
	
	@Override
	public void start(int i, int j)
	{
		super.start(i, j);
		InputBase.gameDraw.gameDrawCursors.remove(InputBase.gameDraw.gameDrawCursorDefault);
		InputBase.gameDraw.gameDrawCursors.add(InputBase.gameDraw.gameDrawCursorAttack);
		InputBase.gameDraw.gameDrawCursorAttack.isDrawing = false;
	}
	
	@Override
	public boolean tap(int i, int j)
	{
		boolean isAction = super.tap(i, j);
		if (isAction)
			InputBase.gameDraw.gameDrawCursorAttack.isDrawing = true;
		return isAction;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		InputBase.gameDraw.gameDrawCursors.remove(InputBase.gameDraw.gameDrawCursorAttack);
		InputBase.gameDraw.gameDrawCursors.add(InputBase.gameDraw.gameDrawCursorDefault);
	}
	
	@Override
	public void end()
	{
		super.end();
		if (isChanged)
		{
			main.tapWithoutAction(endI, endJ);
			if ((boolean) result.getProperty("isAttackUnit"))
			{
				// InputBase.gameDraw.gameDrawUnitAttack.start(result);
			}
			else
			{
				InputBase.gameDraw.gameDrawBuildingSmokes.update();
				InputBase.gameDraw.gameDrawCellAttack.start(result, endI, endJ);
			}
		}
	}
	
}
