package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import ru.ancientempires.GameView;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.view.inputs.InputAlgorithmUnitRange;

public class GameDrawRange extends GameDraw
{
	
	private static final Paint	antialiasPaint			= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	whitePaint				= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	srcinWhitePaint			= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	srcinPaint				= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final int	FRAMES_FOR_CELL			= 7;
	private static final int	FRAMES_FOR_CELL_REVERSE	= 3;
	
	public GameDrawRange()
	{
		GameDrawRange.whitePaint.setColor(Color.WHITE);
		
		GameDrawRange.srcinWhitePaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		GameDrawRange.srcinWhitePaint.setColor(Color.WHITE);
		
		GameDrawRange.srcinPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		
		range = new boolean[0][0];
		
		cacheBitmap = Bitmap.createBitmap(GameView.w, GameView.h, Config.ARGB_8888);
		selfCanvas = new Canvas(cacheBitmap);
	}
	
	private Bitmap	cursor;
	private Bitmap	rangeBitmap;
	
	public GameDrawRange setCursor(Bitmap cursor)
	{
		this.cursor = cursor;
		return this;
	}
	
	private int	startI;
	private int	startJ;
	private int	centerY;
	private int	centerX;
	
	private int		rangeStartY;
	private int		rangeStartX;
	private int		rangeEndY;
	private int		rangeEndX;
	private Path	rangePath;
	
	private boolean[][] range;
	
	private float	maxR;							// расстояние в клеточках до максимально удаленной активной (которая будет рисоваться) клеточки
	private int		radiusMin	= GameDraw.A / 2;
	private int		radiusMax;
	private int		radiusStart;
	private int		radiusEnd;
	private int		radius		= radiusMin;		// current
	
	private boolean isDrawing = false;
	
	private int	frameLength;
	private int	frameEnd;
	
	private Bitmap	cacheBitmap;
	private Canvas	selfCanvas;
	
	public void startAnimate(InputAlgorithmUnitRange inputAlgorithmRange)
	{
		int radius = inputAlgorithmRange.radius;
		range = inputAlgorithmRange.visibleRange;
		
		createBitmap(range);
		
		radiusMax = (int) (maxR * GameDraw.A);
		radiusStart = this.radius;
		radiusEnd = radiusMax;
		updateFrames(GameDrawRange.FRAMES_FOR_CELL);
		
		isDrawing = true;
		
		startI = inputAlgorithmRange.startI;
		startJ = inputAlgorithmRange.startJ;
		
		centerY = (int) ((startI + 0.5f) * GameDraw.A);
		centerX = (int) ((startJ + 0.5f) * GameDraw.A);
		
		rangeStartY = (startI - radius) * GameDraw.A;
		rangeStartX = (startJ - radius) * GameDraw.A;
		rangeEndY = (startI + radius + 1) * GameDraw.A;
		rangeEndX = (startJ + radius + 1) * GameDraw.A;
		
		rangePath = new Path();
		rangePath.addRect(rangeStartX, rangeStartY, rangeEndX, rangeEndY, Direction.CW);
	}
	
	private void updateFrames(int framesForCell)
	{
		frameLength = (int) (maxR * framesForCell *
				(Math.abs(radiusEnd - radiusStart)
						/ (float) (radiusMax - radiusMin)));
		MyAssert.a(frameLength > 0);
		frameLength = Math.max(frameLength, 1);
		frameEnd = GameDraw.iFrame + frameLength;
	}
	
	private void createBitmap(boolean[][] range)
	{
		int size = range.length;
		int radius = size / 2;
		
		maxR = 0;
		for (int i = -radius; i <= radius; i++)
			for (int j = -radius; j <= radius; j++)
				if (this.range[radius + i][radius + j])
				{
					float ri = Math.abs(i) + 0.5f;
					float rj = Math.abs(j) + 0.5f;
					maxR = Math.max(maxR, (float) Math.sqrt(ri * ri + rj * rj));
				}
				
		Bitmap bitmap = Bitmap.createBitmap(size * GameDraw.A, size * GameDraw.A, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawRect(0, 0, size * GameDraw.A, size * GameDraw.A, GameDrawRange.srcinPaint);
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (this.range[i][j])
					canvas.drawBitmap(cursor, j * GameDraw.A, i * GameDraw.A, null);
		rangeBitmap = bitmap;
	}
	
	public void startReverseAnimate(InputAlgorithmUnitRange inputAlgorithmRange)
	{
		radiusStart = getCurrentRadius();
		radiusEnd = radiusMin;
		updateFrames(GameDrawRange.FRAMES_FOR_CELL_REVERSE);
		cacheBitmap.eraseColor(Color.TRANSPARENT);
	}
	
	private int getCurrentRadius()
	{
		int frameLeft = Math.max(frameEnd - GameDraw.iFrame, 0);
		int framePass = frameLength - frameLeft;
		int radius = (frameLeft * radiusStart + framePass * radiusEnd) / frameLength;
		return radius;
	}
	
	public void endAnimate()
	{
		isDrawing = false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (!isDrawing)
			return;
			
		radius = getCurrentRadius();
		if (radius == radiusEnd && radiusEnd == radiusMin)
			isDrawing = false;
			
		float canvasTranslateY = GameDraw.main.offsetY; // canvas.getTranslateY();
		float canvasTranslateX = GameDraw.main.offsetX; // canvas.getTranslateX();
		
		cacheBitmap.eraseColor(Color.TRANSPARENT);
		
		selfCanvas.translate(+canvasTranslateX, +canvasTranslateY);
		selfCanvas.save(Canvas.CLIP_SAVE_FLAG);
		selfCanvas.clipPath(rangePath);
		
		selfCanvas.drawCircle(centerX, centerY, radius, GameDrawRange.whitePaint);
		selfCanvas.drawBitmap(rangeBitmap, rangeStartX, rangeStartY, GameDrawRange.srcinPaint);
		
		selfCanvas.restore();
		selfCanvas.translate(-canvasTranslateX, -canvasTranslateY);
		
		canvas.drawBitmap(cacheBitmap, -canvasTranslateX, -canvasTranslateY, GameDrawRange.antialiasPaint);
	}
	
}
