package ru.ancientempires.campaign.scripts;

import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.atteo.classindex.IndexSubclasses;

import java.io.IOException;
import java.util.ArrayList;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.CampaignEditorGame;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;
import ru.ancientempires.reflection.LoaderInfo;
import ru.ancientempires.reflection.Numbered;
import ru.ancientempires.reflection.NumberedArray;

@IndexSubclasses
public abstract class Script implements Numbered
{

	public static Script newInstance(int i, LoaderInfo info)
	{
		return new ScriptLoaderAlias(i);
	}

	// Нужен только при сохранении
	@Expose public int index;

	@Override
	public int getNumber()
	{
		return index;
	}

	//@Expose public        ScriptType type;
	@NumberedArray public Script[] previous;
	@Expose public boolean isStarting  = false;
	@Expose public boolean isFinishing = false;

	@Expose public Campaign campaign;
	@Expose public Game     game;

	//*
	public void load(JsonReader reader, ArrayList<Script> scripts) throws IOException
	{
		load(reader);
	}
	
	public void load(JsonReader reader) throws IOException
	{
	}
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
	{
	}

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
	{
	}
	//*/
	
	public void finish()
	{
		MyAssert.a(!isSimple());
		campaign.finish(this);
	}
	
	public void performAction()
	{
	}
	
	// Используется только в конструкторах, которые вызываются только в редакторе кампании
	public Game getGame()
	{
		return CampaignEditorGame.game;
	}

	public String toString()
	{
		return String.format("%d%d %3d %s", isStarting ? 1 : 0, isFinishing ? 1 : 0, index, getClass().getSimpleName());
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

}
