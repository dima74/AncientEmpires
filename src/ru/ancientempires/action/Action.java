package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.handler.IGameHandler;
import ru.ancientempires.model.Game;

public abstract class Action extends IGameHandler
{
	
	public static List<Class<? extends Action>> classes = Arrays.asList(
			ActionGetUnit.class,
			ActionGetCellBuy.class,
			ActionCellBuy.class,
			ActionUnitMove.class,
			ActionUnitRepair.class,
			ActionUnitCapture.class,
			ActionUnitAttack.class,
			ActionUnitRaise.class,
			ActionGameEndTurn.class,
			ActionGetRandomNumber.class);
			
	public static Action loadNew(DataInputStream input) throws Exception
	{
		int ordinal = input.readShort();
		Action action = Action.classes.get(ordinal).newInstance();
		action.load(input);
		return action;
	}
	
	public Action()
	{
		setGame(null);
	}
	
	public boolean changesGame()
	{
		return true;
	}
	
	public boolean isCampaign()
	{
		return false;
	}
	
	public abstract ActionResult perform(Game game);
	
	public final void performBase(Game game)
	{
		checkBase(game);
		performQuick();
		if (game.isMain)
			Client.commit(this);
	}
	
	public void checkBase(Game game)
	{
		setGame(game);
		boolean successfully = check();
		MyAssert.a(successfully);
		if (!successfully)
			check();
	}
	
	public boolean check()
	{
		return true;
	}
	
	public abstract void performQuick();
	
	public final void performQuickBase(Game game)
	{
		setGame(game);
		performQuick();
	}
	
	public final void saveBase(DataOutputStream output) throws IOException
	{
		output.writeShort(ordinal());
		save(output);
	}
	
	public int ordinal()
	{
		return Action.classes.indexOf(getClass());
	}
	
	public void load(DataInputStream input) throws IOException
	{}
	
	public void save(DataOutputStream output) throws IOException
	{}
	
	@Override
	public String toString()
	{
		String s = this.getClass().getSimpleName().replace("Action", "") + " ";
		if (this instanceof ActionFromTo)
		{
			ActionFromTo thisCast = (ActionFromTo) this;
			s += coordinates(thisCast.i, thisCast.j)
					+ "->"
					+ coordinates(thisCast.targetI, thisCast.targetJ);
		}
		else if (this instanceof ActionFrom)
		{
			ActionFrom thisCast = (ActionFrom) this;
			s += coordinates(thisCast.i, thisCast.j);
		}
		else if (this instanceof ActionTo)
		{
			ActionTo action = (ActionTo) this;
			s += coordinates(action.targetI, action.targetJ);
		}
		// s = game.hashCode() + " " + s;
		return s;
	}
	
	private String coordinates(int i, int j)
	{
		return "(" + i + "," + j + ")";
	}
	
}
