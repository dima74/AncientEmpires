package ru.ancientempires.reflection;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.atteo.classindex.ClassIndex;
import org.atteo.classindex.IndexSubclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ancientempires.campaign.conditions.AbstractBounds;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.framework.MyAssert;

public class ReflectionHelper
{
	
	public static void saveType(JsonWriter writer, Object object) throws Exception
	{
		writer.name("type").value(saveMap.get(object.getClass()).index);
		writer.name("typeName").value(object.getClass().getSimpleName());
	}
	
	public static Object loadObject(JsonReader reader, Class<?> baseClass) throws Exception
	{
		MyAssert.a("type", reader.nextName());
		int type = reader.nextInt();
		MyAssert.a("typeName", reader.nextName());
		MyAssert.a(reader.nextString(), loadMap.get(baseClass)[type].getSimpleName());
		return loadMap.get(baseClass)[type].newInstance();
	}

	public static Object getNumbered(Class<?> baseClass, int i, LoaderInfo info) throws Exception
	{
		return baseClass.getMethod("newInstance", int.class, LoaderInfo.class).invoke(null, i, info);
	}

	public static Object getNamed(Class<?> baseClass, String name, LoaderInfo info) throws Exception
	{
		return baseClass.getMethod("newInstance", String.class, LoaderInfo.class).invoke(null, name, info);
	}

	private static Class<?>[] baseClasses = new Class<?>[] {
			Script.class,
			AbstractPoint.class,
			AbstractBounds.class
	};

	private static HashMap<Class<?>, ClassInfo>  saveMap = createSave();
	private static HashMap<Class<?>, Class<?>[]> loadMap = createLoad();

	private static HashMap<Class<?>, ClassInfo> createSave()
	{
		HashMap<Class<?>, ClassInfo> map = new HashMap<>(200);
		for (Class<?> base : baseClasses)
		{
			MyAssert.a(base.isAnnotationPresent(IndexSubclasses.class));
			int size = ((List) ClassIndex.getSubclasses(base)).size();
			int i = 0;
			for (Class<?> c : getSubclasses(base))
				map.put(c, new ClassInfo(i++, size));
		}

		for (Map.Entry<Class<?>, ClassInfo> entry : map.entrySet())
			try
			{
				entry.getKey().getConstructor();
			} catch (NoSuchMethodException e)
			{
				e.printStackTrace();
				MyAssert.a(false);
			}

		return map;
	}

	private static HashMap<Class<?>, Class<?>[]> createLoad()
	{
		HashMap<Class<?>, Class<?>[]> map = new HashMap<>();
		for (Class<?> base : baseClasses)
			map.put(base, getSubclasses(base).toArray(new Class<?>[0]));
		return map;
	}
	
	public static List<Class<?>> getSubclasses(Class<?> base)
	{
		ArrayList<Class<?>> subclasses = new ArrayList<>();
		for (Class<?> c : ClassIndex.getSubclasses(base))
			subclasses.add(c);
		Collections.sort(subclasses, new Comparator<Class<?>>()
		{
			@Override
			public int compare(Class<?> o1, Class<?> o2)
			{
				return o1.getSimpleName().compareTo(o2.getSimpleName());
			}
		});
		return subclasses;
	}
	
}
