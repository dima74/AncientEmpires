package ru.ancientempires.model;

import java.util.HashMap;
import java.util.Map;

public class NamedCoordinates {

	private static Map<String, Integer> coordinates = new HashMap<String, Integer>();

	public static int get(String name) {
		return NamedCoordinates.coordinates.get(name);
	}

	public static void set(String name, Integer coordinate) {
		NamedCoordinates.coordinates.put(name, coordinate);
	}
}
