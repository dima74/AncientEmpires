package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ru.ancientempires.action.handlers.ActionCellBuy;
import ru.ancientempires.action.handlers.ActionGameEndTurn;
import ru.ancientempires.action.handlers.ActionGetCellBuy;
import ru.ancientempires.action.handlers.ActionGetUnit;
import ru.ancientempires.action.handlers.ActionUnitAttack;
import ru.ancientempires.action.handlers.ActionUnitCapture;
import ru.ancientempires.action.handlers.ActionUnitMove;
import ru.ancientempires.action.handlers.ActionUnitRaise;
import ru.ancientempires.action.handlers.ActionUnitRepair;

public abstract class Action
{
	
	public static List<Class<? extends Action>> actions = Arrays.asList(
			ActionGetUnit.class,
			ActionGetCellBuy.class,
			ActionCellBuy.class,
			ActionUnitMove.class,
			ActionUnitRepair.class,
			ActionUnitCapture.class,
			ActionUnitAttack.class,
			ActionUnitRaise.class,
			ActionGameEndTurn.class);
			
	public ActionResult result = new ActionResult();
	
	public boolean isCritical()
	{
		return true;
	}
	
	public abstract ActionResult action();
	
	public void saveBase(DataOutputStream output) throws IOException
	{
		output.writeByte(ordinal());
		save(output);
		output.close();
	}
	
	public int ordinal()
	{
		return Action.actions.indexOf(getClass());
	}
	
	public void load(DataInputStream input) throws IOException
	{}
	
	public void save(DataOutputStream output) throws IOException
	{}
	
}
