package ru.ancientempires.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.model.Unit;

public abstract class TaskUnitWithValue extends Task
{
	
	public Unit	unit;
	public int	value;
	
	public TaskUnitWithValue setUnit(Unit unit)
	{
		this.unit = unit;
		return this;
	}
	
	public TaskUnitWithValue setValue(int value)
	{
		this.value = value;
		return this;
	}
	
	@Override
	public void load(DataInputStream input) throws IOException
	{
		int iUnit = input.readInt();
		unit = game.numberedUnits.get(iUnit);
		value = input.readInt();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		int iUnit = game.numberedUnits.add(unit);
		output.writeInt(iUnit);
		output.writeInt(value);
	}
	
}
