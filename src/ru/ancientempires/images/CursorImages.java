package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.images.bitmaps.FewBitmaps;

public class CursorImages
{
	
	public static Bitmap	cursorWay;
	public static Bitmap	cursorAttack;
	public static Bitmap	cursorMixed;
	
	public static final FewBitmaps	cursor				= new FewBitmaps();
	public static final FewBitmaps	cursorPointerAttack	= new FewBitmaps();
	public static final FewBitmaps	cursorPointerWay	= new FewBitmaps();
	
	public static int	cursorH;
	public static int	cursorW;
	
	public static void preload(String path) throws IOException
	{
		CursorImages.cursorWay = BitmapHelper.getResizeBitmap(path + "cursor_way.png");
		CursorImages.cursorAttack = BitmapHelper.getResizeBitmap(path + "cursor_attack.png");
		CursorImages.cursorMixed = BitmapHelper.getResizeBitmap(path + "cursor_mixed.png");
		CursorImages.cursor.setBitmaps(path, new String[]
		{
				"cursor_down.png",
				"cursor_up.png"
		});
		CursorImages.cursorPointerAttack.setBitmaps(path, new String[]
		{
				"cursor_pointer_attack_0.png",
				"cursor_pointer_attack_1.png",
				"cursor_pointer_attack_2.png"
		});
		CursorImages.cursorPointerWay.setBitmaps(path, new String[]
		{
				"cursor_pointer_way.png"
		});
		
		CursorImages.cursorH = CursorImages.cursor.getBitmap().getHeight();
		CursorImages.cursorW = CursorImages.cursor.getBitmap().getWidth();
	}
	
}
