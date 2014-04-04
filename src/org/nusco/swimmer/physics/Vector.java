package org.nusco.swimmer.physics;

public class Vector {

	public static final Vector ZERO = new Vector(0, 0);
	public static final Vector ONE = new Vector(0, 1);

	private final double x;
	private final double y;
	
	public Vector(double x, double y) {
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
	    return Angle.normalize(Math.toDegrees(Math.atan2(getX(), getY())));
	}

	public double getLength() {
		return Math.sqrt(getX() * getX() + getY() * getY());
	}

	public Vector plus(double length, double angle) {
		double angleInRadians = Math.toRadians(angle);
	    long x = (long)(Math.cos(angleInRadians) * length);
	    long y = (long)(Math.sin(angleInRadians) * length);
		return new Vector(getX() + x, getY() + y);
	}

	public Vector by(double scalar) {
		return new Vector(getX() * scalar, getY() * scalar);
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
