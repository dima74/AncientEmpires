package ru.ancientempires.draws;

import android.graphics.Canvas;

import ru.ancientempires.activity.BaseGameActivity;
import ru.ancientempires.activity.GameActivity;
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

public class Draw
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
	
	public DrawMain main     = DrawMain.main;
	public Game     game     = BaseGameActivity.activity.game;
	public float    mapScale = 2;
	public int      A        = Images().bitmapSize;
	public float    fA       = Images().bitmapSize * mapScale;
	public float    a        = A / 24.0f;

	public int h()
	{
		return BaseGameActivity.activity.getView().h;
	}
	
	public int w()
	{
		return BaseGameActivity.activity.getView().w;
	}
	
	public int iFrame()
	{
		return main.iFrame;
	}
	
	public void postUpdateCampaign()
	{
		GameActivity.activity.postUpdateCampaign();
	}
	
	public boolean isEnd()
	{
		return false;
	}
	
	public void draw(Canvas canvas)
	{}
	
}
