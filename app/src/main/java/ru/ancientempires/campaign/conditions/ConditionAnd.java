package ru.ancientempires.campaign.conditions;

import ru.ancientempires.campaign.scripts.Script;

public class ConditionAnd extends ConditionBoolean
{
	
	public ConditionAnd()
	{}
	
	public ConditionAnd(Script... scripts)
	{
		super(scripts);
	}
	
	@Override
	public boolean check()
	{
		for (Script script : scripts)
			if (!script.checkGeneral())
				return false;
		return true;
	}
	
}
