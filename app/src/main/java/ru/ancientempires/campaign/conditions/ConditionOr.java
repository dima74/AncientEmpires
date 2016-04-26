package ru.ancientempires.campaign.conditions;

import ru.ancientempires.campaign.scripts.Script;

public class ConditionOr extends ConditionBoolean
{
	
	public ConditionOr()
	{}
	
	public ConditionOr(Script... scripts)
	{
		super(scripts);
	}
	
	@Override
	public boolean check()
	{
		for (Script script : scripts)
			if (script.checkGeneral())
				return true;
		return false;
	}
	
}
