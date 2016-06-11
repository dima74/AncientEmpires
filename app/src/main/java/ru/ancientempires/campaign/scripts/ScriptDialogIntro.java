package ru.ancientempires.campaign.scripts;

import android.graphics.Bitmap;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.Localization;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.serializable.BitmapPath;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Localize;

public class ScriptDialogIntro extends Script
{
	
	@Localize private   String text;
	@BitmapPath private String imagePath;
	@Exclude private    Bitmap image;

	public ScriptDialogIntro()
	{
	}
	
	public ScriptDialogIntro(String text, String imagePath)
	{
		this.text = text;
		this.imagePath = imagePath;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		text = Localization.get(JsonHelper.readString(reader, "text"));
		imagePath = JsonHelper.readString(reader, "image");
		image = Client.client.imagesLoader.loadImage(imagePath);
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.dialogIntro(image, text, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("text").value(text);
		writer.name("image").value(imagePath);
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
		object.addProperty("imagePath", imagePath);
		return object;
	}

	public ScriptDialogIntro fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		text = ru.ancientempires.Localization.get(object.get("text").getAsString());
		imagePath = object.get("imagePath").getAsString();
		image = Client.client.imagesLoader.loadImage(imagePath);
		return this;
	}

}
