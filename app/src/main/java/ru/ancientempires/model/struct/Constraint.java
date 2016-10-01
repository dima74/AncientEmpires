package ru.ancientempires.model.struct;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;

public class Constraint implements SerializableJson {
	public int      di;
	public int      dj;
	public CellType type;

	public Constraint() {}

	public Constraint(int di, int dj, CellType type) {
		this.di = di;
		this.dj = dj;
		this.type = type;
	}

	public boolean check(Game game, int i, int j) {
		return game.checkCoordinates(i + di, j + dj) && game.fieldCells[i + di][j + dj].type == type;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = new JsonObject();
		object.addProperty("di", di);
		object.addProperty("dj", dj);
		object.addProperty("type", type.getNumber());
		return object;
	}

	public Constraint fromJson(JsonObject object, LoaderInfo info) throws Exception {
		di = object.get("di").getAsInt();
		dj = object.get("dj").getAsInt();
		type = CellType.newInstance(object.get("type").getAsInt(), info);
		return this;
	}

	static public Constraint[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception {
		Constraint[] array = new Constraint[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = new Constraint().fromJson((com.google.gson.JsonObject) jsonArray.get(i), info);
		return array;
	}

}
