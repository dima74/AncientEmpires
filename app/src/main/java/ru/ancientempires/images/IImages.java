package ru.ancientempires.images;

import java.io.IOException;

import ru.ancientempires.model.Game;

public abstract class IImages
{
	
	public void preload(ImagesLoader loader) throws IOException
	{}
	
	public void load(ImagesLoader loader, Game game) throws IOException
	{}
	
}
