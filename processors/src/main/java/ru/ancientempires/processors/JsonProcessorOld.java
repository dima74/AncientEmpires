package ru.ancientempires.processors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.ancientempires.Main;
import ru.ancientempires.serializable.AsNumbered;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.MyNullable;
import ru.ancientempires.serializable.WithNumbered;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.Import;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.cu.CompilationUnitImpl;

/**
 * Reports warnings when empty catch blocks are found.
 */
public class JsonProcessorOld<T> extends AbstractProcessor<CtClass<T>>
{

	public static HashSet<String> serializable     = new HashSet<>();
	public static HashSet<String> serializableBase = new HashSet<>();

	@Override
	public void init()
	{
		System.out.println(getFactory().Class().get(Main.class));
		CompilationUnit unit = getFactory().CompilationUnit().getMap().get("/home/dima/AndroidStudioProjects/AncientEmpires/desktop/src/main/java/ru/ancientempires/Main.java");
		Import anImport = getFactory().CompilationUnit().createImport(JsonArray.class);
		HashSet<Import> imports = new HashSet<>();
		imports.add(anImport);
		((CompilationUnitImpl) unit).setManualImports(imports);
		try (PrintWriter writer = new PrintWriter("/home/dima/1"))
		{
			getFactory().CompilationUnit().getMap().keySet().forEach(writer::println);
			//System.out.println(getFactory().CompilationUnit().getMap().keySet().stream().findFirst().get());
			// /home/dima/AndroidStudioProjects/AncientEmpires/app/src/main/java/ru/ancientempires/GameInit.java
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addImports(CtClass ctClass, Class... importClasses)
	{
		String path = "/home/dima/AndroidStudioProjects/AncientEmpires/desktop/src/main/java/" + ctClass.getQualifiedName().replace('.', '/') + ".java";
		String path0 = "/home/dima/AndroidStudioProjects/AncientEmpires/desktop/src/main/java/ru/ancientempires/Main.java";
		System.out.println("1 " + path);
		System.out.println("2 " + path0);
		System.out.println(path.equals(path0));
		System.out.println(getFactory().CompilationUnit().getMap().get(path));
		System.out.println(getFactory().CompilationUnit().getMap().get(path0));
		CompilationUnitImpl unit = ((CompilationUnitImpl) getFactory().CompilationUnit().getMap().get(path));
		HashSet<Import> imports = new HashSet<>();
		Arrays
				.stream(importClasses)
				.map(getFactory().CompilationUnit()::createImport)
				.forEach(imports::add);
		System.out.println(imports);
		unit.setManualImports(imports);
	}

	static
	{
		try
		{
			System.out.println("JsonProcessorOld.static initializer");
			File folder = new File("/home/dima/AndroidStudioProjects/AncientEmpires/app/build/intermediates/classes/debug/META-INF/services");
			Files.lines(new File(folder, "ru.ancientempires.model.SerializableJson").toPath()).forEach(serializable::add);
			for (String base : folder.list())
				if (!base.contains("SerializableJson") && serializable.contains(base))
					serializableBase.add(base);
			serializable.removeAll(serializableBase);
			System.out.println("serializable = " + serializable);
			System.out.println("serializableBase = " + serializableBase);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static HashSet<Class> simpleClasses = new HashSet<>(Arrays.asList(String.class, int.class, boolean.class));

	@Override
	public void process(CtClass<T> ctClass)
	{
		if (ctClass.getActualClass() == Main.class)
		{
			//addImports(ctClass, JsonArray.class);
			CtMethod method = ctClass.getMethodsByName("test2").get(0);
			method.getBody().addStatement(getFactory().Code().createCodeSnippetStatement("com.google.gson.JsonArray array = new com.google.gson.JsonArray()").compile());
		}
		else
			return;

		try
		{
			if (serializable.contains(ctClass.getQualifiedName()))
			{
				System.out.println("serializable " + ctClass.getActualClass());
				createToJson(ctClass);
				createFromJson(ctClass);
			}

			if (serializableBase.contains(ctClass.getQualifiedName()))
			{
				System.out.println("serializableBase " + ctClass.getActualClass());
				CtBlock ctBlock = addToJson(ctClass);
				String statement = String.format("return ru.ancientempires.reflection.LoaderInfo.toJson(this, %s.class)", ctClass.getSimpleName());
				ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void createToJson(CtClass<T> ctClass) throws Exception
	{
		CtBlock ctBlock = addToJson(ctClass);
		ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement("JsonObject object = super.toJson()"));
		for (CtField<?> ctField : ctClass.getFields())
			if (ctField.getAnnotation(Exclude.class) == null)
			{
				Class fieldType = ctField.getType().getActualClass();
				String fieldName = ctField.getSimpleName();
				String statement = null;
				if (simpleClasses.contains(fieldType))
					statement = String.format("object.addProperty(\"%s\", %s)", fieldName, fieldName);
				else if (fieldType.isEnum())
					statement = String.format("object.addProperty(\"%s\", %s.name())", fieldName, fieldName);
				else if (ctField.getAnnotation(AsNumbered.class) != null)
					statement = String.format("object.addProperty(\"%s\", %s.getNumber())", fieldName, fieldName);
				else if (ctField.getAnnotation(WithNumbered.class) != null)
				{
					WithNumbered annotation = ctField.getAnnotation(WithNumbered.class);
					statement = String.format("object.addProperty(\"%s\", game.%s.add(%s))", fieldName, annotation.value(), fieldName);
				}
				else if (fieldType.isArray() || Iterable.class.isAssignableFrom(fieldType))
				{
					String arrayName = fieldName + "Array";
					Field field = ctClass.getActualClass().getField(fieldName);
					Class componentType = fieldType.isArray() ? fieldType.getComponentType() : (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("JsonArray %s = new JsonArray();", arrayName)));
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("for (%s %s : %s)\n", componentType.getSimpleName(), "element", fieldName)
					                                                                    + String.format("\t\t\t%s.add(%s.toJson())", arrayName, "element")));
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("object.add(\"%s\", %s)", fieldName, arrayName)));
					addImports(ctClass, JsonArray.class);
					/*
					Bonus[] bonuses = new Bonus[0];
					JsonArray %sArray = new JsonArray();
					for (Bonus bonus : bonuses)
					%sArray.add(bonus.toJson());
					object.add("%s", %sArray);
					*/
				}
				else
					statement = String.format("object.add(\"%s\", %s.toJson())", fieldName, fieldName);

				if (ctField.getAnnotation(MyNullable.class) != null)
					//ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("if (%s != null)", fieldName)));
					statement = String.format("if (%s != null)\n\t\t\t%s", fieldName, statement);

				if (statement != null)
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
			}
		ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement("return object"));
	}

