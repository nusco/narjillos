package org.nusco.narjillos.core.physics;

/**
 * A point on a plane, in either cartesian or polar coordinates.
 */
public class Vector {

	public static final Vector ZERO = new Vector(0, 0);

	static {
		FastMath.setUp();
	}

	public final double x;
	public final double y;

	// cached fields (for performance)
	private double angle = Double.NaN;
	private double length = Double.NaN;

	public static Vector polar(double degrees, double length) {
		double sin = FastMath.sin(degrees);
		double cos = FastMath.cos(degrees);
		return Vector.cartesian(cos * length, sin * length);
	}

	public static Vector cartesian(double x, double y) {
		return new Vector(x, y);
	}

	private Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public boolean isZero() {
		return x == 0 && y == 0;
	}

	public double getAngle() throws ZeroVectorAngleException {
		if (isZero())
			throw new ZeroVectorAngleException();
		
		if (Double.isNaN(angle))
			angle = FastMath.atan(y, x);
		
		return angle;
	}

	public double getLength() {
		if (Double.isNaN(length))
			length = Math.sqrt(x * x + y * y);

		return length;
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

	double getDistanceFrom(Vector other) {
		return this.minus(other).getLength();
	}

	public Vector getNormalComponentOn(Vector other) throws ZeroVectorAngleException {
		double resultAngle = other.getAngle() - 90;
		double resultLength = FastMath.cos(getAngle() - resultAngle) * getLength();
		return Vector.polar(resultAngle, resultLength);
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Vector other = (Vector) obj;
		return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
				&& Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
	}

	public boolean approximatelyEquals(Vector other) {
		final double delta = 0.01;
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
}
