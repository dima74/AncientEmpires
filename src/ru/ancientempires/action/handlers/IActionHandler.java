package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;

public interface IActionHandler
{
	
	public ActionResult action(Action action);
	
	public ActionHandler newInstance();
	
}
