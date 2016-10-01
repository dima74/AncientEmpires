package android.graphics;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import ru.ancientempires.framework.MyAssert;

public class Canvas {

	private Graphics2D g;
	private int        h;
	private int        w;

	public Canvas(Graphics2D g) {
		this.g = g;
		h = g.getClipBounds().height;
		w = g.getClipBounds().width;
	}

	public Canvas(Bitmap bitmap) {
		g = bitmap.image.createGraphics();
		h = bitmap.image.getHeight();
		w = bitmap.image.getWidth();
	}

	public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
		g.drawImage(bitmap.image, (int) left, (int) top, null);
	}

	public void drawRect(float left, float top, float right, float bottom, Paint paint) {
		java.awt.Color saveColor = g.getColor();
		int color = paint.color;
		if (color != 0)
			g.setColor(new java.awt.Color(paint.color, true));

		int x = (int) left;
		int y = (int) top;
		int w = (int) (right - left);
		int h = (int) (bottom - top);
		g.fillRect(x, y, w, h);

		if (color != 0)
			g.setColor(saveColor);
	}

	public void drawLines(float[] wayPoints, Paint lINE_PAINT) {
		MyAssert.a(false);
	}

	public void translate(float dx, float dy) {
		g.translate(dx, dy);
	}

	public void scale(float sx, float sy) {
		g.scale(sx, sy);
	}

	public final void scale(float sx, float sy, float px, float py) {
		translate(px, py);
		scale(sx, sy);
		translate(-px, -py);
	}

	public void drawColor(int color) {
		java.awt.Color saveColor = g.getColor();
		g.setColor(new java.awt.Color(color, true));
		g.fillRect(0, 0, w, h);
		g.setColor(saveColor);
	}

	private ArrayList<AffineTransform> transforms = new ArrayList<>();

	public void save() {
		transforms.add(g.getTransform());
	}

	public void restore() {
		AffineTransform t = transforms.get(transforms.size() - 1);
		transforms.remove(transforms.size() - 1);
		g.setTransform(t);
	}
}
