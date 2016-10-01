package android.graphics;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import ru.ancientempires.framework.MyAssert;

public class BitmapFactory {

	public static Bitmap decodeStream(InputStream is) {
		try {
			return new Bitmap(ImageIO.read(is));
		} catch (IOException e) {
			e.printStackTrace();
			MyAssert.a(false);
			return null;
		}
	}
}
