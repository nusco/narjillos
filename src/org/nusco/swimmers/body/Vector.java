package org.nusco.swimmers.body;

public class Vector {
	
	public static final Vector ZERO = new Vector(0, 0);

	private final long x;
	private final long y;
	
	public Vector(long x, long y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		Vector other = (Vector)obj;
		return other.getX() == getX() && other.getY() == getY();
	}

	public Vector plus(int length, double angle) {
		double angleInRadians = Math.toRadians(angle);
	    long x = (long)(Math.cos(angleInRadians) * length);
	    long y = (long)(Math.sin(angleInRadians) * length);
		return new Vector(getX() + x, getY() + y);
	}
	
	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}

	public long getX() {
		return x;
	}

	public long getY() {
		return y;
	}
}
