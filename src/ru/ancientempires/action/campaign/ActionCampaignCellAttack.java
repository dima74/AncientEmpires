package ru.ancientempires.action.campaign;

import ru.ancientempires.action.ActionFrom;
import ru.ancientempires.model.Cell;

public class ActionCampaignCellAttack extends ActionFrom
{
	
	@Override
	public boolean isCampaign()
	{
		return true;
	}
	
	@Override
	public void performQuick()
	{
		Cell targetCell = game.fieldCells[i][j];
		if (targetCell.player != null)
			game.currentEarns[targetCell.player.ordinal] -= targetCell.type.earn;
		targetCell.destroy();
	}
	
}
