package org.nusco.narjillos.shared.physics;

/**
 * A point on a plane, in either cartesian or polar coordinates.
 */
public class Vector {

	public static final Vector ZERO = Vector.cartesian(0, 0);

	public final double x;
	public final double y;

	public static Vector polar(double degrees, double length) {
		double sin = Math.sin(Math.toRadians(degrees));
		double cos = Math.cos(Math.toRadians(degrees));
		return Vector.cartesian(cos * length, sin * length);
	}

	public static Vector cartesian(double x, double y) {
		return new Vector(x, y);
	}
	
	private Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getAngle() throws ZeroVectorException {
		if (x == 0 && y == 0)
			throw new ZeroVectorException();
	    return Math.toDegrees(Math.atan2(y, x));
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector plus(Vector other) {
		return Vector.cartesian(x + other.x, y + other.y);
	}

	public Vector minus(Vector other) {
		return Vector.cartesian(x - other.x, y - other.y);
	}

	public Vector by(double scalar) {
		return Vector.cartesian(x * scalar, y * scalar);
	}

	public Vector invert() {
		return this.by(-1);
	}

	public Vector normalize(double length) throws ZeroVectorException {
		return Vector.polar(getAngle(), length);
	}

	Vector getNormal() throws ZeroVectorException {
		return Vector.polar(getAngle() - 90, 1);
	}

	public Vector getProjectionOn(Vector other) throws ZeroVectorException {
		Vector direction = pointsInSameDirectionAs(other) ? other : other.invert();
		double relativeAngle = Math.toRadians(direction.getAngle() - getAngle());
		double resultLength = Math.cos(relativeAngle) * getLength();
		return Vector.polar(direction.getAngle(), resultLength);
	}

	private boolean pointsInSameDirectionAs(Vector other) throws ZeroVectorException {
		return Math.abs(getAngleWith(other)) < 90;
	}

	public Vector getNormalComponentOn(Vector other) throws ZeroVectorException {
		return getProjectionOn(other.getNormal());
	}

	public double getAngleWith(Vector other) throws ZeroVectorException {
		double result = getAngle() - other.getAngle();
		if(result < -180)
			return result + 360;
		if(result > 180)
			return result - 360;
		return result;
	}

	public Vector rotateBy(double degrees) throws ZeroVectorException {
		return Vector.polar(getAngle() + degrees, getLength());
	}

	double getDistanceFrom(Vector other) {
		return this.minus(other).getLength();
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		Vector other = (Vector) obj;
		return compare(x, other.x) && compare(y, other.y);
	}

	private boolean compare(double d1, double d2) {
		return Double.doubleToLongBits(d1) == Double.doubleToLongBits(d2);
	}

	public boolean almostEquals(Vector other) {
		final double delta = 0.001;
		return Math.abs(x - other.x) < delta && Math.abs(y - other.y) < delta;
	}
	
	@Override
	public String toString() {
		return "(" + approx(x) + ", " + approx(y) + ")";
	}

	private double approx(double n) {
		final double decimals = 100.0;
		return (Math.round(n * decimals)) / decimals;
	}

	public double getCrossProductWith(Vector other) throws ZeroVectorException {
		return getLength() * other.getLength() * Math.cos(Math.toRadians(getAngleWith(other)));
	}

	public boolean isZero() {
		return x == 0 && y == 0;
	}
}
