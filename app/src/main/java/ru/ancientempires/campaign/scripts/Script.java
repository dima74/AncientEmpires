package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.atteo.classindex.IndexSubclasses;

import java.io.IOException;
import java.util.ArrayList;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.CampaignEditorGame;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.handler.IGameHandler;
import ru.ancientempires.model.Game;
import ru.ancientempires.serializable.AsNumberedArray;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Numbered;
import ru.ancientempires.serializable.SerializableJson;
import ru.ancientempires.serializable.SerializableJsonHelper;

@IndexSubclasses
public abstract class Script extends IGameHandler implements SerializableJson, Numbered
{

	public static Script newInstance(int i, LoaderInfo info)
	{
		return new ScriptLoaderAlias(i);
	}

	// Нужен только при сохранении
	@Exclude public int index;

	@Override
	public int getNumber()
	{
		return index;
	}

	//@Expose public        ScriptType type;
	@AsNumberedArray public Script[] previous;
	@Exclude public boolean isStarting  = false;
	@Exclude public boolean isFinishing = false;

	@Exclude public Campaign campaign;

	//*
	public void load(JsonReader reader, ArrayList<Script> scripts) throws IOException
	{
		load(reader);
	}
	
	public void load(JsonReader reader) throws IOException
	{}
	//*/

	public boolean isSimple()
	{
		return true;
	}

	public boolean checkGeneral()
	{
		for (Script script : previous)
			if (!script.isFinishing)
				return false;
		return check();
	}
	
	public boolean check()
	{
		return true;
	}
	
	public void start()
	{}

	//*
	public final void saveGeneral(JsonWriter writer) throws IOException
	{
		writer.beginObject();
		//writer.name("type").value(type.name());
		
		writer.name("previous").beginArray();
		for (Script script : previous)
			writer.value(script.index);
		writer.endArray();
		
		// save(writer);
		writer.endObject();
	}
	
	public void save(JsonWriter writer) throws IOException
	{}
	//*/
	
	public void finish()
	{
		MyAssert.a(!isSimple());
		campaign.finish(this);
	}
	
	public void performAction()
	{}
	
	// Используется только в конструкторах, которые вызываются только в редакторе кампании
	public Game getGame()
	{
		return CampaignEditorGame.game;
	}

	public void resolveAliases(Script[] scripts)
	{
		resolveAliases(previous, scripts);
	}

	public static void resolveAliases(Script[] scripts, Script[] all)
	{
		for (int i = 0; i < scripts.length; i++)
			if (scripts[i].getClass() == ScriptLoaderAlias.class)
				scripts[i] = all[((ScriptLoaderAlias) scripts[i]).i];
	}

	public String toString()
	{
		return String.format("%d%d %3d %s", isStarting ? 1 : 0, isFinishing ? 1 : 0, index, getClass().getSimpleName());
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = SerializableJsonHelper.toJson(this);
		object.add("previous", SerializableJsonHelper.toJsonArrayNumbered(previous));
		return object;
	}

	public Script fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		previous = Script.newInstanceArrayNumbered(object.get("previous").getAsJsonArray(), info);
		return this;
	}

	static public Script[] newInstanceArrayNumbered(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		Script[] array = new Script[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = newInstance(jsonArray.get(i).getAsInt(), info);
		return array;
	}

	static public Script[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		Script[] array = new Script[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = info.fromJson(((com.google.gson.JsonObject) jsonArray.get(i)), Script.class);
		return array;
	}

}
