package org.nusco.narjillos.core.physics;

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

	public Vector getEndPoint() {
		return startPoint.plus(vector);
	}

	public Vector getVector() {
		return vector;
	}

	public double getMinimumDistanceFromPoint(Vector point) {
		// FIXME: this was probably meant to avoid zerovectorexceptions,
		// but it's just wrong. fix it.
		if (vector.isZero())
			return startPoint.getDistanceFrom(point);
		
		double length = vector.getLength();

		// FIXME: do we need this?
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

	public Vector getDistanceFrom(Segment other) {
		// FIXME: why did I do this? surely it's conceptually wrong?
		if (other.getVector().isZero())
			return Vector.ZERO;
		
		Vector startPointMovement = getStartPoint().minus(other.getStartPoint());
		Vector endPointMovement = getEndPoint().minus(other.getEndPoint());
		Vector movement = startPointMovement.plus(endPointMovement).by(0.5);

		if (movement.isZero())
			return Vector.ZERO;

		try {
			return movement.getNormalComponentOn(getVector());
		} catch (ZeroVectorAngleException e) {
			// should never happen with the previous checks
			return null;
		}
	}

	@Override
	public String toString() {
		return "[" + startPoint + ", " + vector + "]";
	}
}
