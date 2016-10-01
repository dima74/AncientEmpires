package ru.ancientempires.draws;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Iterator;
import java.util.LinkedHashSet;

import ru.ancientempires.Paints;
import ru.ancientempires.activities.BaseGameActivity;
import ru.ancientempires.draws.inputs.InputMain;
import ru.ancientempires.draws.inputs.InputPlayer;
import ru.ancientempires.draws.onframes.DrawBlackScreen;

public class DrawMain extends BaseDrawMain {

	public InputMain   inputMain;
	public InputPlayer inputPlayer;

	private int          actionY;
	public  DrawAction   action;
	public  DrawInfoNull infoNull;
	public  DrawInfo     info;
	public  DrawInfoMove infoMove;
	public          int     infoY        = 0;
	volatile public boolean isActiveGame = true;

	public DrawCampaign    campaign;
	public DrawBlackScreen blackScreen;

	public boolean isDrawCursor = false;
	public DrawCursor cursorDefault;

	public LinkedHashSet<Draw>[] draws = new LinkedHashSet[DrawLevel.values().length];

	public boolean isBlackScreen = false;
													
	/*
	 * Порядок рисования:
	 * 
	 * клеточки
	 * войны не двигающиеся
	 * 
	 * ? войны двигающиеся
	 * ? искорки
	 * ? увеличение уровня
	 * ? искорки отравления/ауры
	 * ? поле хода/атаки война
	 * ? дым
	 * 
	 * инфо
	 */

	public void add(Draw draw) {
		add(draw, DrawLevel.MIDDLE);
	}

	public void add(Draw draw, DrawLevel level) {
		draws[level.ordinal()].add(draw);
	}

	public void remove(Draw draw) {
		remove(draw, DrawLevel.MIDDLE);
	}

	public void remove(Draw draw, DrawLevel level) {
		draws[level.ordinal()].remove(draw);
	}

	@Override
	public void setVisibleMapSize() {
		visibleMapH = h() - info.h;
		visibleMapW = w();
	}

	{
		main = this;
		// Должно быть до super(), в частности чтобы у всех draws в BaseDrawMain был корректный Draw.main
		// Хотя, казалось бы раз они в BaseDrawMain, то им main и не нужен...
	}

	public DrawMain(BaseGameActivity activity) {
		super(activity);

		action = new DrawAction(this);
		infoNull = new DrawInfoNull(this);
		info = new DrawInfo(this);
		infoMove = new DrawInfoMove(this);
		campaign = new DrawCampaign(this);
		blackScreen = new DrawBlackScreen(this);
		cursorDefault = new DrawCursor(this).setCursor(CursorImages().cursor);

		initOffset();
		focusOnCurrentPlayerCenter();
		offsetY = nextOffsetY;
		offsetX = nextOffsetX;
		actionY = (visibleMapH - action.h) / 2;

		for (DrawLevel level : DrawLevel.values())
			draws[level.ordinal()] = new LinkedHashSet<>();

		add(campaign);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (isDrawCursor)
			cursorDefault.draw(canvas);

		for (DrawLevel level : DrawLevel.values())
			for (Iterator<Draw> iterator = draws[level.ordinal()].iterator(); iterator.hasNext(); ) {
				Draw draw = iterator.next();
				draw.draw(canvas);
				if (draw.isEnd())
					iterator.remove();
			}

		buildingSmokes.draw(canvas);
		canvas.restore();

		if (minOffsetY == maxOffsetY && offsetY > 0)
			canvas.drawRect(0, (maxOffsetY + mapH) * mapScale, w(), visibleMapH, Paints.WHITE);

		canvas.save();
		canvas.translate(0, h() - info.h);
		infoNull.draw(canvas);
		infoMove.draw(canvas);
		canvas.translate(0, infoY);
		info.draw(canvas);
		canvas.restore();

		if (action.isActive()) {
			canvas.save();
			canvas.translate(0, actionY);
			action.draw(canvas);
			canvas.restore();
		}

		if (minOffsetY == maxOffsetY && offsetY > 0)
			canvas.drawRect(0, 0, w(), offsetY * mapScale, Paints.WHITE);

		blackScreen.draw(canvas);
		if (isBlackScreen)
			canvas.drawColor(Color.BLACK);
	}

	@Override
	public boolean isActiveGame() {
		return isActiveGame;
	}

	@Override
	public void touch(float touchY, float touchX) {
		// проверка по иксу немного странная ---
		// ведь GameView (пока что) всегда имеет ширину равный ширине экрана
		if (!isActiveGame || touchY < 0 || touchX < 0 || touchY > h() - info.h || touchX > w())
			return;
		if (action.isActive()) {
			action.touch(touchY - actionY, touchX);
			return;
		}
		super.touch(touchY, touchX);
	}

	@Override
	public void tap(int i, int j) {
		inputMain.tap(i, j);
	}
}
