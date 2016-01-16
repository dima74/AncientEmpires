package ru.ancientempires.save;

import ru.ancientempires.helpers.FileLoader;

public class GameSaveLoader extends FileLoader
{
	
	public SaveInfo saveInfo;
	
	public GameSaveLoader(FileLoader loader, SaveInfo saveInfo)
	{
		super(loader);
		this.saveInfo = saveInfo;
		saveInfo.loader = this;
	}
	
	public FileLoader snapshots()
	{
		return getLoader("snapshots/" + (saveInfo.numberSnapshots - 1) + "/");
	}
	
	public FileLoader actions()
	{
		return getLoader("actions/" + (saveInfo.numberSnapshots - 1) + "/");
	}
	
}
