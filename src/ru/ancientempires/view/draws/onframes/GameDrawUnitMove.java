package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.helpers.Point;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;

public class GameDrawUnitMove extends GameDrawOnFrames
{
	
	private static final int	FRAMES_FOR_CELL	= 7;
	
	private UnitBitmap			unitBitmap;
	private int					i, j;
	private Point[]				ways;
	private boolean				isStay			= false;
	
	public GameDrawUnitMove(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void init(int i, int j)
	{
		this.unitBitmap = this.gameDraw.gameDrawUnit.extractUnit(i, j);
		this.i = i;
		this.j = j;
		this.isStay = true;
	}
	
	public void start(Point[] ways, ActionResult result)
	{
		this.isStay = false;
		this.ways = ways;
		animate(0, (ways.length - 1) * GameDrawUnitMove.FRAMES_FOR_CELL);
		if (result != null)
			this.gameDraw.gameDrawUnitMoveEnd.start(result, this.frameLength);
	}
	
	public void destroy()
	{
		if (!this.isStay && this.unitBitmap != null)
			this.gameDraw.gameDrawUnit.updateOneUnit(this.ways[this.ways.length - 1]);
		this.unitBitmap = null;
		this.isStay = false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (this.isStay)
			drawUnit(canvas, this.i * GameDraw.A, this.j * GameDraw.A);
		super.draw(canvas);
		if (!this.isDrawing)
			return;
		
		int framePass = this.gameDraw.iFrame - this.frameStart;
		int i = framePass / GameDrawUnitMove.FRAMES_FOR_CELL;
		int framePassPart = framePass - i * GameDrawUnitMove.FRAMES_FOR_CELL;
		int frameLeftPart = GameDrawUnitMove.FRAMES_FOR_CELL - framePassPart;
		int y = (frameLeftPart * this.ways[i].i + framePassPart * this.ways[i + 1].i) * GameDraw.A / GameDrawUnitMove.FRAMES_FOR_CELL;
		int x = (frameLeftPart * this.ways[i].j + framePassPart * this.ways[i + 1].j) * GameDraw.A / GameDrawUnitMove.FRAMES_FOR_CELL;
		drawUnit(canvas, y, x);
		
		if (this.gameDraw.iFrame == this.frameEnd - 1)
			destroy();
	}
	
	private void drawUnit(Canvas canvas, int y, int x)
	{
		canvas.drawBitmap(this.unitBitmap.getBitmap(), x, y, null);
		canvas.drawBitmap(this.unitBitmap.textBitmap, x, y, null);
	}
	
}
