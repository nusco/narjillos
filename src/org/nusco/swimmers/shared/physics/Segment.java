package org.nusco.swimmers.shared.physics;

public class Segment {

	public final Vector start;
	public final Vector end;

	public Segment(Vector start, Vector end) {
		this.start = start;
		this.end = end;
	}

	private double getLength() {
		return end.minus(start).getLength();
	}

	public double getMinimumDistanceFromPoint(Vector point) {
		if (getLength() < 0.0001)
			return start.getDistanceFrom(point);

		double lengthSquared = getLength() * getLength();

		double t = ((point.x - start.x) * (end.x - start.x) + (point.y - start.y) * (end.y - start.y)) / lengthSquared;

		if (t < 0)
			return start.getDistanceFrom(point);

		if (t > 1)
			return end.getDistanceFrom(point);

		Vector projection = start.plus(end.minus(start).by(t));
		return projection.minus(point).getLength();
	}
}
