package android.graphics;

import java.awt.image.BufferedImage;

public class Bitmap {

	public enum Config {
		ARGB_8888
	}

	public BufferedImage image;

	public Bitmap(BufferedImage image) {
		this.image = image;
	}

	public int getHeight() {
		return image.getHeight();
	}

	public int getWidth() {
		return image.getWidth();
	}

	public static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
		return src;
	}

	public static Bitmap createBitmap(int width, int height, Config config) {
		return new Bitmap(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
	}

	public static Bitmap createBitmap(Bitmap source, int x, int y, int w, int h) {
		return new Bitmap(source.image.getSubimage(x, y, w, h));
	}

}
