package ru.ancientempires.save;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.ancientempires.helpers.FileLoader;

public class FileCopier
{
	
	private FileLoader	from;
	private FileLoader	to;
	private byte[]		buffer	= new byte[1024 * 1024];
	
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
		if (!from.exists(name))
			return;
		InputStream input = from.openIS(name);
		OutputStream output = to.openOS(name);
		int size = input.read(buffer);
		output.write(buffer, 0, size);
		input.close();
		output.close();
	}
	
}
