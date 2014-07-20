package org.nusco.swimmers.shared.physics;

public class Segment {

	public final Vector startPoint;
	public final Vector vector;

	public Segment(Vector startPoint, Vector vector) {
		this.startPoint = startPoint;
		this.vector = vector;
	}

	private double getLength() {
		return vector.minus(startPoint).getLength();
	}
	
	public double getMinimumDistanceFromPoint(Vector point) {
		if (getLength() < 0.0001)
			return startPoint.getDistanceFrom(point);

		double lengthSquared = getLength() * getLength();

		double t = ((point.x - startPoint.x) * (vector.x - startPoint.x) + (point.y - startPoint.y) * (vector.y - startPoint.y)) / lengthSquared;

		if (t < 0)
			return startPoint.getDistanceFrom(point);

		if (t > 1)
			return vector.getDistanceFrom(point);

		Vector projection = startPoint.plus(vector.minus(startPoint).by(t));
		return projection.minus(point).getLength();
	}
}
