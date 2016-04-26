package ru.ancientempires.campaign.scripts;

import ru.ancientempires.framework.MyAssert;

public class ScriptLoaderAlias extends Script
{

	public int i;

	public ScriptLoaderAlias()
	{
	}

	public ScriptLoaderAlias(int i)
	{
		this.i = i;
	}

	@Override
	public boolean check()
	{
		MyAssert.a(false);
		return false;
	}
}
