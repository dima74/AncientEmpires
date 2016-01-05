package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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
		this.iStart = JsonHelper.readInt(reader, "iStart");
		this.jStart = JsonHelper.readInt(reader, "jStart");
		this.iEnd = JsonHelper.readInt(reader, "iEnd");
		this.jEnd = JsonHelper.readInt(reader, "jEnd");
	}
	
	@Override
	public void start()
	{
		super.start();
		find();
		campaign.iDrawCampaign.unitMove(this.iStart, this.jStart, this.iEnd, this.jEnd, this);
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
		int absI = this.iEnd + relI;
		int absJ = this.jEnd + relJ;
		if (GameHandler.checkCoord(absI, absJ) && GameHandler.fieldUnits[absI][absJ] == null)
		{
			this.iEnd = absI;
			this.jEnd = absJ;
			return true;
		}
		return false;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("iStart").value(this.iStart);
		writer.name("jStart").value(this.jStart);
		writer.name("iEnd").value(this.iEnd);
		writer.name("jEnd").value(this.jEnd);
	}
	
}
