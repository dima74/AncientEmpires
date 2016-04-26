package ru.ancientempires.campaign.conditions;

import org.atteo.classindex.IndexSubclasses;

import ru.ancientempires.handler.IGameHandler;

@IndexSubclasses
public abstract class AbstractBounds extends IGameHandler
{

	public abstract boolean in(int i, int j);

}
