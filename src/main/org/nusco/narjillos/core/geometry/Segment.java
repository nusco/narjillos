package org.nusco.narjillos.core.geometry;

/**
 * A vector that has a specific origin in the plane.
 */
public class Segment implements Bounded {

	private final Vector startPoint;

	private final Vector vector;

	private transient BoundingBox cachedBoundingBox;

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

	public double getMinimumDistanceFromPointSquared(Vector point) {
		if (vector.isZero())
			return startPoint.getDistanceSquaredFrom(point);

		double lengthSquared = vector.getLengthSquared();

		if (lengthSquared < 0.00001)
			return startPoint.getDistanceSquaredFrom(point);

		double t =
			((point.x - startPoint.x) * (vector.x - startPoint.x) + (point.y - startPoint.y) * (vector.y - startPoint.y)) / lengthSquared;

		if (t < 0)
			return startPoint.getDistanceSquaredFrom(point);

		if (t > 1)
			return (getEndPoint()).getDistanceSquaredFrom(point);

		Vector projection = startPoint.plus(vector.minus(startPoint).by(t));
		return projection.minus(point).getLengthSquared();
	}

	public Vector getDistanceFrom(Segment other) {
		// TODO: why did I do this? surely it's conceptually wrong?
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
	public BoundingBox getBoundingBox() {
		if (cachedBoundingBox == null)
			cachedBoundingBox = new BoundingBox(this);
		return cachedBoundingBox;
	}

	@Override
	public String toString() {
		return "[" + startPoint + ", " + vector + "]";
	}
}
