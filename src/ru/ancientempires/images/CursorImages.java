package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.images.bitmaps.FewBitmaps;

public class CursorImages extends IImages
{
	
	public Bitmap	cursorWay;
	public Bitmap	cursorAttack;
	public Bitmap	cursorMixed;
	
	public final FewBitmaps	cursor				= new FewBitmaps();
	public final FewBitmaps	cursorPointerAttack	= new FewBitmaps();
	public final FewBitmaps	cursorPointerWay	= new FewBitmaps();
	
	public int	cursorH;
	public int	cursorW;
	
	@Override
	public void preload(ImagesLoader loader) throws IOException
	{
		cursorWay = loader.loadImage("cursor_way.png");
		cursorAttack = loader.loadImage("cursor_attack.png");
		cursorMixed = loader.loadImage("cursor_mixed.png");
		cursor.setBitmaps(loader, new String[]
		{
				"cursor_down.png",
				"cursor_up.png"
		});
		cursorPointerAttack.setBitmaps(loader, new String[]
		{
				"cursor_pointer_attack_0.png",
				"cursor_pointer_attack_1.png",
				"cursor_pointer_attack_2.png"
		});
		cursorPointerWay.setBitmaps(loader, new String[]
		{
				"cursor_pointer_way.png"
		});
		
		cursorH = cursor.getBitmap().getHeight();
		cursorW = cursor.getBitmap().getWidth();
	}
	
}
