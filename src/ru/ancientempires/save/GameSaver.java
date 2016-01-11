package ru.ancientempires.save;

import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.action.Action;
import ru.ancientempires.model.Game;

public class GameSaver
{
	
	private GameSaveLoader	loader;
	private Game			game;
	private SaveInfo		saveInfo	= new SaveInfo();
	
	public GameSaver(Game game)
	{
		this.game = game;
		loader = new GameSaveLoader(game.path.getLoader(), saveInfo);
		saveInfo.loader = loader;
	}
	
	public void save() throws IOException
	{
		if (saveInfo == null)
			saveInfo.load();
		checkSaveSnapshot();
	}
	
	private void checkSaveSnapshot() throws IOException
	{
		if (saveInfo.numberActionsAfterLastSave == 100)
		{
			++saveInfo.numberSnapshots;
			saveInfo.numberActionsAfterLastSave = 0;
			loader.snapshots().mkdirs("");
			loader.actions().mkdirs("");
			new GameSnapshotSaver(game, loader.snapshots()).save();
			saveInfo.save();
		}
	}
	
	public void save(Action action) throws IOException
	{
		String relativeNumberAction = "" + saveInfo.numberActionsAfterLastSave++;
		DataOutputStream dos = loader.actions().openDOS(relativeNumberAction);
		action.saveBase(dos);
		saveInfo.save();
		checkSaveSnapshot();
	}
	
}
