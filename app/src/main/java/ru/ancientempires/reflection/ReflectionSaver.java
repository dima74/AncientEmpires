package ru.ancientempires.reflection;

import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonWriter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import ru.ancientempires.framework.MyAssert;

public class ReflectionSaver
{
	
	static int stack;

	public static void save(JsonWriter writer, Object object) throws Exception
	{
		MyAssert.a(++stack < 10);
		if (object.getClass().isArray())
		{
			int length = Array.getLength(object);
			writer.beginArray().value(length);
			for (int i = 0; i < length; ++i)
				save(writer, Array.get(object, i));
			writer.endArray();
		} else
		{
			writer.beginObject();
			ReflectionHelper.saveType(writer, object);
			for (Class<?> current = object.getClass(); current != Object.class; current = current.getSuperclass())
				for (Field field : current.getDeclaredFields())
					if (!field.isAnnotationPresent(Expose.class))
					{
						field.setAccessible(true);
						Object o = field.get(object);
						if (o == null)
							continue;
						Class<?> c = o.getClass();
						String name = field.getName();
						writer.name(name);
						if (c == Integer.class)
							writer.value((Integer) o);
						else if (c == String.class)
							writer.value((String) o);
						else if (c == Boolean.class)
							writer.value((Boolean) o);
						else if (o instanceof Numbered)
							writer.value(((Numbered) o).getNumber());
						else if (o instanceof Named)
							writer.value(((Named) o).getName());
						else if (o.getClass().isEnum())
							writer.value(((Enum) o).name());
						else if (field.isAnnotationPresent(NumberedArray.class))
						{
							Numbered[] array = (Numbered[]) o;
							writer.beginArray().value(array.length);
							for (Numbered element : array)
								writer.value(element.getNumber());
							writer.endArray();
						} else if (field.isAnnotationPresent(NamedArray.class))
						{
							Named[] array = (Named[]) o;
							writer.beginArray().value(array.length);
							for (Named element : array)
								writer.value(element.getName());
							writer.endArray();
						} else
							save(writer, o);
					}
			writer.endObject();
		}
		--stack;
	}

}
