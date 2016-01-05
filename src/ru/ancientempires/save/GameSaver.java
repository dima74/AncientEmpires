package ru.ancientempires.save;

import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.action.Action;
import ru.ancientempires.helpers.FileHelper;
import ru.ancientempires.model.Game;

public class GameSaver
{
	
	private FileHelper	loader;
	private Game		game;
	private SaveInfo	saveInfo;
	
	public GameSaver(Game game)
	{
		this.game = game;
		loader = game.path.getLoader();
	}
	
	public void save() throws IOException
	{
		if (saveInfo == null)
			saveInfo = new SaveInfo(loader).load();
		checkSaveSnapshot();
	}
	
	private void checkSaveSnapshot() throws IOException
	{
		if (saveInfo.numberActions % 100 == 0)
		{
			loaderSnapshots().mkdirs("");
			loaderActions().mkdirs("");
			new GameSnapshotSaver(game, loaderSnapshots()).save();
			++saveInfo.numberSnapshots;
			saveInfo.save();
		}
	}
	
	public void save(Action action) throws IOException
	{
		String relativeNumberAction = String.valueOf(saveInfo.numberActions % 100);
		DataOutputStream dos = loaderActions().openDOS(relativeNumberAction);
		// action.saveBase(dos);
		++saveInfo.numberActions;
		saveInfo.save();
	}
	
	private FileHelper loaderSnapshots()
	{
		return loader.getLoader("snapshots/" + saveInfo.numberSnapshots + "/");
	}
	
	private FileHelper loaderActions()
	{
		return loader.getLoader("actions/" + saveInfo.numberSnapshots + "/");
	}
	
}
