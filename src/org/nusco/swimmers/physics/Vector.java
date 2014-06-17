package org.nusco.swimmers.physics;

public class Vector {

	public static final Vector ZERO = Vector.cartesian(0, 0);
	public static final Vector ZERO_ONE = Vector.polar(0, 1);

	public static Vector polar(double degrees, double length) {
		double sin = Math.sin(Math.toRadians(degrees));
		double cos = Math.cos(Math.toRadians(degrees));
		
		return new Vector(cos * length, sin * length);
	}

	public static Vector cartesian(double x, double y) {
		return new Vector(x, y);
	}

	private final double x;
	private final double y;
	
	private Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAngle() {
	    return Math.toDegrees(Math.atan2(getY(), getX()));
	}

	public double getLength() {
		return Math.sqrt(getX() * getX() + getY() * getY());
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

	public Vector getNormal() {
		return Vector.polar(getAngle() - 90, 1);
	}

	public Vector getTangentialComponentOn(Vector other) {
		double relativeAngle = other.getAngle() - getAngle();
		double resultLength = Math.cos(Math.toRadians(relativeAngle)) * getLength();
		return Vector.polar(other.getAngle(), resultLength);
	}

	public Vector getNormalComponentOn(Vector other) {
		return getTangentialComponentOn(other.getNormal());
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

	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}
}
