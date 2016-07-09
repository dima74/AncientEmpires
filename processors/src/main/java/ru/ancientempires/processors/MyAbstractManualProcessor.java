package ru.ancientempires.processors;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ru.ancientempires.serializable.DontGenerate;
import spoon.processing.AbstractManualProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

public abstract class MyAbstractManualProcessor extends AbstractManualProcessor
{

	public HashSet<Class> allSubclasses = new HashSet<>();
	public HashSet<Class> serializableClasses;
	public HashSet<Class> indexesClasses;
	public HashSet<Class> baseClasses;

	public void initStatic(Class<?> interfaceClass) throws Exception
	{
		// serializable --- собственно классы, к которым мы добавляем наши методы fromJson/toJson
		// serializableBase --- у которых мы генерируем метод toJson, вызывающий toJson у LoaderInfo, а также статические toJson и fromJson для массивов

		serializableClasses = getSubclasses(interfaceClass);
		indexesClasses = getAnnotatedIndexSubclasses();
		indexesClasses.removeIf(c -> !interfaceClass.isAssignableFrom(c));
		indexesClasses.remove(interfaceClass);

		serializableClasses.remove(interfaceClass);
		serializableClasses.removeIf(c -> c.getAnnotation(DontGenerate.class) != null);
		//serializableClasses.removeAll(indexesClasses);

		for (Class indexesClass : indexesClasses)
			allSubclasses.addAll(getSubclasses(indexesClass));
		allSubclasses.addAll(indexesClasses);

		baseClasses = new HashSet<>(indexesClasses);
		for (Class serializableClass : serializableClasses)
			if (!allSubclasses.contains(serializableClass))
				baseClasses.add(serializableClass);
		baseClasses.addAll(indexesClasses);

		System.out.println("serializableClasses = " + serializableClasses);
		System.out.println("baseClasses = " + baseClasses);
	}

	public static HashSet<Class> getSubclasses(Class c) throws Exception
	{
		File folder = new File("/home/dima/AndroidStudioProjects/AncientEmpires/processors/build/classes/main/META-INF/services");
		HashSet<Class> subclasses = new HashSet<>();
		for (String line : Files.readAllLines(new File(folder, c.getName()).toPath()))
			subclasses.add(Class.forName(line));
		return subclasses;
	}

	public static HashSet<Class> getAnnotatedIndexSubclasses() throws Exception
	{
		File folder = new File("/home/dima/AndroidStudioProjects/AncientEmpires/processors/build/classes/main/META-INF/services");
		HashSet<Class> subclasses = new HashSet<>();
		for (String file : folder.list())
			subclasses.add(Class.forName(file));
		return subclasses;
	}

	public static HashSet<CtMethod> newMethods = new HashSet<>();

	public CtBlock createMethod(CtClass ctClass, String name, Class returnType, Object... parametrs)
	{
		return createMethod(ctClass, new ModifierKind[] {ModifierKind.PUBLIC}, name, returnType, true, parametrs);
	}

	public CtBlock createMethodNoExcept(CtClass ctClass, String name, Class returnType, Object... parametrs)
	{
		return createMethod(ctClass, new ModifierKind[] {ModifierKind.PUBLIC}, name, returnType, false, parametrs);
	}

	public CtBlock createMethodStatic(CtClass ctClass, String name, Class returnType, Object... parametrs)
	{
		return createMethod(ctClass, new ModifierKind[] {
				ModifierKind.PUBLIC,
				ModifierKind.STATIC
		}, name, returnType, true, parametrs);
	}

	public CtBlock createMethod(CtClass ctClass, ModifierKind[] modifiers, String name, Class returnType, boolean exception, Object... parameters)
	{
		Set<ModifierKind> modifiersPublic = new HashSet<>(Arrays.asList(modifiers));
		CtTypeReference ctReturnType = getFactory().Type().createReference(returnType);
		HashSet<CtTypeReference<? extends Throwable>> thrownTypes = exception ? new HashSet<>(Arrays.asList(getFactory().Type().createReference(Exception.class))) : new HashSet<>();

		assert parameters.length % 2 == 0;
		ArrayList<CtParameter<?>> ctParameters = new ArrayList<>();
		for (int i = 0; i < parameters.length; i += 2)
		{
			Class parametrClass = (Class) parameters[i];
			String parametrName = (String) parameters[i + 1];
			ctParameters.add(getFactory().Method().createParameter(null, getFactory().Type().createReference(parametrClass), parametrName));
		}

		CtMethod ctMethod = getFactory().Method().create(ctClass, modifiersPublic, ctReturnType, name, ctParameters, thrownTypes);
		CtBlock ctBlock = getFactory().Core().createBlock();
		ctMethod.setBody(ctBlock);
		ctClass.addMethod(ctMethod);
		newMethods.add(ctMethod);
		return ctBlock;
	}

	public void writeChanges() throws IOException
	{
		PrintWriter output = new PrintWriter("/home/dima/AndroidStudioProjects/changesHandler/changedClasses.txt");
		newMethods
				.stream()
				.collect(Collectors.groupingBy(ctMethod -> ((CtClass) ctMethod.getParent()).getActualClass()))
				.forEach((changedClass, ctMethods) ->
				{
					String path = "/home/dima/AndroidStudioProjects/AncientEmpires/%s/" + changedClass.getCanonicalName().replace('.', '/') + ".java";
					String path1 = String.format(path, "app/src/main/java");
					String path2 = String.format(path, "desktop/build/generated-sources/spoon");
					output.println(path1);
					output.println(path2);
					output.println(ctMethods.size());
					ctMethods.forEach(output::println);
				});
		output.close();
	}

}
