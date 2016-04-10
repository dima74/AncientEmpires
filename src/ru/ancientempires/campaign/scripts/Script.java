package ru.ancientempires.campaign.scripts;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.CampaignEditorGame;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;

public abstract class Script
{
	
	// Нужен только при сохранении
	public int			index;
						
	public ScriptType	type;
	public Script[]		previous;
	public boolean		isStarting	= false;
	public boolean		isFinishing	= false;
									
	public Campaign		campaign;
	public Game			game;
						
	public void load(JsonReader reader, ArrayList<Script> scripts) throws IOException
	{
		load(reader);
	}
	
	public void load(JsonReader reader) throws IOException
	{}
	
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
	
	public final void saveGeneral(JsonWriter writer) throws IOException
	{
		writer.beginObject();
		writer.name("type").value(type.name());
		
		writer.name("previous").beginArray();
		for (Script script : previous)
			writer.value(script.index);
		writer.endArray();
		
		save(writer);
		writer.endObject();
	}
	
	public void save(JsonWriter writer) throws IOException
	{}
	
	public void finish()
	{
		MyAssert.a(!type.isSimple);
		campaign.finish(this);
	}
	
	public void performAction()
	{}
	
	// Используется только в конструкторах, которые вызываются только в редакторе кампании
	public Game getGame()
	{
		return CampaignEditorGame.game;
	}
	
	@Override
	public String toString()
	{
		return String.format("%d%d %3d %s", isStarting ? 1 : 0, isFinishing ? 1 : 0, index, getClass().getSimpleName());
	}
	
}
