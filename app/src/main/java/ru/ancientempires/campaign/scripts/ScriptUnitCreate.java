package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignUnitCreate;
import ru.ancientempires.helpers.UnitHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptUnitCreate extends ScriptOnePoint {

	private UnitType unitType;
	private Player   player;

	public ScriptUnitCreate() {}

	public ScriptUnitCreate(int player, String unitType, Object... point) {
		super(point);
		this.player = getGame().players[player];
		this.unitType = "KING".equals(unitType) ? new UnitHelper(getGame()).getKingType(this.player) : getGame().rules.getUnitType(unitType);
	}

	@Override
	public void start() {
		campaign.iDrawCampaign.unitCreate(i(), j(), unitType, player, this);
	}

	@Override
	public void performAction() {
		new ActionCampaignUnitCreate().setType(unitType).setPlayer(player.ordinal).setIJ(i(), j()).perform(game);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("unitType", unitType.getName());
		object.addProperty("player", player.getNumber());
		return object;
	}

	public ScriptUnitCreate fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		unitType = UnitType.newInstance(object.get("unitType").getAsString(), info);
		player = Player.newInstance(object.get("player").getAsInt(), info);
		return this;
	}
}
