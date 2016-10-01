package ru.ancientempires.actions;

import org.atteo.classindex.IndexSubclasses;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

import ru.ancientempires.actions.result.ActionResult;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.AbstractGameHandler;
import ru.ancientempires.model.Game;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableData;

@IndexSubclasses
public abstract class Action extends AbstractGameHandler implements SerializableData {

	public Action() {
		setGame(null);
	}

	public boolean changesGame() {
		return true;
	}

	/*
	Сейчас action'ы которые относятся к кампании не сохраняются, вместо этого после ScriptEnableActiveGame сохраняется snapshot.
	Может быть стоит сделать чтобы эти action'ы сохранялись, единственная проблема возникнет если игра прервётся во время кампании,
	ну наверно можно просто доавить в saveInfo номер последнего action'а который нужно выполнять
	public boolean isCampaign()
	{
		return false;
	}
	*/

	public ActionResult perform(Game game) {
		performBase(game);
		return null;
	}

	public final void performBase(Game game) {
		try {
			checkBase(game);
			performQuick();
			if (game.isMain)
				Client.commit(this);
		} catch (Exception e) {
			MyAssert.a(false);
			e.printStackTrace();
		}
	}

	public final boolean checkBase(Game game) {
		setGame(game);
		boolean successfully = /*isCampaign() || */check();
		if (!successfully) {
			MyAssert.a(false);
			check();
		}
		return successfully;
	}

	public boolean check() {
		return true;
	}

	public abstract void performQuick();

	public final void performQuickBase(Game game) {
		setGame(game);
		game.allActions.add(this);
		performQuick();
	}

	@Override
	public String toString() {
		String s = this.getClass().getSimpleName().replace("Action", "") + " ";
		if (this instanceof ActionFromTo) {
			ActionFromTo thisCast = (ActionFromTo) this;
			s += coordinates(thisCast.i, thisCast.j)
					+ "->"
					+ coordinates(thisCast.targetI, thisCast.targetJ);
		} else if (this instanceof ActionFrom) {
			ActionFrom thisCast = (ActionFrom) this;
			s += coordinates(thisCast.i, thisCast.j);
		} else if (this instanceof ActionTo) {
			ActionTo action = (ActionTo) this;
			s += coordinates(action.targetI, action.targetJ);
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			toData(new DataOutputStream(baos));
			s += " " + Arrays.toString(baos.toByteArray());
		} catch (Exception e) {
			MyAssert.a(false);
			e.printStackTrace();
		}

		// s = game.hashCode() + " " + s;
		return s;
	}

	private String coordinates(int i, int j) {
		return "(" + i + "," + j + ")";
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception {
		ru.ancientempires.serializable.SerializableDataHelper.toData(output, this);
	}

	public Action fromData(DataInputStream input, LoaderInfo info) throws Exception {
		return this;
	}
}
