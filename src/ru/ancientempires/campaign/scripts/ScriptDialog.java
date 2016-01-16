package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import android.graphics.Bitmap;
import ru.ancientempires.Localization;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.JsonHelper;

public class ScriptDialog extends Script
{
	
	public String	text;
	private String	imagePath;
	private String	align;
	private Bitmap	image;
	
	public ScriptDialog()
	{}
	
	public ScriptDialog(String text, String imagePath, String align)
	{
		this.text = text;
		this.imagePath = imagePath;
		this.align = align;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		text = Localization.get(JsonHelper.readString(reader, "text"));
		imagePath = JsonHelper.readString(reader, "image");
		image = Client.client.imagesLoader.loadImage(imagePath);
		align = JsonHelper.readString(reader, "align");
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.showDialog(image, text, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("text").value(text);
		writer.name("image").value(imagePath);
		writer.name("align").value(align);
	}
	
}
