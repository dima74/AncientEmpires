package ru.ancientempires.campaign.points;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.atteo.classindex.IndexSubclasses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import ru.ancientempires.model.AbstractGameHandler;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableData;
import ru.ancientempires.serializable.SerializableJson;

@IndexSubclasses
public abstract class AbstractPoint extends AbstractGameHandler implements SerializableJson, SerializableData {

	public abstract int getI();

	public abstract int getJ();

	@Override
	public int hashCode() {
		final int prime = 777;
		int result = 1;
		result = prime * result + getI();
		result = prime * result + getJ();
		return result;
	}

	@Override
	public boolean equals(Object o) {
		AbstractPoint p = (AbstractPoint) o;
		return p.getI() == getI() && p.getJ() == getJ();
	}

	public static AbstractPoint[] createPoints(Object... points) {
		ArrayList<AbstractPoint> list = new ArrayList<>();
		for (int i = 0; i < points.length; i++) {
			Object o = points[i];
			if (o instanceof AbstractPoint)
				list.add((AbstractPoint) o);
			else if (o.getClass() == Integer.class) {
				list.add(new PointInteger((int) points[i], (int) points[i + 1]));
				++i;
			} else if (o.getClass() == String.class)
				list.add(new PointNamed((String) o));
		}
		return list.toArray(new AbstractPoint[list.size()]);
	}

	public static AbstractPoint createPoint(Object... points) {
		return createPoints(points)[0];
	}

	@Override
	public String toString() {
		return "{" + getI() + ", " + getJ() + "}";
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = ru.ancientempires.serializable.SerializableJsonHelper.toJson(this);
		return object;
	}

	public AbstractPoint fromJson(JsonObject object, LoaderInfo info) throws Exception {
		return this;
	}

	static public AbstractPoint[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception {
		AbstractPoint[] array = new AbstractPoint[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = info.fromJson(((com.google.gson.JsonObject) jsonArray.get(i)), AbstractPoint.class);
		return array;
	}

	public void toData(DataOutputStream output) throws Exception {
		ru.ancientempires.serializable.SerializableDataHelper.toData(output, this);
	}

	public AbstractPoint fromData(DataInputStream input, LoaderInfo info) throws Exception {
		return this;
	}
}
