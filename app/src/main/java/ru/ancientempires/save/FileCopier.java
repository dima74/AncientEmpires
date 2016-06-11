package ru.ancientempires.save;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.ancientempires.helpers.FileLoader;

public class FileCopier
{
	
	private FileLoader from;
	private FileLoader to;
	private byte[] buffer = new byte[1024 * 1024];
	
	public FileCopier(FileLoader from, FileLoader to)
	{
		this.from = from;
		this.to = to;
	}
	
	public void copy(String... names) throws IOException
	{
		for (String name : names)
			copy(name);
	}
	
	public void copy(String name) throws IOException
	{
		copyWithRename(name, name);
	}
	
	public void copyWithRename(String oldName, String newName) throws IOException
	{
		if (!from.exists(oldName))
			return;
		InputStream input = from.openIS(oldName);
		OutputStream output = to.openOS(newName);
		int size = input.read(buffer);
		output.write(buffer, 0, size);
		input.close();
		output.close();
	}
	
}
