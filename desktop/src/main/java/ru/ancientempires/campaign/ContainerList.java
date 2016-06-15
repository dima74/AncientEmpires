package ru.ancientempires.campaign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;

import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.framework.MyAssert;

public class ContainerList extends ArrayList<ScriptContainer>
{
	
	public ContainerList(ArrayList<ScriptContainer> containers)
	{
		super(containers);
	}
	
	public ContainerList(ScriptContainer... containers)
	{
		super(Arrays.asList(containers));
	}
	
	public ContainerList(Object... scripts)
	{
		Arrays
				.stream(scripts)
				.forEachOrdered(new Consumer<Object>()
				{
					@Override
					public void accept(Object o)
					{
						if (o instanceof Script)
							add(new ScriptContainer((Script) o));
						else if (o instanceof ContainerList)
						{
							ScriptContainer[] containers = ((ContainerList) o).root();
							for (ScriptContainer container : containers)
								add(container);
						}
						else
							MyAssert.a(false);
					}
				});
	}
	
	public ScriptContainer[] root()
	{
		HashSet<ScriptContainer> allContainers = new HashSet<ScriptContainer>();
		for (ScriptContainer container : this)
			ContainerList.addSubTree(allContainers, container);
		return allContainers
				.stream()
				.filter(container -> container.prev.isEmpty())
				.toArray(ScriptContainer[]::new);
	}
	
	private static void addSubTree(HashSet<ScriptContainer> allContainers, ScriptContainer container)
	{
		allContainers.add(container);
		for (ScriptContainer prev : container.prev)
			ContainerList.addSubTree(allContainers, prev);
	}
	
	// Script and ContainerList
	public ContainerList add(Object... scripts)
	{
		ContainerList list = new ContainerList(scripts);
		for (ScriptContainer container : this)
			container.add(list);
		return list;
	}
	
	public ContainerList first()
	{
		return new ContainerList(get(0));
	}
	
	public ContainerList last()
	{
		return new ContainerList(get(size() - 1));
	}
	
}
