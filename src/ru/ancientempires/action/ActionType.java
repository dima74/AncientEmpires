package ru.ancientempires.action;

public class ActionType
{
	
	public static ActionType	GET_UNIT	= new ActionType().setNotCritical();
	public static ActionType	GET_BUY		= new ActionType().setNotCritical();
	
	public static ActionType	GET_ACTIONS		= new ActionType().setNotCritical();
	public static ActionType	GET_UNIT_WAY	= new ActionType().setNotCritical();
	public static ActionType	GET_UNIT_ATTACK	= new ActionType().setNotCritical();
	public static ActionType	GET_UNIT_RAISE	= new ActionType().setNotCritical();
	
	public static ActionType	ACTION_CELL_BUY		= new ActionType();
	public static ActionType	ACTION_UNIT_MOVE	= new ActionType();
	public static ActionType	ACTION_UNIT_REPAIR	= new ActionType();
	public static ActionType	ACTION_UNIT_CAPTURE	= new ActionType();
	public static ActionType	ACTION_UNIT_ATTACK	= new ActionType();
	public static ActionType	ACTION_UNIT_RAISE	= new ActionType();
	public static ActionType	ACTION_END_TURN		= new ActionType();
	
	public static ActionType[] types = new ActionType[]
	{
			ActionType.GET_UNIT,
			ActionType.GET_BUY,
			
			// ActionType.GET_ACTIONS,
			// ActionType.GET_UNIT_ATTACK,
			// ActionType.GET_UNIT_WAY,
			// ActionType.GET_UNIT_RAISE,
			
			ActionType.ACTION_CELL_BUY,
			ActionType.ACTION_UNIT_MOVE,
			ActionType.ACTION_UNIT_REPAIR,
			ActionType.ACTION_UNIT_CAPTURE,
			ActionType.ACTION_UNIT_ATTACK,
			ActionType.ACTION_UNIT_RAISE,
			ActionType.ACTION_END_TURN
	};
	
	public static int amount;
	
	public static void init()
	{
		ActionType.amount = ActionType.types.length;
		for (int i = 0; i < ActionType.amount; i++)
			ActionType.types[i].ordinal = i;
	}
	
	public int ordinal;
	
	public boolean critical = true;
	
	private ActionType setNotCritical()
	{
		this.critical = false;
		return this;
	}
	
}
