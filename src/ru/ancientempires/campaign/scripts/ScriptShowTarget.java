package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.Localization;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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
		this.textTitle = Localization.get(JsonHelper.readString(reader, "text_title"));
		this.textTarget = Localization.get(JsonHelper.readString(reader, "text_target"));
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.showTarget(this.textTitle, this.textTarget, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("text_title").value(this.textTitle);
		writer.name("text_target").value(this.textTarget);
	}
	
}
