package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import android.graphics.Bitmap;
import ru.ancientempires.Localization;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.JsonHelper;

public class ScriptIntro extends Script
{
	
	private String	text;
	private String	imagePath;
	private Bitmap	image;
	
	public ScriptIntro()
	{}
	
	public ScriptIntro(String text, String imagePath)
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
		super.start();
		campaign.iDrawCampaign.showIntro(image, text, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("text").value(text);
		writer.name("image").value(imagePath);
	}
	
}
