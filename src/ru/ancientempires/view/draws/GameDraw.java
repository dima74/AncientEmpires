package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import ru.ancientempires.client.Client;
import ru.ancientempires.images.ActionImages;
import ru.ancientempires.images.ArrowsImages;
import ru.ancientempires.images.BigNumberImages;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.CursorImages;
import ru.ancientempires.images.Images;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.images.SmokeImages;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.images.StatusesImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Game;

public abstract class GameDraw
{
	
	public Images Images()
	{
		return Client.client.images;
	}
	
	public CellImages CellImages()
	{
		return Client.client.images.cell;
	}
	
	public UnitImages UnitImages()
	{
		return Client.client.images.unit;
	}
	
	public ActionImages ActionImages()
	{
		return Client.client.images.action;
	}
	
	public SmallNumberImages SmallNumberImages()
	{
		return Client.client.images.smallNumber;
	}
	
	public BigNumberImages BigNumberImages()
	{
		return Client.client.images.bigNumber;
	}
	
	public SparksImages SparksImages()
	{
		return Client.client.images.sparks;
	}
	
	public CursorImages CursorImages()
	{
		return Client.client.images.cursor;
	}
	
	public ArrowsImages ArrowsImages()
	{
		return Client.client.images.arrows;
	}
	
	public StatusesImages StatusesImages()
	{
		return Client.client.images.statuses;
	}
	
	public SmokeImages SmokeImages()
	{
		return Client.client.images.smoke;
	}
	
	public static final float	mapScale	= 2;
	public static final int		A			= Images.get().bitmapSize;
	public static final float	fA			= Images.get().bitmapSize * GameDraw.mapScale;
	public static final float	a			= Images.get().bitmapSize / 24.0f;				// 1/24 A
	
	public static int			h;
	public static int			w;
	public static GameDrawMain	main;
	public static Game			game;
	public static int			iFrame;
	
	public void update()
	{}
	
	public void draw(Canvas canvas)
	{}
	
}
