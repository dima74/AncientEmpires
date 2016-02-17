package ru.ancientempires.campaign;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptType;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Game;

public class CampaignLoader
{
	
	private FileLoader	loader;
	private Game		game;
	private Campaign	campaign;
	
	private ArrayList<Script>	scripts			= new ArrayList<Script>();
	private ArrayList<int[]>	scriptsPrevious	= new ArrayList<int[]>();
	
	public CampaignLoader(FileLoader loader, Game game, Campaign campaign)
	{
		this.loader = loader;
		this.game = game;
		this.campaign = campaign;
	}
	
	public Script[] load() throws Exception
	{
		loadScripts();
		return endLoad();
	}
	
	private void loadScripts() throws Exception
	{
		JsonReader reader = loader.getReader("campaign.json");
		reader.beginObject();
		MyAssert.a(reader.nextName(), "scripts");
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
			loadNewScript(reader);
		reader.endArray();
		reader.endObject();
		reader.close();
	}
	
	public void loadNewScript(JsonReader reader) throws Exception
	{
		reader.beginObject();
		
		ScriptType type = ScriptType.valueOf(JsonHelper.readString(reader, "type"));
		// MyLog.l(type);
		
		MyAssert.a(reader.nextName(), "previous");
		Script script = type.scriptClass.newInstance();
		script.type = type;
		script.game = game;
		script.campaign = campaign;
		int[] previous = new Gson().fromJson(reader, int[].class);
		script.load(reader, scripts);
		reader.endObject();
		
		script.index = scripts.size();
		scripts.add(script);
		scriptsPrevious.add(previous);
	}
	
	public Script[] endLoad()
	{
		for (int i = 0; i < scripts.size(); i++)
		{
			Script script = scripts.get(i);
			int[] previous = scriptsPrevious.get(i);
			script.previous = new Script[previous.length];
			for (int j = 0; j < previous.length; j++)
				script.previous[j] = scripts.get(previous[j]);
		}
		return scripts.toArray(new Script[0]);
	}
	
}
