package ru.ancientempires.processors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.AbstractGameHandler;
import ru.ancientempires.serializable.AfterFromJson;
import ru.ancientempires.serializable.BitmapPath;
import ru.ancientempires.serializable.CheckForNullAndEmpty;
import ru.ancientempires.serializable.EraseNulls;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Localize;
import ru.ancientempires.serializable.MyNullable;
import ru.ancientempires.serializable.MyNullableTo;
import ru.ancientempires.serializable.Named;
import ru.ancientempires.serializable.Numbered;
import ru.ancientempires.serializable.OnlyIf;
import ru.ancientempires.serializable.SerializableJson;
import ru.ancientempires.serializable.WithNamed;
import ru.ancientempires.serializable.WithNumbered;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtClass;

public class JsonProcessor extends MyAbstractManualProcessor {

	public HashMap<Class, String> classToSuffix                        = new HashMap<>();
	public HashSet<Class>         classesToAddNewInstanceArrayNumbered = new HashSet<>();
	public HashSet<Class>         classesToAddNewInstanceArrayNamed    = new HashSet<>();

	public void initStatic() throws Exception {
		initStatic(SerializableJson.class);
		classToSuffix.put(int.class, "Int");
		classToSuffix.put(Integer.class, "Int");
		classToSuffix.put(String.class, "String");
		classToSuffix.put(boolean.class, "Boolean");
	}

