package ru.ancientempires.serializable;

import com.google.gson.JsonObject;

import org.atteo.classindex.IndexSubclasses;

@IndexSubclasses
public interface SerializableJson {

	/*
		toJson --- одинаковый как для простых объектов (например Cell), так и для сложных (например Action/Script),
			просто сложные сами вызываеют SerializableJson.toJson(), а простые вместо этого начиают с "new JsonObject()"
		fromJson --- немного отличается,
			у простых newInstance + fromJson,
			у сложных сначала надо найти класс по свойству type Json-объекта
	*/

	JsonObject toJson();

	Object fromJson(JsonObject element, LoaderInfo info) throws Exception;

}
