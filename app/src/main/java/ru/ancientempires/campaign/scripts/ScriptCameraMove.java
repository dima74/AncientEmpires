package ru.ancientempires.campaign.scripts;

public class ScriptCameraMove extends ScriptOnePoint
{
	
	public ScriptCameraMove()
	{}
	
	public ScriptCameraMove(Object... point)
	{
		super(point);
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.cameraMove(this);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
}