	@Override
	public void process() {
		newMethods.clear();
		try {
			initStatic();
			System.out.println("JsonProcessor.process");

			for (Class baseClass : baseClasses) {
				System.out.println("base " + baseClass);
				CtClass ctClass = getFactory().Class().get(baseClass);
				String simpleName = baseClass.getSimpleName();

				boolean simple = !allSubclasses.contains(ctClass.getActualClass());
				String inLoop = "array[i] = " + String.format(simple ? "new %s().fromJson((com.google.gson.JsonObject) jsonArray.get(i), info);"
						: "info.fromJson(((com.google.gson.JsonObject) jsonArray.get(i)), %s.class);", simpleName);
				CtBlock ctBlock = createMethodStatic(ctClass, "fromJsonArray", Array.newInstance(baseClass, 0).getClass(), JsonArray.class, "jsonArray", LoaderInfo.class, "info");
				String statement = String.format("%s[] array = new %s[jsonArray.size()];\n" +
						"\tfor (int i = 0; i < array.length; i++)\n" +
						"\t\t%s\n" +
						"\treturn array", simpleName, simpleName, inLoop);
				ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
			}

			for (Class serializableClass : serializableClasses) {
				System.out.println("serializable " + serializableClass);
				CtClass ctClass = getFactory().Class().get(serializableClass);
				boolean simple = !allSubclasses.contains(ctClass.getActualClass());
				boolean base = simple || indexesClasses.contains(ctClass.getActualClass());
				createToJson(ctClass, simple, base);
				createFromJson(ctClass, simple, base);
			}

			addNewInstanceArray(classesToAddNewInstanceArrayNumbered, "Numbered", "Int");
			addNewInstanceArray(classesToAddNewInstanceArrayNamed, "Named", "String");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//CtClass ctClass = getFactory().Class().get(Main.class);
		//CtMethod ctMethod = ctClass.getMethod("test2");
		//ctMethod.getBody().addStatement(getFactory().Code().createCodeSnippetStatement("new ru.ancientempires.reflection.LoaderInfo()").compile());
		//ctMethod.getBody().addStatement(getFactory().Code().createCodeSnippetStatement("new com.google.gson.JsonArray()").compile());
		//ctMethod.getBody().addStatement(getFactory().Code().createCodeSnippetStatement("new ru.ancientempires.reflection.LoaderInfo(null)"));
		//ctClass.compileAndReplaceSnippets();
	}

	public void addNewInstanceArray(HashSet<Class> classesToAddNewInstanceArray, String suffixMethod, String suffixGetAs) {
		for (Class c : classesToAddNewInstanceArray) {
			System.out.println("classesToAddNewInstanceArray" + suffixMethod + " " + c);
			CtClass ctClass = getFactory().Class().get(c);
			String simpleName = c.getSimpleName();

			CtBlock ctBlock = createMethodStatic(ctClass, "newInstanceArray" + suffixMethod, Array.newInstance(c, 0).getClass(), JsonArray.class, "jsonArray", LoaderInfo.class, "info");
			String statement = String.format("%s[] array = new %s[jsonArray.size()];\n" +
					"\tfor (int i = 0; i < array.length; i++)\n" +
					"\t\tarray[i] = newInstance(jsonArray.get(i).getAs%s(), info);\n" +
					"\treturn array", simpleName, simpleName, suffixGetAs);
			ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
		}
	}

	public void createToJson(CtClass ctClass, boolean simple, boolean base) throws Exception {
		Class actualClass = ctClass.getActualClass();
		CtBlock ctBlock = createMethodNoExcept(ctClass, "toJson", JsonObject.class);

		String firstStatement = String.format("JsonObject object = %s", simple ? "new JsonObject()" : base ? "ru.ancientempires.serializable.SerializableJsonHelper.toJson(this)" : "super.toJson()");
		ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(firstStatement));

		for (Field field : actualClass.getDeclaredFields())
			if (field.getAnnotation(Exclude.class) == null && !Modifier.isStatic(field.getModifiers())) {
				String fieldName = field.getName();
				Class fieldType = field.getType();
				System.out.println(actualClass + " " + fieldType + " " + fieldName);
				Class componentType = fieldType.isArray() ? fieldType.getComponentType() :
						Iterable.class.isAssignableFrom(fieldType) ? ((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]) : null;
				String componentTypeName = componentType == null ? null : componentType.getSimpleName();

				String statement = null;
				if (classToSuffix.containsKey(fieldType))
					statement = String.format("object.addProperty(\"%s\", %s)", fieldName, fieldName);
				else if (fieldType.isEnum())
					statement = String.format("object.addProperty(\"%s\", %s.name())", fieldName, fieldName);
				else if (field.getAnnotation(WithNumbered.class) != null) {
					WithNumbered annotation = field.getAnnotation(WithNumbered.class);
					statement = String.format("object.addProperty(\"%s\", game.%s.add(%s))", fieldName, annotation.value(), fieldName);
				} else if (Named.class.isAssignableFrom(fieldType))
					statement = String.format("object.addProperty(\"%s\", %s.getName())", fieldName, fieldName);
				else if (Numbered.class.isAssignableFrom(fieldType))
					statement = String.format("object.addProperty(\"%s\", %s.getNumber())", fieldName, fieldName);
				else if (componentType != null && Named.class.isAssignableFrom(componentType))
					statement = String.format("object.add(\"%s\", ru.ancientempires.serializable.SerializableJsonHelper.toJsonArrayNamed(%s))", fieldName, fieldName);
				else if (componentType != null && Numbered.class.isAssignableFrom(componentType))
					statement = String.format("object.add(\"%s\", ru.ancientempires.serializable.SerializableJsonHelper.toJsonArrayNumbered(%s))", fieldName, fieldName);
				else if (componentType != null)
					statement = String.format("object.add(\"%s\", ru.ancientempires.serializable.SerializableJsonHelper.toJsonArray(%s))", fieldName, fieldName);
				else
					statement = String.format("object.add(\"%s\", %s.toJson())", fieldName, fieldName);

				if (field.getAnnotation(MyNullable.class) != null || field.getAnnotation(MyNullableTo.class) != null)
					statement = String.format("if (%s != null)\n\t\t%s", fieldName, statement);
				if (field.getAnnotation(OnlyIf.class) != null)
					statement = String.format("if (%s())\n\t\t%s", field.getAnnotation(OnlyIf.class).value(), statement);
				if (field.getAnnotation(CheckForNullAndEmpty.class) != null)
					statement = String.format("if (%s != null && !%s.isEmpty())\n\t\t%s", fieldName, fieldName, statement);

				if (statement != null)
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
			}
		if (ctClass.getAnnotation(WithNamed.class) != null) {
			WithNamed annotation = ctClass.getAnnotation(WithNamed.class);
			ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("game.%s.toJsonPart(object, this)", annotation.value())));
		}
		if (ctClass.getAnnotation(WithNumbered.class) != null) {
			WithNumbered annotation = ctClass.getAnnotation(WithNumbered.class);
			String statement = String.format("game.%s.toJsonPart(object, this)", annotation.value());
			if (annotation.checkGameForNull())
				statement = "if (game != null)\n\t\t" + statement;
			ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
		}
		if (ctClass.getAnnotation(EraseNulls.class) != null)
			ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement("ru.ancientempires.serializable.SerializableJsonHelper.eraseNulls(object)"));
		ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement("return object"));
	}

	public void createFromJson(CtClass ctClass, boolean simple, boolean base) throws Exception {
		Class actualClass = ctClass.getActualClass();
		CtBlock ctBlock = createMethod(ctClass, "fromJson", actualClass, JsonObject.class, "object", LoaderInfo.class, "info");

		if (!base || (simple && AbstractGameHandler.class.isAssignableFrom(actualClass)))
			ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(simple ? "game = info.game" : "super.fromJson(object, info)"));

		for (Field field : actualClass.getDeclaredFields())
			if (field.getAnnotation(Exclude.class) == null && !Modifier.isStatic(field.getModifiers())) {
				String fieldName = field.getName();
				Class fieldType = field.getType();
				String fieldTypeName = fieldType.getSimpleName();
				Class componentType = fieldType.isArray() ? fieldType.getComponentType() :
						Iterable.class.isAssignableFrom(fieldType) ? ((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]) : null;
				String componentTypeName = componentType == null ? null : componentType.getSimpleName();

				String statement = null;
				if (field.getAnnotation(Localize.class) != null)
					statement = String.format("%s = ru.ancientempires.Localization.get(object.get(\"%s\").getAsString())", fieldName, fieldName);
				else if (classToSuffix.containsKey(fieldType)) {
					statement = String.format("%s = object.get(\"%s\").getAs%s()", fieldName, fieldName, classToSuffix.get(fieldType));
					if (field.getAnnotation(BitmapPath.class) != null)
						statement += String.format(";\n\timage = ru.ancientempires.client.Client.client.imagesLoader.loadImage(%s)", fieldName);
				} else if (fieldType.isEnum())
					statement = String.format("%s = %s.valueOf(object.get(\"%s\").getAsString())", fieldName, fieldTypeName, fieldName);
				else if (field.getAnnotation(WithNumbered.class) != null) {
					WithNumbered annotation = field.getAnnotation(WithNumbered.class);
					statement = String.format("%s = game.%s.get(object.get(\"%s\").getAsInt())", fieldName, annotation.value(), fieldName);
				} else if (Named.class.isAssignableFrom(fieldType))
					statement = String.format("%s = %s.newInstance(object.get(\"%s\").getAsString(), info)", fieldName, fieldTypeName, fieldName);
				else if (Numbered.class.isAssignableFrom(fieldType))
					statement = String.format("%s = %s.newInstance(object.get(\"%s\").getAsInt(), info)", fieldName, fieldTypeName, fieldName);
				else if (componentType != null && Named.class.isAssignableFrom(componentType)) {
					statement = String.format("%s = %s.newInstanceArrayNamed(object.get(\"%s\").getAsJsonArray(), info)", fieldName, componentTypeName, fieldName);
					classesToAddNewInstanceArrayNamed.add(fieldType.getComponentType());
				} else if (componentType != null && Numbered.class.isAssignableFrom(componentType)) {
					statement = String.format("%s = %s.newInstanceArrayNumbered(object.get(\"%s\").getAsJsonArray(), info)", fieldName, componentTypeName, fieldName);
					classesToAddNewInstanceArrayNumbered.add(fieldType.getComponentType());
				} else if (componentType != null) {
					String expression = !allSubclasses.contains(componentType) ?
							String.format("info.fromJsonArraySimple(object.get(\"%s\").getAsJsonArray(), %s.class)", fieldName, componentTypeName) :
							String.format("%s.fromJsonArray(object.get(\"%s\").getAsJsonArray(), info)", componentTypeName, fieldName);
					if (!fieldType.isArray())
						expression = String.format("new %s<>(Arrays.asList(%s))", fieldTypeName, expression);
					statement = String.format("%s = %s", fieldName, expression);
				} else {
					MyAssert.a(SerializableJson.class.isAssignableFrom(fieldType));
					statement = String.format("%s = info.fromJson((JsonObject) object.get(\"%s\"), %s.class)", fieldName, fieldName, fieldTypeName);
				}

				if (field.getAnnotation(MyNullable.class) != null || field.getAnnotation(CheckForNullAndEmpty.class) != null)
					statement = String.format("if (object.has(\"%s\"))\n\t\t%s", fieldName, statement);
				if (field.getAnnotation(OnlyIf.class) != null)
					statement = String.format("if (%s())\n\t\t%s", field.getAnnotation(OnlyIf.class).value(), statement);

				if (statement != null)
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
			}
		if (ctClass.getAnnotation(WithNamed.class) != null) {
			WithNamed annotation = ctClass.getAnnotation(WithNamed.class);
			ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("game.%s.fromJsonPart(object, this)", annotation.value())));
		}
		if (ctClass.getAnnotation(WithNumbered.class) != null) {
			WithNumbered annotation = ctClass.getAnnotation(WithNumbered.class);
			String statement = String.format("game.%s.fromJsonPart(object, this)", annotation.value());
			if (annotation.checkGameForNull())
				statement = "if (game != null)\n\t\t" + statement;
			ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
		}
		for (Method method : actualClass.getDeclaredMethods())
			if (method.getAnnotation(AfterFromJson.class) != null)
				ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(method.getName() + "()"));
		ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement("return this"));
	}
}
