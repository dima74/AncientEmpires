package ru.ancientempires;

import java.util.Objects;

import ru.ancientempires.campaign.points.AbstractPoint;

public class Point {

	public static final Point NULL_POINT = new Point(-1, -1);

	public int i, j;

	public Point(int i, int j) {
		this.i = i;
		this.j = j;
	}

	public Point(AbstractPoint point) {
		this(point.getI(), point.getJ());
	}

	public Point(float i, float j) {
		this(Math.round(i), Math.round(j));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Point point = (Point) o;
		return i == point.i &&
				j == point.j;
	}

	@Override
	public int hashCode() {
		return Objects.hash(i, j);
	}
}
