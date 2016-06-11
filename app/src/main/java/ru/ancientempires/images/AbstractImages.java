package ru.ancientempires.images;

import java.io.IOException;

import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.model.Game;

public abstract class AbstractImages
{
	
	public void preload(FileLoader loader) throws IOException
	{}
	
	public void load(FileLoader loader, Game game) throws IOException
	{}
	
}
