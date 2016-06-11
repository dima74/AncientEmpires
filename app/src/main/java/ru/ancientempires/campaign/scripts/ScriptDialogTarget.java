package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.Localization;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Localize;

public class ScriptDialogTarget extends Script
{
	
	@Localize private String textTitle;
	@Localize private String textTarget;

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

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("textTitle", textTitle);
		object.addProperty("textTarget", textTarget);
		return object;
	}

	public ScriptDialogTarget fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		textTitle = ru.ancientempires.Localization.get(object.get("textTitle").getAsString());
		textTarget = ru.ancientempires.Localization.get(object.get("textTarget").getAsString());
		return this;
	}

}
