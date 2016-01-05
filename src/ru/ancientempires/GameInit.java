package ru.ancientempires;

import java.io.IOException;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;

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
	
	public Thread	foldersInitThread;
	public Thread	initThread;
	
	public void init()
	{
		foldersInitThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					Client.client.loadPart1();
				}
				catch (IOException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
				initThread.start();
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
					// Debug.startMethodTracing("traces/client");
					Client.client.loadPart2();
					// Debug.stopMethodTracing();
				}
				catch (IOException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
			}
		};
	}
	
}
