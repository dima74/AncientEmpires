package ru.ancientempires.bonuses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.util.Arrays;
import java.util.List;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;

public abstract class BonusCreator {
	
	/*
		После какого-либо действия (атака/перемещение) войн может дать бонусы некоторым другим войнам
		Эти бонусы могут быть как положительные так и отрицательные
		Нам нужно отобразить факт получения бонуса, 
			поэтому нужен список войнов которые получили бонусы с информацией о полученных бонусах
		//Для простоты будем считать, что любой войн получает не более одного бонуса
		В возвращаемом массиве войны могут повторяться, 
			таким образом один войн может получить несколько бонусов
	*/

	public static List<Class<? extends BonusCreator>> classes = Arrays.asList(
			BonusCreatorWisp.class,
			BonusCreatorDireWolf.class);

	public int ordinal() {
		return BonusCreator.classes.indexOf(getClass());
	}

	public BonusCreate[] applyBonusesAfterAttack(Game game, Unit unit, Unit targetUnit) {
		return new BonusCreate[0];
	}

	public BonusCreate[] applyBonusesAfterMove(Game game, Unit unit) {
		return new BonusCreate[0];
	}

	public void saveJSON(JsonObject object, JsonSerializationContext context) {}

	public void loadJSON(JsonObject object, Rules rules, JsonDeserializationContext context) {}

	public static Bonus copy(Bonus bonus, Game game) {
		try {
			return game.getLoaderInfo().fromJson(bonus.toJson(), Bonus.class);
		} catch (Exception e) {
			e.printStackTrace();
			MyAssert.a(false);
			return null;
		}
	}
}
