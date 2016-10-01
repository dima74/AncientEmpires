package ru.ancientempires.campaign.scripts;

import android.graphics.Bitmap;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.BitmapPath;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Localize;

public class ScriptDialog extends Script {

	@Localize public    String text;
	@BitmapPath private String imagePath;
	private             String align;
	@Exclude private    Bitmap image;

	public ScriptDialog() {}

	public ScriptDialog(String text, String imagePath, String align) {
		this.text = text;
		this.imagePath = imagePath;
		this.align = align;
	}

	@Override
	public void start() {
		campaign.iDrawCampaign.dialog(image, text, this);
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public String toString() {
		return super.toString() + " " + imagePath + " " + text;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("text", text);
		object.addProperty("imagePath", imagePath);
		object.addProperty("align", align);
		return object;
	}

	public ScriptDialog fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		text = ru.ancientempires.Localization.get(object.get("text").getAsString());
		imagePath = object.get("imagePath").getAsString();
		image = ru.ancientempires.client.Client.client.imagesLoader.loadImage(imagePath);
		align = object.get("align").getAsString();
		return this;
	}
}
