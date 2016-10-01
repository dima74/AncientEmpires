package ru.ancientempires.campaign.scripts;

import android.graphics.Bitmap;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.BitmapPath;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Localize;

public class ScriptDialogIntro extends Script {

	@Localize private   String text;
	@BitmapPath private String imagePath;
	@Exclude private    Bitmap image;

	public ScriptDialogIntro() {}

	public ScriptDialogIntro(String text, String imagePath) {
		this.text = text;
		this.imagePath = imagePath;
	}

	@Override
	public void start() {
		campaign.iDrawCampaign.dialogIntro(image, text, this);
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("text", text);
		object.addProperty("imagePath", imagePath);
		return object;
	}

	public ScriptDialogIntro fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		text = ru.ancientempires.Localization.get(object.get("text").getAsString());
		imagePath = object.get("imagePath").getAsString();
		image = ru.ancientempires.client.Client.client.imagesLoader.loadImage(imagePath);
		return this;
	}

}
