package org.nusco.narjillos.shared.physics;

/**
 * A point on a plane, in either cartesian or polar coordinates.
 */
public class Vector {

	public static final Vector ZERO = Vector.cartesian(0, 0);

	static {
		FastMath.setUp();
	}

	public final double x;
	public final double y;

	// cached fields (for performance)
	private double angle = Double.NaN;
	private double length = Double.NaN;
	private Vector inverted = null;
	private Vector normal = null;
	
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

	public double getAngle() throws ZeroVectorException {
		if (isZero())
			throw new ZeroVectorException();
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

	public Vector invert() {
		if (inverted == null)
			inverted  = this.by(-1);
		return inverted;
	}

	public Vector normalize(double length) throws ZeroVectorException {
		return Vector.polar(getAngle(), length);
	}

	public Vector getNormal() throws ZeroVectorException {
		if (normal == null)
			normal = Vector.polar(getAngle() - 90, 1);
		return normal;
	}

	public Vector getProjectionOn(Vector other) throws ZeroVectorException {
		double resultLength = FastMath.cos(getAngleWith(other)) * getLength();
		return Vector.polar(other.getAngle(), resultLength);
	}

	public Vector getNormalComponentOn(Vector other) throws ZeroVectorException {
		return getProjectionOn(other.getNormal());
	}

	public double getAngleWith(Vector other) throws ZeroVectorException {
		return Angle.normalize(getAngle() - other.getAngle());
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
		return areExactlyEqual(x, other.x) && areExactlyEqual(y, other.y);
	}

	private boolean areExactlyEqual(double d1, double d2) {
		return Double.doubleToLongBits(d1) == Double.doubleToLongBits(d2);
	}

	public boolean almostEquals(Vector other) {
		final double delta = 0.01;
		return Math.abs(x - other.x) < delta && Math.abs(y - other.y) < delta;
	}

	private double approx(double n) {
		final double decimals = 100.0;
		return (Math.round(n * decimals)) / decimals;
	}

	public boolean isZero() {
		return x == 0 && y == 0;
	}
	
	@Override
	public String toString() {
		return "(" + approx(x) + ", " + approx(y) + ")";
	}
}
