package org.nusco.narjillos.shared.physics;

/**
 * A vector that has a specific origin in the plane.
 */
public class Segment {

	private final Vector startPoint;
	private final Vector vector;

	public Segment(Vector startPoint, Vector vector) {
		this.startPoint = startPoint;
		this.vector = vector;
	}

	public Vector getStartPoint() {
		return startPoint;
	}

	public Vector getVector() {
		return vector;
	}

	public double getLength() {
		return vector.getLength();
	}
	
	public double getMinimumDistanceFromPoint(Vector point) {
		if (vector.isZero())
			return startPoint.getDistanceFrom(point);
		
		double length = getLength();
		
		if (length < 0.0001)
			return startPoint.getDistanceFrom(point);

		double lengthSquared = length * length;

		double t = ((point.x - startPoint.x) * (vector.x - startPoint.x) + (point.y - startPoint.y) * (vector.y - startPoint.y)) / lengthSquared;

		if (t < 0)
			return startPoint.getDistanceFrom(point);

		if (t > 1)
			return vector.getDistanceFrom(point);

		Vector projection = startPoint.plus(vector.minus(startPoint).by(t));
		return projection.minus(point).getLength();
	}

	public Vector getEndPoint() {
		return startPoint.plus(vector);
	}

	public Vector getMidPoint() {
		return startPoint.plus(vector.by(0.5));
	}

	public Vector getDistanceFrom(Segment earlierPosition) {
		if (earlierPosition.getVector().isZero())
			return Vector.ZERO;
		
		Vector startPointMovement = getStartPoint().minus(earlierPosition.getStartPoint());
		Vector endPointMovement = getEndPoint().minus(earlierPosition.getEndPoint());
		Vector movement = startPointMovement.plus(endPointMovement).by(0.5);

		if (movement.isZero())
			return Vector.ZERO;

		try {
			return movement.getNormalComponentOn(getVector());
		} catch (ZeroVectorException e) {
			// should never happen with the previous checks
			return null;
		}
	}

	@Override
	public String toString() {
		return "[" + startPoint + ", " + vector + "]";
	}
}
