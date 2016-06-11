package ru.ancientempires.serializable;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import ru.ancientempires.Localization;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.handler.IGameHandler;

public class ReflectionLoader
{

	public static <T> T load(JsonReader reader, Class<T> baseClass, LoaderInfo info) throws Exception
	{
		if (baseClass.isArray())
		{
			reader.beginArray();
			int length = reader.nextInt();
			Class<?> componentClass = baseClass.getComponentType();
			Object array = Array.newInstance(componentClass, length);
			for (int i = 0; i < length; i++)
				Array.set(array, i, load(reader, componentClass, info));
			reader.endArray();
			return (T) array;
		}
		else
		{
			reader.beginObject();
			Object object = ReflectionHelper.loadObject(reader, baseClass);
			Class<?> objectClass = object.getClass();
			while (reader.peek() == JsonToken.NAME)
			{
				String fieldName = reader.nextName();
				Field field = getField(objectClass, fieldName);
				field.setAccessible(true);
				Class<?> fieldClass = field.getType();
				if (fieldClass == int.class)
					field.set(object, reader.nextInt());
				else if (fieldClass == String.class)
				{
					String string = reader.nextString();
					field.set(object, string);
					if (field.isAnnotationPresent(BitmapPath.class))
					{
						Field fieldImage = getField(objectClass, "image");
						fieldImage.setAccessible(true);
						fieldImage.set(object, Client.client.imagesLoader.loadImage(string));
					}
					else if (field.isAnnotationPresent(Localize.class))
						field.set(object, Localization.get(string));
				}
				else if (fieldClass == boolean.class)
					field.set(object, reader.nextBoolean());
				else if (Numbered.class.isAssignableFrom(fieldClass))
					field.set(object, ReflectionHelper.getNumbered(fieldClass, reader.nextInt(), info));
				else if (Named.class.isAssignableFrom(fieldClass))
					field.set(object, ReflectionHelper.getNamed(fieldClass, reader.nextString(), info));
				else if (fieldClass.isEnum())
					field.set(object, Enum.valueOf((Class<Enum>) fieldClass, reader.nextString()));
				else if (field.isAnnotationPresent(AsNumberedArray.class))
				{
					reader.beginArray();
					int length = reader.nextInt();
					Class<?> componentClass = fieldClass.getComponentType();
					Object array = Array.newInstance(componentClass, length);
					for (int i = 0; i < length; i++)
						Array.set(array, i, ReflectionHelper.getNumbered(componentClass, reader.nextInt(), info));
					reader.endArray();
					field.set(object, array);
				}
				else
					field.set(object, load(reader, fieldClass, info));
			}
			reader.endObject();
			if (object instanceof IGameHandler)
				((IGameHandler) object).setGame(info.game);
			return (T) object;
		}
	}
	
	private static Field getField(Class<?> c, String name) throws Exception
	{
		name = name.intern();
		for (Class<?> current = c; current != Object.class; current = current.getSuperclass())
			for (Field field : current.getDeclaredFields())
				if (field.getName() == name)
					return field;
		MyAssert.a(false);
		return null;
	}
	
}
