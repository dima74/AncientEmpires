package ru.ancientempires.load;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;

public class GameInit
{
	
	/*
		Механизм инициализации игры:
			0. Загружаем строки, как загрузили - показываем главное меню.
			1. Начинаем загружать список игр - GamePath'ы
					Если надо показать список игр, а он ещё не загрузился, то
					показываем прогресс бар и дозагружаем.
			2. После загрузки списка игр начинаем загружать правила и картинки.
				Если надо начинать игру, а вторая часть ещё не загрузилась, то
					показываем прогресс бар и дозагружаем.
	*/

	public Thread foldersInitThread;
	public Thread initThread;

	public void init(final Client client)
	{
		MyLog.l("GameInit.init()");
		foldersInitThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					client.loadPart1();
				}
				catch (Exception e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
			}
		};
		foldersInitThread.start();

		initThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					foldersInitThread.join();
					// Debug.startMethodTracing("traces/client");
					client.loadPart2();
					// Debug.stopMethodTracing();
				}
				catch (Exception e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
			}
		};
		initThread.start();
	}

}
