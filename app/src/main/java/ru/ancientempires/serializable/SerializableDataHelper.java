package ru.ancientempires.serializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SerializableDataHelper
{

	public static void toData(DataOutputStream output, SerializableData object) throws Exception
	{
		output.writeInt(0x76543210);
		output.writeInt(SerializableHelper.saveMap.get(object.getClass()).index);
	}

	public static void toDataArray(DataOutputStream output, boolean[] array) throws Exception
	{
		output.writeInt(array.length);
		for (boolean b : array)
			output.writeBoolean(b);
	}

	public static boolean[] fromDataArrayBoolean(DataInputStream input) throws Exception
	{
		boolean[] array = new boolean[input.readInt()];
		for (int i = 0; i < array.length; i++)
			array[i] = input.readBoolean();
		return array;
	}

}
