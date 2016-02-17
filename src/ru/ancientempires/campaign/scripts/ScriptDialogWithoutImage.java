package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.Localization;
import ru.ancientempires.helpers.JsonHelper;

public class ScriptDialogWithoutImage extends Script
{
	
	private String text;
	
	public ScriptDialogWithoutImage()
	{}
	
	public ScriptDialogWithoutImage(String text)
	{
		this.text = text;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		text = Localization.get(JsonHelper.readString(reader, "text"));
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.showDialog(text, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("text").value(text);
	}
	
}
