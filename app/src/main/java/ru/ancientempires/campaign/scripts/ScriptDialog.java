package ru.ancientempires.campaign.scripts;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.Localization;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.reflection.BitmapPath;
import ru.ancientempires.reflection.Localize;

public class ScriptDialog extends Script
{
	
	@Localize public    String text;
	@BitmapPath private String imagePath;
	private             String align;
	@Expose private     Bitmap image;

	public ScriptDialog()
	{
	}
	
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
		campaign.iDrawCampaign.dialog(image, text, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("text").value(text);
		writer.name("image").value(imagePath);
		writer.name("align").value(align);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}

	@Override
	public String toString()
	{
		return super.toString() + " " + imagePath + " " + text;
	}
}
