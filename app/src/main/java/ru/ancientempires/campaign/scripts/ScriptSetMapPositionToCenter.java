package ru.ancientempires.campaign.scripts;

public class ScriptSetMapPositionToCenter extends Script {

	public ScriptSetMapPositionToCenter() {}

	@Override
	public void start() {
		float iCenter = game.h / 2.0f - 0.5f;
		float jCenter = game.w / 2.0f - 0.5f;
		campaign.iDrawCampaign.setMapPosition(iCenter, jCenter);
	}
}
