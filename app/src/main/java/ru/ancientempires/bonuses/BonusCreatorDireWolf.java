package ru.ancientempires.bonuses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.tasks.TaskRemoveBonus;

public class BonusCreatorDireWolf extends BonusCreator
{
	
	/*
		Здесь один бонус, у BonusCreatorWisp может быть несколько, 
			мне кажется можно и у виспа сделать один
	*/

	public Bonus bonus;

	public BonusCreatorDireWolf()
	{}

	public BonusCreatorDireWolf(Bonus bonus)
	{
		this.bonus = bonus;
	}

	@Override
	public BonusCreate[] applyBonusesAfterAttack(Game game, Unit unit, Unit targetUnit)
	{
		Bonus bonus = copy(this.bonus, game);
		targetUnit.addBonus(bonus);
		new TaskRemoveBonus(game)
				.setUnit(targetUnit)
				.setBonus(bonus)
				.setTurn(game.numberPlayers())
				.register();
		return new BonusCreate[]
				{
						new BonusCreate(targetUnit, bonus)
				};
	}

	@Override
	public void saveJSON(JsonObject object, JsonSerializationContext context)
	{
		object.add("bonus", context.serialize(bonus));
	}

	@Override
	public void loadJSON(JsonObject object, Rules rules, JsonDeserializationContext context)
	{
		bonus = context.deserialize(object.get("bonus"), Bonus.class);
	}

}
