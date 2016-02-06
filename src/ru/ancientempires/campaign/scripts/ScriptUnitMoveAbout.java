package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.action.campaign.ActionCampaignUnitMove;
import ru.ancientempires.helpers.JsonHelper;

public class ScriptUnitMoveAbout extends Script
{
	
	private int	i;
	private int	j;
	private int	targetI;
	private int	targetJ;
	
	public ScriptUnitMoveAbout()
	{}
	
	public ScriptUnitMoveAbout(int iStart, int jStart, int iEnd, int jEnd)
	{
		i = iStart;
		j = jStart;
		targetI = iEnd;
		targetJ = jEnd;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		i = JsonHelper.readInt(reader, "i");
		j = JsonHelper.readInt(reader, "j");
		targetI = JsonHelper.readInt(reader, "targetI");
		targetJ = JsonHelper.readInt(reader, "targetJ");
	}
	
	@Override
	public void start()
	{
		super.start();
		find();
		campaign.iDrawCampaign.unitMove(i, j, targetI, targetJ, this);
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignUnitMove()
				.setIJ(i, j)
				.setTargetIJ(targetI, targetJ)
				.perform(game);
	}
	
	private void find()
	{
		for (int a = 0;; a++)
			for (int i = -a; i <= a; i++)
				for (int j = -a; j <= a; j++)
					if (Math.abs(i) + Math.abs(j) == a)
						if (tryIJ(i, j))
							return;
	}
	
	private boolean tryIJ(int relativeI, int relativeJ)
	{
		int absoluteI = targetI + relativeI;
		int absoluteJ = targetJ + relativeJ;
		if (game.checkCoordinates(absoluteI, absoluteJ) && game.fieldUnits[absoluteI][absoluteJ] == null)
		{
			targetI = absoluteI;
			targetJ = absoluteJ;
			return true;
		}
		return false;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(i);
		writer.name("j").value(j);
		writer.name("targetI").value(targetI);
		writer.name("targetJ").value(targetJ);
	}
	
}
