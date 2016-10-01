package ru.ancientempires.processors;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Numbered;
import ru.ancientempires.serializable.SerializableData;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtClass;

public class DataProcessor extends MyAbstractManualProcessor {

	public HashMap<Class, String> classToSuffix = new HashMap<>();

	public void initStatic() throws Exception {
		initStatic(SerializableData.class);
		classToSuffix.put(int.class, "Int");
		classToSuffix.put(boolean.class, "Boolean");
		classToSuffix.put(String.class, "UTF");
	}

	@Override
	public void process() {
		try {
			initStatic();
			for (Class serializableClass : serializableClasses) {
				System.out.println("serializable " + serializableClass);
				CtClass ctClass = getFactory().Class().get(serializableClass);
				boolean simple = !allSubclasses.contains(ctClass.getActualClass());
				boolean base = simple || indexesClasses.contains(ctClass.getActualClass());
				createToData(ctClass, base);
				createFromData(ctClass, base);
			}

			writeChanges();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createToData(CtClass ctClass, boolean base) {
		Class actualClass = ctClass.getActualClass();
		CtBlock ctBlock = createMethod(ctClass, "toData", void.class, DataOutputStream.class, "output");
		String firstStatement = base ? "ru.ancientempires.serializable.SerializableDataHelper.toData(output, this)" : "super.toData(output)";
		ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(firstStatement));
		for (Field field : actualClass.getDeclaredFields())
			if (field.getAnnotation(Exclude.class) == null && !Modifier.isStatic(field.getModifiers()) && !field.getName().equals("result")) {
				String fieldName = field.getName();
				Class fieldType = field.getType();
				System.out.println(actualClass + " " + fieldType + " " + fieldName);

				String statement = null;
				if (classToSuffix.containsKey(fieldType))
					statement = String.format("output.write%s(%s)", classToSuffix.get(fieldType), fieldName);
				else if (fieldType.isEnum())
					statement = String.format("output.writeByte(%s.ordinal())", fieldName);
				else if (Numbered.class.isAssignableFrom(fieldType))
					statement = String.format("output.writeInt(%s.getNumber())", fieldName);
				else if (fieldType == boolean[].class)
					statement = String.format("ru.ancientempires.serializable.SerializableDataHelper.toDataArray(output, %s)", fieldName);
				else {
					MyAssert.a(SerializableData.class.isAssignableFrom(fieldType));
					statement = String.format("%s.toData(output)", fieldName);
				}
				ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
			}
	}

	public void createFromData(CtClass ctClass, boolean base) {
		Class actualClass = ctClass.getActualClass();
		CtBlock ctBlock = createMethod(ctClass, "fromData", actualClass, DataInputStream.class, "input", LoaderInfo.class, "info");
		if (!base)
			ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement("super.fromData(input, info)"));
		for (Field field : actualClass.getDeclaredFields())
			if (field.getAnnotation(Exclude.class) == null && !Modifier.isStatic(field.getModifiers()) && !field.getName().equals("result")) {
				String fieldName = field.getName();
				Class fieldType = field.getType();
				String fieldTypeName = fieldType.getSimpleName();
				System.out.println(actualClass + " " + fieldType + " " + fieldName);

				DataInputStream in;
				String statement = null;
				if (classToSuffix.containsKey(fieldType))
					statement = String.format("%s = input.read%s()", fieldName, classToSuffix.get(fieldType));
				else if (fieldType.isEnum())
					statement = String.format("%s = %s.values()[input.readByte()]", fieldName, fieldName);
				else if (Numbered.class.isAssignableFrom(fieldType))
					statement = String.format("%s = %s.newInstance(input.readInt(), info)", fieldName, fieldTypeName);
				else if (fieldType == boolean[].class)
					statement = String.format("%s = ru.ancientempires.serializable.SerializableDataHelper.fromDataArrayBoolean(input)", fieldName);
				else
					statement = String.format("%s = info.fromData(input, %s.class)", fieldName, fieldTypeName);
				ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
			}
		ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement("return this"));
	}

}