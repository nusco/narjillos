package org.nusco.swimmers.shared.physics;

public class Vector {

	public static final Vector ZERO = Vector.cartesian(0, 0);
	public static final Vector ZERO_ONE = Vector.polar(0, 1);

	public static Vector polar(double degrees, double length) {
		double sin = Math.sin(Math.toRadians(degrees));
		double cos = Math.cos(Math.toRadians(degrees));
		
		return Vector.cartesian(cos * length, sin * length);
	}

	public static Vector cartesian(double x, double y) {
		return new Vector(x, y);
	}

	private final double x;
	private final double y;

	// redundant data for performance
	private final double angle;
	private final double length;
	
	private Vector(double x, double y) {
		this.x = x;
		this.y = y;
		this.angle = Math.toDegrees(Math.atan2(y, x));
		this.length = Math.sqrt(x * x + y * y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAngle() {
	    return angle;
	}

	public double getLength() {
		return length;
	}

	public Vector getInverse() {
		return Vector.cartesian(-x, -y);
	}

	public Vector getNormal() {
		return Vector.polar(angle - 90, 1);
	}

	public Vector plus(Vector other) {
		return Vector.cartesian(getX() + other.getX(), getY() + other.getY());
	}

	public Vector minus(Vector other) {
		return Vector.cartesian(getX() - other.getX(), getY() - other.getY());
	}

	public Vector by(double scalar) {
		return Vector.cartesian(getX() * scalar, getY() * scalar);
	}

	public Vector normalize(double length) {
		return Vector.polar(getAngle(), length);
	}

	public Vector getProjectionOn(Vector other) {
		Vector direction = pointsInSameDirectionAs(other) ? other : other.getInverse();
		double relativeAngle = direction.getAngle() - getAngle();
		double resultLength = Math.cos(Math.toRadians(relativeAngle)) * getLength();
		return Vector.polar(direction.getAngle(), resultLength);
	}

	private boolean pointsInSameDirectionAs(Vector other) {
		return other.getAngle() - getAngle() < 90;
	}

	public Vector getNormalComponentOn(Vector other) {
		return getProjectionOn(other.getNormal());
	}

	public double getAngleWith(Vector other) {
		double result = getAngle() - other.getAngle();
		if(result < -180)
			return result + 360;
		if(result > 180)
			return result - 360;
		return result;
	}

	public Vector rotateBy(double degrees) {
		return Vector.polar(getAngle() + degrees, getLength());
	}

	@Override
	public int hashCode() {
		return (int)x ^ (int)y;
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
		return Math.abs(getAngle() - other.getAngle()) < delta && Math.abs(getLength() - other.getLength()) < delta;
	}
	
	@Override
	public String toString() {
		return "(" + approx(getX()) + ", " + approx(getY()) + ")";
	}

	private double approx(double n) {
		final double decimals = 100.0;
		return (Math.round(n * decimals)) / decimals;
	}
}
