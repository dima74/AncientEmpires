package ru.ancientempires.bonuses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.util.ArrayList;

import ru.ancientempires.actions.CheckerUnit;
import ru.ancientempires.helpers.ActionHelper;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Range;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.tasks.TaskRemoveBonus;

public class BonusCreatorWisp extends BonusCreator {

	public Range   range;
	public Bonus[] bonuses;

	public BonusCreatorWisp() {}

	public BonusCreatorWisp(Range range, Bonus... bonuses) {
		this.range = range;
		this.bonuses = bonuses;
	}

	@Override
	public BonusCreate[] applyBonusesAfterMove(final Game game, final Unit unit) {
		final ArrayList<BonusCreate> creates = new ArrayList<>();
		new ActionHelper(game).forUnitInRange(unit.i, unit.j, range, new CheckerUnit() {
			@Override
			public boolean check(Unit targetUnit) {
				if (targetUnit != null && unit.player == targetUnit.player)
					for (Bonus bonus0 : bonuses) {
						Bonus bonus = copy(bonus0, game);
						creates.add(new BonusCreate(targetUnit, bonus));
						targetUnit.addBonus(bonus);
						new TaskRemoveBonus(game)
								.setUnit(targetUnit)
								.setBonus(bonus)
								.setTurn(game.numberPlayers())
								.register();
					}
				return false;
			}
		});
		return creates.toArray(new BonusCreate[creates.size()]);
	}

	@Override
	public void saveJSON(JsonObject object, JsonSerializationContext context) {
		object.addProperty("range", range.name);
		object.add("bonuses", context.serialize(bonuses));
	}

	@Override
	public void loadJSON(JsonObject object, Rules rules, JsonDeserializationContext context) {
		range = rules.getRange(object.get("range").getAsString());
		bonuses = context.deserialize(object.get("bonuses"), Bonus[].class);
	}
}
