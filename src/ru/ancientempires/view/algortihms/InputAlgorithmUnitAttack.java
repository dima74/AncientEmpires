package ru.ancientempires.view.algortihms;

import ru.ancientempires.action.ActionType;

public class InputAlgorithmUnitAttack extends InputAlgorithmUnitRange
{
	
	public InputAlgorithmUnitAttack(InputAlgoritmMain main)
	{
		super(main);
		this.gameDrawZone = this.main.gameDraw.gameDrawZoneAttack;
		this.actionType = ActionType.ACTION_UNIT_ATTACK;
		this.actionTypeGet = ActionType.GET_UNIT_ATTACK;
	}
	
	@Override
	public void start(int i, int j)
	{
		super.start(i, j);
		this.main.gameDraw.gameDrawCursors.remove(this.main.gameDraw.gameDrawCursorDefault);
		this.main.gameDraw.gameDrawCursors.add(this.main.gameDraw.gameDrawCursorAttack);
		this.main.gameDraw.gameDrawCursorAttack.isDrawing = false;
	}
	
	@Override
	public boolean tap(int i, int j)
	{
		boolean isAction = super.tap(i, j);
		if (isAction)
			this.main.gameDraw.gameDrawCursorAttack.isDrawing = true;
		return isAction;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		this.main.gameDraw.gameDrawCursors.remove(this.main.gameDraw.gameDrawCursorAttack);
		this.main.gameDraw.gameDrawCursors.add(this.main.gameDraw.gameDrawCursorDefault);
	}
	
	@Override
	public void end()
	{
		super.end();
		this.main.gameDraw.gameDrawInfo.update(InputAlgorithm.game);
		
		if (this.isChanged)
			if ((boolean) this.result.getProperty("isAttackUnit"))
				this.main.gameDraw.gameDrawUnitAttack.start(this.result);
			else
				this.main.gameDraw.gameDrawCellAttack.start(this.result);
	}
	
}
