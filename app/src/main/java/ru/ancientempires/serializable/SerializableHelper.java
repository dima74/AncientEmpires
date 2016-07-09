package ru.ancientempires.serializable;

import org.atteo.classindex.ClassIndex;
import org.atteo.classindex.IndexSubclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ancientempires.actions.Action;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.campaign.conditions.AbstractBounds;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.struct.Struct;
import ru.ancientempires.model.struct.StructInfo;
import ru.ancientempires.tasks.Task;

public class SerializableHelper
{
	
	private static Class<?>[] baseClasses = new Class<?>[] {
			Script.class,
			Task.class,
			Action.class,
			Bonus.class,
			AbstractPoint.class,
			AbstractBounds.class,
			Struct.class,
			StructInfo.class
	};

	public static HashMap<Class<?>, ClassInfo>  saveMap = createSave();
	public static HashMap<Class<?>, Class<?>[]> loadMap = createLoad();

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
			}
			catch (NoSuchMethodException e)
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
			map.put(base, getSubclasses(base));
		return map;
	}
	
	public static Class<?>[] getSubclasses(Class<?> base)
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
		return subclasses.toArray(new Class<?>[subclasses.size()]);
	}

}
