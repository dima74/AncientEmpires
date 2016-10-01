package ru.ancientempires;

import android.view.SurfaceHolder;

import ru.ancientempires.activities.BaseGameActivity;
import ru.ancientempires.activities.GameActivity;

public class GameView extends BaseView implements SurfaceHolder.Callback {

	public GameView(BaseGameActivity activity) {
		super(activity);
	}

	@Override
	public GameThread createThread() {
		return new GameThread((GameActivity) activity, getHolder());
	}
}
