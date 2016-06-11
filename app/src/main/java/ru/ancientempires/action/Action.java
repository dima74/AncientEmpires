package ru.ancientempires.action;

import org.atteo.classindex.IndexSubclasses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.List;

import ru.ancientempires.action.campaign.ActionCampaignCellAttack;
import ru.ancientempires.action.campaign.ActionCampaignRemoveUnit;
import ru.ancientempires.action.campaign.ActionCampaignRewriteScriptsStatus;
import ru.ancientempires.action.campaign.ActionCampaignSetNamedPoint;
import ru.ancientempires.action.campaign.ActionCampaignSetNamedUnit;
import ru.ancientempires.action.campaign.ActionCampaignUnitChangePosition;
import ru.ancientempires.action.campaign.ActionCampaignUnitCreate;
import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.handler.IGameHandler;
import ru.ancientempires.model.Game;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableData;
import ru.ancientempires.serializable.SerializableDataHelper;

@IndexSubclasses
public abstract class Action extends IGameHandler implements SerializableData
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
			ActionGetRandomNumber.class,
			ActionActivateStruct.class,

			ActionCampaignCellAttack.class,
			ActionCampaignRemoveUnit.class,
			ActionCampaignRewriteScriptsStatus.class,
			ActionCampaignSetNamedPoint.class,
			ActionCampaignSetNamedUnit.class,
			ActionCampaignUnitChangePosition.class,
			ActionCampaignUnitCreate.class);

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

	/*
	Сейчас action'ы которые относятся к кампании не сохраняются, вместо этого после ScriptEnableActiveGame сохраняется snapshot.
	Может быть стоит сделать чтобы эти action'ы сохранялись, единственная проблема возникнет если игра прервётся во время кампании,
	ну наверно можно просто доавить в saveInfo номер последнего action'а который нужно выполнять
	*/
	public boolean isCampaign()
	{
		return false;
	}
	
	public ActionResult perform(Game game)
	{
		performBase(game);
		return null;
	}
	
	public final void performBase(Game game)
	{
		try
		{
			checkBase(game);
			performQuick();
			if (game.isMain)
				Client.commit(this);
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
	public final void checkBase(Game game)
	{
		setGame(game);
		boolean successfully = /*isCampaign() || */check();
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
		game.allActions.add(this);
		performQuick();
	}
	
	public final void saveBase(DataOutputStream output) throws Exception
	{
		output.writeShort(ordinal());
		save(output);
	}

	public int ordinal()
	{
		MyAssert.a(classes.contains(getClass()));
		return classes.indexOf(getClass());
	}
	
	public void load(DataInputStream input) throws Exception
	{}
	
	public void save(DataOutputStream output) throws Exception
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
	
	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		SerializableDataHelper.toData(output, this);
	}

	public Action fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		return this;
	}

}
