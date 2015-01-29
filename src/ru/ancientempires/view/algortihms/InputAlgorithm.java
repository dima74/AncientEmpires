package ru.ancientempires.view.algortihms;


public abstract class InputAlgorithm
{
	
	public InputAlgoritmMain	main;
	
	public InputAlgorithm(InputAlgoritmMain main)
	{
		this.main = main;
	}
	
	public void start(int i, int j)
	{}
	
	public boolean tap(int i, int j)
	{
		return false;
	}
	
	public void destroy()
	{
		// this.main.inputAlgorithms.remove(this);
	}
	
	public void end()
	{
		destroy();
	}
	
}
