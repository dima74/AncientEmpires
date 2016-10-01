package android.graphics;

public class Paint {

	public static final int ANTI_ALIAS_FLAG = 0x01;

	public int color;

	public Paint() {}

	public Paint(int flags) {}

	public void setColor(int color) {
		this.color = color;
	}

}
