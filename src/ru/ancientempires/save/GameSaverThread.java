package ru.ancientempires.save;

import java.util.concurrent.ConcurrentLinkedQueue;

import ru.ancientempires.framework.MyAssert;

public class GameSaverThread extends Thread
{
	
	ConcurrentLinkedQueue<Save>	queue		= new ConcurrentLinkedQueue<Save>();
	volatile private boolean	isRunning	= true;
	
	@Override
	public void run()
	{
		try
		{
			while (isRunning)
			{
				Save action;
				while ((action = queue.peek()) != null)
				{
					action.save();
					queue.poll();
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
