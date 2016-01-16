package ru.ancientempires.save;

import java.util.concurrent.ConcurrentLinkedQueue;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.save.GameSaver.SaveAction;

public class GameSaverThread extends Thread
{
	
	ConcurrentLinkedQueue<Save>	queue		= new ConcurrentLinkedQueue<Save>();
	volatile public boolean		isRunning	= true;
	
	@Override
	public void run()
	{
		try
		{
			while (isRunning)
			{
				Save action;
				while ((action = queue.poll()) != null)
				{
					System.out.print(action.getClass().getSimpleName().replace("Save", ""));
					if (action instanceof SaveAction)
					{
						SaveAction saveAction = (SaveAction) action;
						System.out.print(" " + saveAction.action.getClass().getSimpleName());
					}
					System.out.println();
					action.save();
				}
				Thread.yield();
			}
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
}
