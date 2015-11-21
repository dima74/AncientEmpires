package ru.ancientempires.view.inputs;

public abstract class InputAlgorithm
{
	
	public InputPlayer main;
	
	public InputAlgorithm(InputPlayer main)
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
	{}
	
	public void end()
	{
		destroy();
	}
	
}
