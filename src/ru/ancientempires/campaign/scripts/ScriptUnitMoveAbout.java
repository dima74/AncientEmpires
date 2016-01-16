package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.helpers.JsonHelper;

public class ScriptUnitMoveAbout extends Script
{
	
	private int	iStart;
	private int	jStart;
	private int	iEnd;
	private int	jEnd;
	
	public ScriptUnitMoveAbout()
	{}
	
	public ScriptUnitMoveAbout(int iStart, int jStart, int iEnd, int jEnd)
	{
		this.iStart = iStart;
		this.jStart = jStart;
		this.iEnd = iEnd;
		this.jEnd = jEnd;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		iStart = JsonHelper.readInt(reader, "iStart");
		jStart = JsonHelper.readInt(reader, "jStart");
		iEnd = JsonHelper.readInt(reader, "iEnd");
		jEnd = JsonHelper.readInt(reader, "jEnd");
	}
	
	@Override
	public void start()
	{
		super.start();
		find();
		campaign.iDrawCampaign.unitMove(iStart, jStart, iEnd, jEnd, this);
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
	
	private boolean tryIJ(int relI, int relJ)
	{
		int absI = iEnd + relI;
		int absJ = jEnd + relJ;
		if (game.checkCoordinates(absI, absJ) && game.fieldUnits[absI][absJ] == null)
		{
			iEnd = absI;
			jEnd = absJ;
			return true;
		}
		return false;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("iStart").value(iStart);
		writer.name("jStart").value(jStart);
		writer.name("iEnd").value(iEnd);
		writer.name("jEnd").value(jEnd);
	}
	
}
