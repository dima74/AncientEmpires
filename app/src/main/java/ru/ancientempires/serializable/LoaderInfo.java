package ru.ancientempires.serializable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.atteo.classindex.IndexSubclasses;

import java.io.DataInputStream;
import java.lang.reflect.Array;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.AbstractGameHandler;
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;

public class LoaderInfo {

	public LoaderInfo() {}

	public Game  game;
	public Rules rules;

	public LoaderInfo(Game game, Rules rules) {
		this.game = game;
		this.rules = rules;
	}

	public LoaderInfo(Game game) {
		this(game, game.rules);
	}

	public LoaderInfo(Rules rules) {
		this.rules = rules;
	}

	public <T extends SerializableJson> T fromJson(JsonObject object, Class<T> baseClass) throws Exception {
		MyAssert.a(baseClass.getAnnotation(IndexSubclasses.class) != null);
		int type = object.get("type").getAsInt();
		MyAssert.a(object.get("typeName").getAsString(), SerializableHelper.loadMap.get(baseClass)[type].getSimpleName());
		T result = (T) SerializableHelper.loadMap.get(baseClass)[type].newInstance();
		if (result instanceof AbstractGameHandler)
			((AbstractGameHandler) result).game = game;
		result.fromJson(object, this);
		return result;
	}

	public <T extends SerializableJson> T[] fromJsonArray(JsonArray jsonArray, Class<T> baseClass) throws Exception {
		T[] array = (T[]) Array.newInstance(baseClass, jsonArray.size());
		for (int i = 0; i < array.length; i++)
			array[i] = fromJson(((JsonObject) jsonArray.get(i)), baseClass);
		return array;
	}

	public <T extends SerializableJson> T[] fromJsonArraySimple(JsonArray jsonArray, Class<T> baseClass) throws Exception {
		T[] array = (T[]) Array.newInstance(baseClass, jsonArray.size());
		for (int i = 0; i < array.length; i++)
			array[i] = (T) baseClass.newInstance().fromJson((JsonObject) jsonArray.get(i), this);
		return array;
	}

	public <T extends SerializableData> T fromData(DataInputStream input, Class<T> baseClass) throws Exception {
		MyAssert.a(input.readInt(), 0x76543210);
		int type = input.readInt();
		T result = (T) SerializableHelper.loadMap.get(baseClass)[type].newInstance();
		result.fromData(input, this);
		if (result instanceof AbstractGameHandler)
			((AbstractGameHandler) result).game = game;
		return result;
	}

}
