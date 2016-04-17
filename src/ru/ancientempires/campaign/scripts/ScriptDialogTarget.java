package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.Localization;
import ru.ancientempires.helpers.JsonHelper;

public class ScriptDialogTarget extends Script
{
	
	private String	textTitle;
	private String	textTarget;
					
	public ScriptDialogTarget()
	{}
	
	public ScriptDialogTarget(String textTitle, String textTarget)
	{
		this.textTitle = textTitle;
		this.textTarget = textTarget;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		textTitle = Localization.get(JsonHelper.readString(reader, "textTitle"));
		textTarget = Localization.get(JsonHelper.readString(reader, "textTarget"));
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.dialogTarget(textTitle, textTarget, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("textTitle").value(textTitle);
		writer.name("textTarget").value(textTarget);
	}
	
}
