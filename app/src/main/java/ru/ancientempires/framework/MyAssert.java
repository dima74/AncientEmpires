package ru.ancientempires.framework;

import com.google.gson.JsonObject;

import java.io.DataOutputStream;
import java.io.PrintWriter;

import ru.ancientempires.serializable.SerializableJson;
import ru.ancientempires.serializable.SerializableJsonHelper;

public class MyAssert {

	public static DataOutputStream output;
	public static PrintWriter      outputText;

	public static void a(boolean booleanTrue) {
		a(booleanTrue, null);
	}

	public static void a(boolean booleanTrue, String message) {
		if (!booleanTrue) {
			MyLog.l("!!!");
			new Exception().printStackTrace();
			System.out.print("");
			//System.exit(1);
			//throw new RuntimeException();
		}
	}

	private static void method() {}

	public static void a(Object one, Object two) {
		boolean equals = one.equals(two);
		String message = null;
		if (!equals) {
			if (one instanceof SerializableJson)
				message = SerializableJsonHelper.diff(((SerializableJson) one), (SerializableJson) two);
			if (one instanceof JsonObject)
				message = SerializableJsonHelper.diff(((JsonObject) one), ((JsonObject) two));
		}
		MyAssert.a(equals, message);
	}

}
