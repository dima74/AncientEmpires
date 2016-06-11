package ru.ancientempires.model.struct;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJsonHelper;

public class StructCitadel extends Struct
{

	public UnitType crystalType;
	public int      crystalsRequired;
	public Constraint[] constraints = new Constraint[0];

	public StructCitadel()
	{}

	public StructCitadel(UnitType crystalType, int crystalsRequired)
	{
		this.crystalType = crystalType;
		this.crystalsRequired = crystalsRequired;
	}

	public StructCitadel addConstraint(int di, int dj, CellType type)
	{
		ArrayList<Constraint> list = new ArrayList<>(Arrays.asList(constraints));
		list.add(new Constraint(di, dj, type));
		constraints = list.toArray(new Constraint[list.size()]);
		return this;
	}

	public void checkStructInfo(Cell cell)
	{
		if (cell.structInfo == null)
			cell.structInfo = new StructInfoCitadel();
	}

	@Override
	public boolean canActivate(Cell cell)
	{
		checkStructInfo(cell);
		if (!(cell.structInfo instanceof StructInfoCitadel))
			return false;
		int i = cell.i;
		int j = cell.j;
		Game game = cell.game;
		for (Constraint constraint : constraints)
			if (!constraint.check(game, i, j))
				return false;
		if (crystalType != game.fieldUnits[i][j].type)
			return false;
		StructInfoCitadel structInfo = (StructInfoCitadel) cell.structInfo;
		return structInfo.crystalsReceived < crystalsRequired;
	}

	@Override
	public void activate(Cell cell)
	{
		MyAssert.a(canActivate(cell));
		int i = cell.i;
		int j = cell.j;
		Game game = cell.game;
		StructInfoCitadel structInfo = (StructInfoCitadel) cell.structInfo;
		Unit unit = game.fieldUnits[i][j];
		game.removeUnit(i, j);
		++structInfo.crystalsReceived;
	}

	@Override
	public void loadJSON(JsonElement element, LoaderInfo info)
	{
		JsonObject object = element.getAsJsonObject();
		crystalType = info.rules.getUnitType(object.get("crystalType").getAsString());
		crystalsRequired = element.getAsJsonObject().get("crystalsRequired").getAsInt();

		JsonArray constraints = object.get("constraints").getAsJsonArray();

	}

	@Override
	public JsonElement toJSON()
	{
		JsonObject result = new JsonObject();
		result.addProperty("crystalType", crystalType.name);
		result.addProperty("crystalsRequired", crystalsRequired);

		JsonArray constraints = new JsonArray();
		for (Constraint constraint : this.constraints)
		{
			JsonObject constraintObject = new JsonObject();
			constraintObject.addProperty("di", constraint.di);
			constraintObject.addProperty("dj", constraint.dj);
			constraintObject.addProperty("name", constraint.type.name);
			constraints.add(constraintObject);
		}
		result.add("constraints", constraints);
		return result;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("crystalType", crystalType.getName());
		object.addProperty("crystalsRequired", crystalsRequired);
		object.add("constraints", SerializableJsonHelper.toJsonArray(constraints));
		return object;
	}

	public StructCitadel fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		crystalType = UnitType.newInstance(object.get("crystalType").getAsString(), info);
		crystalsRequired = object.get("crystalsRequired").getAsInt();
		constraints = info.fromJsonArraySimple(object.get("constraints").getAsJsonArray(), Constraint.class);
		return this;
	}

}
