package ru.ancientempires.view.draws;

import ru.ancientempires.helpers.Point;
import ru.ancientempires.images.UnitBitmap;
import android.graphics.Canvas;

public class GameDrawUnitMove extends GameDraw
{
	
	private static final int	FRAMES_FOR_CELL	= 7;
	
	private UnitBitmap			unitBitmap;
	
	private int					i;
	private int					j;
	
	private Point[]				ways;
	private int					frameStart;
	private int					frameLength;
	
	private boolean				isMoving		= false;
	
	public GameDrawUnitMove(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void init(int i, int j)
	{
		this.unitBitmap = this.gameDraw.gameDrawUnit.extractUnit(i, j);
		this.ways = new Point[]
		{
				new Point(i, j)
		};
	}
	
	public void start(Point[] ways)
	{
		this.isMoving = true;
		this.ways = ways;
		this.frameLength = (ways.length - 1) * GameDrawUnitMove.FRAMES_FOR_CELL;
		this.frameStart = this.gameDraw.iFrame;
	}
	
	public void end()
	{
		this.gameDraw.gameDrawUnit.update(this.gameDraw.game);
		this.unitBitmap = null;
		this.isMoving = false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (this.unitBitmap != null)
		{
			if (!this.isMoving)
			{
				drawUnit(canvas, 0);
				return;
			}
			int framePass = this.gameDraw.iFrame - this.frameStart;
			if (framePass >= this.frameLength)
			{
				drawUnit(canvas, this.ways.length - 1);
				end();
				return;
			}
			int i = framePass / GameDrawUnitMove.FRAMES_FOR_CELL;
			int framePassPart = framePass - i * GameDrawUnitMove.FRAMES_FOR_CELL;
			int frameLeftPart = GameDrawUnitMove.FRAMES_FOR_CELL - framePassPart;
			int y = (frameLeftPart * this.ways[i].i + framePassPart * this.ways[i + 1].i) * GameDraw.A / GameDrawUnitMove.FRAMES_FOR_CELL;
			int x = (frameLeftPart * this.ways[i].j + framePassPart * this.ways[i + 1].j) * GameDraw.A / GameDrawUnitMove.FRAMES_FOR_CELL;
			drawUnit(canvas, y, x);
		}
	}
	
	private void drawUnit(Canvas canvas, int y, int x)
	{
		canvas.drawBitmap(this.unitBitmap.getBitmap(), x, y, null);
		canvas.drawBitmap(this.unitBitmap.textBitmap, x, y, null);
	}
	
	private void drawUnit(Canvas canvas, int i)
	{
		drawUnit(canvas, this.ways[i].i * GameDraw.A, this.ways[i].j * GameDraw.A);
	}
	
}
