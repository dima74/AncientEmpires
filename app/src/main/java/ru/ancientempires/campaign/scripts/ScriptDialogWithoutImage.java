package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Localize;

public class ScriptDialogWithoutImage extends Script
{
	
	@Localize private String text;
	
	public ScriptDialogWithoutImage()
	{}
	
	public ScriptDialogWithoutImage(String text)
	{
		this.text = text;
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.dialog(text, this);
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
		object.addProperty("text", text);
		return object;
	}

	public ScriptDialogWithoutImage fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		text = ru.ancientempires.Localization.get(object.get("text").getAsString());
		return this;
	}

}
