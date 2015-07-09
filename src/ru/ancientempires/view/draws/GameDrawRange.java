package ru.ancientempires.view.draws;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.view.algortihms.InputAlgorithmUnitRange;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

public class GameDrawRange extends GameDraw
{
	
	private static final Paint	antialiasPaint			= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	whitePaint				= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	srcinWhitePaint			= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	srcinPaint				= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final int	FRAMES_FOR_CELL			= 7;
	private static final int	FRAMES_FOR_CELL_REVERSE	= 3;
	
	public GameDrawRange(GameDrawMain gameDraw)
	{
		super(gameDraw);
		
		GameDrawRange.whitePaint.setColor(Color.WHITE);
		
		GameDrawRange.srcinWhitePaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		GameDrawRange.srcinWhitePaint.setColor(Color.WHITE);
		
		GameDrawRange.srcinPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		
		this.range = new boolean[0][0];
	}
	
	private Bitmap	cursor;
	private Bitmap	rangeBitmap;
	
	public GameDrawRange setCursor(Bitmap cursor)
	{
		this.cursor = cursor;
		return this;
	}
	
	private int			startI;
	private int			startJ;
	private int			centerY;
	private int			centerX;
	
	private int			rangeStartY;
	private int			rangeStartX;
	private int			rangeEndY;
	private int			rangeEndX;
	private Path		rangePath;
	
	private boolean[][]	range;
	
	private float		maxR;							// расстояние в клеточках до максимально удаленной активной (которая будет рисоваться) клеточки
	private int			radiusMin	= GameDraw.A / 2;
	private int			radiusMax;
	private int			radiusStart;
	private int			radiusEnd;
	private int			radius		= this.radiusMin;	// current
														
	private boolean		isDrawing	= false;
	
	private int			frameLength;
	private int			frameEnd;
	
	private Bitmap		cacheBitmap;
	private Canvas		selfCanvas;
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		this.cacheBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		this.selfCanvas = new Canvas(this.cacheBitmap);
	}
	
	public void startAnimate(InputAlgorithmUnitRange inputAlgorithmRange)
	{
		int radius = inputAlgorithmRange.radius;
		this.range = inputAlgorithmRange.visibleRange;
		
		createBitmap(this.range);
		
		this.radiusMax = (int) (this.maxR * GameDraw.A);
		this.radiusStart = this.radius;
		this.radiusEnd = this.radiusMax;
		updateFrames(GameDrawRange.FRAMES_FOR_CELL);
		
		this.isDrawing = true;
		
		this.startI = inputAlgorithmRange.startI;
		this.startJ = inputAlgorithmRange.startJ;
		
		this.centerY = (int) ((this.startI + 0.5f) * GameDraw.A);
		this.centerX = (int) ((this.startJ + 0.5f) * GameDraw.A);
		
		this.rangeStartY = (this.startI - radius) * GameDraw.A;
		this.rangeStartX = (this.startJ - radius) * GameDraw.A;
		this.rangeEndY = (this.startI + radius + 1) * GameDraw.A;
		this.rangeEndX = (this.startJ + radius + 1) * GameDraw.A;
		
		this.rangePath = new Path();
		this.rangePath.addRect(this.rangeStartX, this.rangeStartY, this.rangeEndX, this.rangeEndY,
				Direction.CW);
	}
	
	private void updateFrames(int framesForCell)
	{
		this.frameLength = (int) (this.maxR * framesForCell *
							(Math.abs(this.radiusEnd - this.radiusStart)
							/ (float) (this.radiusMax - this.radiusMin)));
		MyAssert.a(this.frameLength > 0);
		this.frameLength = Math.max(this.frameLength, 1);
		this.frameEnd = this.gameDraw.iFrame + this.frameLength;
	}
	
	private void createBitmap(boolean[][] range)
	{
		int size = range.length;
		int radius = size / 2;
		
		this.maxR = 0;
		for (int i = -radius; i <= radius; i++)
			for (int j = -radius; j <= radius; j++)
				if (this.range[radius + i][radius + j])
				{
					float ri = Math.abs(i) + 0.5f;
					float rj = Math.abs(j) + 0.5f;
					this.maxR = Math.max(this.maxR, (float) Math.sqrt(ri * ri + rj * rj));
				}
		
		Bitmap bitmap = Bitmap.createBitmap(size * GameDraw.A, size * GameDraw.A, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawRect(0, 0, size * GameDraw.A, size * GameDraw.A, GameDrawRange.srcinPaint);
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (this.range[i][j])
					canvas.drawBitmap(this.cursor, j * GameDraw.A, i * GameDraw.A, null);
		this.rangeBitmap = bitmap;
	}
	
	public void startReverseAnimate(InputAlgorithmUnitRange inputAlgorithmRange)
	{
		this.radiusStart = getCurrentRadius();
		this.radiusEnd = this.radiusMin;
		updateFrames(GameDrawRange.FRAMES_FOR_CELL_REVERSE);
		this.cacheBitmap.eraseColor(Color.TRANSPARENT);
	}
	
	private int getCurrentRadius()
	{
		int frameLeft = Math.max(this.frameEnd - this.gameDraw.iFrame, 0);
		int framePass = this.frameLength - frameLeft;
		int radius = (frameLeft * this.radiusStart + framePass * this.radiusEnd) / this.frameLength;
		return radius;
	}
	
	public void endAnimate()
	{
		this.isDrawing = false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (!this.isDrawing)
			return;
		
		this.radius = getCurrentRadius();
		if (this.radius == this.radiusEnd && this.radiusEnd == this.radiusMin)
			this.isDrawing = false;
		
		int canvasTranslateY = this.gameDraw.offsetY; // canvas.getTranslateY();
		int canvasTranslateX = this.gameDraw.offsetX; // canvas.getTranslateX();
		
		this.cacheBitmap.eraseColor(Color.TRANSPARENT);
		
		this.selfCanvas.translate(+canvasTranslateX, +canvasTranslateY);
		this.selfCanvas.save(Canvas.CLIP_SAVE_FLAG);
		this.selfCanvas.clipPath(this.rangePath);
		
		this.selfCanvas.drawCircle(this.centerX, this.centerY, this.radius, GameDrawRange.whitePaint);
		this.selfCanvas.drawBitmap(this.rangeBitmap, this.rangeStartX, this.rangeStartY, GameDrawRange.srcinPaint);
		
		this.selfCanvas.restore();
		this.selfCanvas.translate(-canvasTranslateX, -canvasTranslateY);
		
		canvas.drawBitmap(this.cacheBitmap, -canvasTranslateX, -canvasTranslateY, GameDrawRange.antialiasPaint);
	}
	
}
