package ru.ancientempires.campaign;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.actions.campaign.ActionCampaignRewriteScriptsStatus;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;
import ru.ancientempires.serializable.SerializableJsonHelper;

public class Campaign
{
	
	public Game game;
	
	public Script[]      scripts;
	public JsonArray     arrayState; // используется только при загрузке
	public IDrawCampaign iDrawCampaign;
	public boolean isDefault = false;
	public boolean needActionRewriteScriptsStatus;
	
	public Campaign(Game game)
	{
		this.game = game;
	}
	
	public void save(FileLoader loader) throws Exception
	{
		JsonWriter writer = loader.getWriter("campaign.json");
		JsonObject object = new JsonObject();
		object.add("scripts", SerializableJsonHelper.toJsonArray(scripts));
		if (isDefault)
			object.add("state", toJsonState());
		writer.jsonValue(object.toString());
		writer.close();
	}
	
	public void load(FileLoader loader) throws Exception
	{
		JsonReader reader = loader.getReader("campaign.json");
		JsonObject object = (JsonObject) new JsonParser().parse(reader);
		reader.close();

		this.scripts = game.getLoaderInfo().fromJsonArray((JsonArray) object.get("scripts"), Script.class);
		
		int i = 0;
		for (Script script : this.scripts)
		{
			script.campaign = this;
			//script.game = game;
			MyAssert.a(script.game == game);
			script.index = i++; // не будет использоваться, для отладки
			script.resolveAliases(this.scripts);
		}

		if (arrayState == null)
			arrayState = (JsonArray) object.get("state");
		fromJsonState(arrayState);
		arrayState = null;
	}
	
	public void start()
	{
		update();
	}
	
	public boolean isUpdate;
	//public boolean needSaveSnapshot;
	
	public void update()
	{
		if (isUpdate)
			return;
		isUpdate = true;
		boolean change = true;
		while (change)
		{
			change = false;
			for (Script script : scripts)
				if (!script.isStarting && script.checkGeneral())
				{
					change = true;
					script.isStarting = true;
					script.start();
					if (script.isSimple())
						script.isFinishing = true;
				}
		}
		
		isUpdate = false;
		if (needActionRewriteScriptsStatus)
		{
			new ActionCampaignRewriteScriptsStatus(game.campaign.scripts).perform(game);
			needActionRewriteScriptsStatus = false;
		}
		/*
		if (needSaveSnapshot)
		{
			try
			{
				game.saver.saveSnapshot();
			} catch (IOException e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
			needSaveSnapshot = false;
		}
		*/
	}
	
	public void finish(Script script)
	{
		script.isFinishing = true;
		iDrawCampaign.updateCampaign();
	}
	
	public void saveState(FileLoader loader) throws IOException
	{
		DataOutputStream output = loader.openDOS("campaignState.dat");
		for (Script script : scripts)
		{
			output.writeBoolean(script.isStarting);
			output.writeBoolean(script.isFinishing);
		}
		output.close();
	}
	
	public void loadState(FileLoader loader) throws IOException
	{
		DataInputStream input = loader.openDIS("campaignState.dat");
		for (Script script : scripts)
		{
			script.isStarting = input.readBoolean();
			script.isFinishing = input.readBoolean();
		}
		input.close();
	}
	
	public JsonArray toJsonState()
	{
		JsonArray array = new JsonArray();
		for (Script script : scripts)
		{
			array.add(new JsonPrimitive(script.isStarting ? 1 : 0));
			array.add(new JsonPrimitive(script.isFinishing ? 1 : 0));
		}
		return array;
	}
	
	public void fromJsonState(JsonArray array) throws IOException
	{
		for (int i = 0; i < scripts.length; i++)
		{
			scripts[i].isStarting = array.get(i * 2).getAsInt() == 1;
			scripts[i].isFinishing = array.get(i * 2 + 1).getAsInt() == 1;
		}
	}

	/*
	private static class SimpleScript extends Script
	{
		public SimpleScript()
		{}

		public SimpleScript(Script script)
		{
			isStarting = script.isStarting;
			isFinishing = script.isFinishing;
		}
	}
	
	public Campaign createSimpleCopy(Game otherGame)
	{
		Campaign campaign = new Campaign(otherGame);
		campaign.scripts = new Script[scripts.length];
		for (int i = 0; i < scripts.length; i++)
			campaign.scripts[i] = new SimpleScript(scripts[i]);
		return campaign;
	}
	*/
	
}
