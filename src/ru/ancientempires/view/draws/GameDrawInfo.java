package ru.ancientempires.view.draws;

import ru.ancientempires.images.BigNumberImages;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.Images;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.view.GameView;
import ru.ancientempires.view.draws.onframes.GameDrawDecreaseHealth;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameDrawInfo extends GameDraw
{
	
	private static final Paint	paint	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color1	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color2	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color3	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color4	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color5	= new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private Bitmap				backgroundBitmap;
	
	public int					a;
	private int					h;
	private int					w;
	private float				mW;
	private int					color;
	
	private Bitmap				goldBitmap;
	private Bitmap				amountBitmap;
	
	public GameDrawInfo(GameDrawMain gameDraw)
	{
		super(gameDraw);
		
		this.w = GameView.w;
		this.h = gameDraw.gameDrawInfoH;
		
		this.backgroundBitmap = Bitmap.createBitmap(this.w, this.h, Config.ARGB_8888);
		Canvas canvas = new Canvas(this.backgroundBitmap);
		
		GameDrawInfo.color1.setColor(0xFFAFB7AB);
		GameDrawInfo.color2.setColor(0xFF6D7581);
		GameDrawInfo.color3.setColor(0xFF434A64);
		GameDrawInfo.color4.setColor(0xFF242A45);
		GameDrawInfo.color5.setColor(0xFF12142F);
		
		this.a = 2;// GameDraw.A / 24;
		this.mW = this.w - this.a * 7 - GameDraw.A;
		
		GameDrawInfo.paint.setStrokeWidth(1);
		GameDrawInfo.color1.setStrokeWidth(this.a);
		GameDrawInfo.color2.setStrokeWidth(this.a);
		GameDrawInfo.color3.setStrokeWidth(this.a);
		GameDrawInfo.color4.setStrokeWidth(this.a);
		GameDrawInfo.color5.setStrokeWidth(this.a);
		
		drawLeftPart(canvas, this.h, this.mW);
		drawRightPart(canvas, this.h, this.w, this.mW - this.a);
	}
	
	private void drawLeftPart(Canvas canvas, float h, float w)
	{
		float nh = h / this.a;
		float nw = w / this.a;
		canvas.drawRect(0, 0, w, h, GameDrawInfo.color3);
		canvas.drawRect(this.a, this.a, w - this.a, h - this.a, GameDrawInfo.color1);
		canvas.drawRect(3 * this.a, 3 * this.a, w - 3 * this.a, h - 3 * this.a, GameDrawInfo.color3);
		
		drawLine(canvas, 3, 3.5f, 6, 3.5f, GameDrawInfo.color5, true, true, true, nh, nw);
		drawLine(canvas, 4, 4.5f, 6, 4.5f, GameDrawInfo.color4, false, true, true, nh, nw);
		drawLine(canvas, 4, 5.5f, 6, 5.5f, GameDrawInfo.color4, false, true, true, nh, nw);
		drawLine(canvas, 3, 6.5f, 7, 6.5f, GameDrawInfo.color1, true, true, true, nh, nw);
		drawLine(canvas, 7.5f, 4, 7.5f, 8, GameDrawInfo.color2, false, false, true, nh, nw);
		drawLine(canvas, 3.5f, 8, 3.5f, nw - 8, GameDrawInfo.color2, false, false, false, nh, nw);
		drawLine(canvas, 8, 4.5f, nh - 8, 4.5f, GameDrawInfo.color5, false, false, true, nh, nw);
		drawLine(canvas, 4.5f, 8, 4.5f, nw - 8, GameDrawInfo.color5, false, true, false, nh, nw);
		
		canvas.drawRect(5 * this.a, 8 * this.a, w - 5 * this.a, h - 8 * this.a, GameDrawInfo.color4);
		canvas.drawRect(8 * this.a, 5 * this.a, w - 8 * this.a, h - 5 * this.a, GameDrawInfo.color4);
	}
	
	private void drawLine(Canvas canvas, float i1, float j1, float i2, float j2, Paint paint,
			boolean axial, boolean vertical, boolean horizontal, float nh, float nw)
	{
		canvas.drawLine(this.a * j1, this.a * i1, this.a * j2, this.a * i2, paint);
		if (axial)
			drawLine(canvas, j1, i1, j2, i2, paint, false, vertical, horizontal, nh, nw);
		if (vertical)
			drawLine(canvas, nh - i1, j1, nh - i2, j2, paint, false, false, horizontal, nh, nw);
		if (horizontal)
			drawLine(canvas, i1, nw - j1, i2, nw - j2, paint, false, false, false, nh, nw);
	}
	
	private void drawRightPart(Canvas canvas, float h, float w, float mW)
	{
		canvas.drawRect(mW, 0, w, h, GameDrawInfo.color3);
		canvas.drawRect(mW + this.a, this.a, w - this.a, h - this.a, GameDrawInfo.color1);
		float a3 = this.a * 3;
		canvas.drawRect(mW + a3, a3, w - a3, h - a3, GameDrawInfo.color3);
	}
	
	private Bitmap	bitmap;
	private int		defense;
	
	@Override
	public boolean update(Game game)
	{
		updateCell(game);
		updatePlayer(game.currentPlayer);
		return false;
	}
	
	public void updateCell(Game game)
	{
		final Cell cell = game.map.getField()[game.currentPlayer.cursorI][game.currentPlayer.cursorJ];
		this.bitmap = CellImages.getCellBitmap(cell, false);
		this.defense = cell.type.defense;
	}
	
	public void updatePlayer(Player player)
	{
		this.color = player.color.showColor;
		this.goldBitmap = GameDrawDecreaseHealth.createBitmap(BigNumberImages.images, 0, player.gold);
		this.amountBitmap = GameDrawDecreaseHealth.createBitmap(BigNumberImages.images, 0, player.units.size());
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(this.backgroundBitmap, 0, 0, null);
		
		if (this.bitmap != null)
		{
			// изображение клеточки
			canvas.drawBitmap(this.bitmap, this.mW + this.a * 3, this.a * 4, null);
			
			SmallNumberImages images = SmallNumberImages.images;
			// и ее защиты
			int x = this.w - this.a * 4 - images.w;
			final int y = this.h - this.a * 4 - images.h;
			int defense = this.defense;
			do
			{
				canvas.drawBitmap(images.getBitmap(defense % 10), x, y, null);
				defense /= 10;
				x -= images.w;
			}
			while (defense > 0);
			canvas.drawBitmap(SmallNumberImages.defenceBitmap, x, y, null);
		}
		
		// градиент по цвет игрока
		int amn = 8;
		for (int i = 0; i < amn; i++)
		{
			GameDrawInfo.paint.setColor(this.color & 0x00FFFFFF | 0xFF * (amn - i) / amn << 24);
			
			float y1 = (5 + i) * this.a;
			float x1 = (i < 3 ? 8 : 5) * this.a;
			float y2 = (5 + i) * this.a;
			float x2 = this.mW - (i < 3 ? 8 : 5) * this.a - .5f;
			for (int k = 0; k < this.a; k++)
				canvas.drawLine(x1, y1 + k + 1, x2, y2 + k + 1, GameDrawInfo.paint);
		}
		
		int xGold = (int) (this.mW * .075f);
		int xUnits = (int) (this.mW * .5f);
		int yGold = (this.h - Images.amountGoldH) / 2;
		int yUnits = (this.h - Images.amountUnitsH) / 2;
		canvas.drawBitmap(Images.amountGold, xGold, yGold, null);
		canvas.drawBitmap(Images.amountUnits, xUnits, yUnits, null);
		canvas.drawBitmap(this.goldBitmap, xGold + Images.amountGoldW + this.a, yGold + 1, null);
		canvas.drawBitmap(this.amountBitmap, xUnits + Images.amountUnitsW + this.a + this.a, yUnits, null);
	}
	
}
