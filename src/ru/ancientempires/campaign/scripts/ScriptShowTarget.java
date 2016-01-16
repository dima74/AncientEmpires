package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.Localization;
import ru.ancientempires.helpers.JsonHelper;

public class ScriptShowTarget extends Script
{
	
	private String	textTitle;
	private String	textTarget;
	
	public ScriptShowTarget()
	{}
	
	public ScriptShowTarget(String textTitle, String textTarget)
	{
		this.textTitle = textTitle;
		this.textTarget = textTarget;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		textTitle = Localization.get(JsonHelper.readString(reader, "text_title"));
		textTarget = Localization.get(JsonHelper.readString(reader, "text_target"));
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.showTarget(textTitle, textTarget, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("text_title").value(textTitle);
		writer.name("text_target").value(textTarget);
	}
	
}