	private void createVariable(CtBlock ctBlock, Class c, String name)
	{
		CtTypeReference<Object> ctTypeReference = getFactory().Code().createCtTypeReference(c);
		CtLocalVariable<Object> variable = getFactory().Code().createLocalVariable(ctTypeReference, name, getFactory().Code().createConstructorCall(ctTypeReference));
		ctBlock.addStatement(variable);
	}

	public static HashMap<Class, String> classToGetAsSuffix = new HashMap<>();

	static
	{
		classToGetAsSuffix.put(int.class, "Int");
		classToGetAsSuffix.put(String.class, "String");
		classToGetAsSuffix.put(boolean.class, "Boolean");
	}

	public void createFromJson(CtClass<T> ctClass) throws Exception
	{
		CtBlock ctBlock = addFromJson(ctClass);
		ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement("super.fromJson(object, info)"));
		for (CtField<?> ctField : ctClass.getFields())
			if (ctField.getAnnotation(Exclude.class) == null)
			{
				Class fieldType = ctField.getType().getActualClass();
				String fieldName = ctField.getSimpleName();
				String statement = null;
				if (simpleClasses.contains(fieldType))
					statement = String.format("%s = object.get(\"%s\").getAs%s()", fieldName, fieldName, classToGetAsSuffix.get(fieldType));
				else if (fieldType.isEnum())
					statement = String.format("%s = %s.valueOf(object.get(\"%s\").getAsString())", fieldName, fieldType.getSimpleName(), fieldName);
				else if (ctField.getAnnotation(AsNumbered.class) != null)
					statement = String.format("%s = %s.newInstance(object.get(\"%s\").getAsInt(), info)", fieldName, fieldType.getSimpleName(), fieldName);
				else if (ctField.getAnnotation(WithNumbered.class) != null)
				{
					WithNumbered annotation = ctField.getAnnotation(WithNumbered.class);
					statement = String.format("%s = game.%s.get(object.get(\"%s\").getAsInt());", fieldName, annotation.value(), fieldName);
				}
				else if (fieldType.isArray() || Iterable.class.isAssignableFrom(fieldType))
				{
					String arrayName = fieldName + "Array";
					String listName = fieldName + "List";
					Field field = ctClass.getActualClass().getField(fieldName);
					Class componentType = fieldType.isArray() ? fieldType.getComponentType() : (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
					String componentTypeName = componentType.getSimpleName();
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("JsonArray %s = (JsonArray) object.get(\"%s\")", arrayName, fieldName)));
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("ArrayList<%s> %s = new ArrayList<>()", componentTypeName, listName)));
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("for (JsonElement element : %s)\n", arrayName) +
					                                                                    String.format("\t\t\t%s.add(info.fromJson((JsonObject) element, %s.class))", listName, componentTypeName)));
					if (fieldType.isArray())
						ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("%s = %s.toArray(new %s[0])", fieldName, listName, componentTypeName)));
					else
						ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(String.format("%s = new HashSet<>(%s)", fieldName, listName)));
					getFactory().CompilationUnit().createImport(JsonElement.class);
					/*
					JsonObject object = null;
					JsonArray array = (JsonArray) object.get("%s");
					ArrayList<Bonus> bonusesList = new ArrayList<>();
					for (JsonElement element : array)
						bonusesList.add(info.fromJson((JsonObject) element, Bonus.class));
					bonuses = bonusesList.toArray(new Bonus[0]);
					bonuses = new HashSet<>(bonusesList);
					//*/
				}
				else
					statement = String.format("%s = info.fromJson((JsonObject) object.get(\"%s\"), %s.class)", fieldName, fieldName, fieldType.getSimpleName());

				if (ctField.getAnnotation(MyNullable.class) != null)
					statement = String.format("if (object.has(\"%s\"))\n\t\t\t%s", fieldName, statement);

				if (statement != null)
					ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement(statement));
			}
		ctBlock.addStatement(getFactory().Code().createCodeSnippetStatement("return this"));
	}

	public CtBlock addToJson(CtClass<T> ctClass)
	{
		return addMethod(ctClass, "toJson", JsonObject.class, null);
	}

	public CtBlock addFromJson(CtClass<T> ctClass)
	{
		List<CtParameter<?>> parametrs = Arrays.asList(
				getFactory().Method().createParameter(null, getFactory().Type().createReference(JsonObject.class), "object"),
				getFactory().Method().createParameter(null, getFactory().Type().createReference(LoaderInfo.class), "info"));
		return addMethod(ctClass, "fromJson", ctClass.getActualClass(), parametrs);
	}

	public CtBlock addMethod(CtClass<T> ctClass, String name, Class returnType, List<CtParameter<?>> ctParameters)
	{
		CtTypeReference ctType = getFactory().Type().createReference(returnType);
		CtTypeReference<Exception> ctTypeException = getFactory().Type().createReference(Exception.class);
		HashSet<CtTypeReference<? extends Throwable>> thrownTypes = new HashSet<>(Arrays.asList(ctTypeException));
		Set<ModifierKind> modifiersPublic = new HashSet<>(Arrays.asList(ModifierKind.PUBLIC));

		CtMethod ctMethod = getFactory().Method().create(ctClass, modifiersPublic, ctType, name, ctParameters, thrownTypes);
		CtBlock block = getFactory().Core().createBlock();
		ctMethod.setBody(block);
		ctClass.addMethod(ctMethod);
		return block;
	}

}
