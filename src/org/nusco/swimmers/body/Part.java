package org.nusco.swimmers.body;

import java.util.LinkedList;
import java.util.List;

public abstract class Part {

	private final int length;
	private final int thickness;

	private double relativeAngle = 0;
	private List<Part> children = new LinkedList<>();

	protected Part(int length, int thickness, int initialRelativeAngle) {
		this.length = length;
		this.thickness = thickness;
		this.relativeAngle = initialRelativeAngle;
	}

	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	public void setRelativeAngle(double angle) {
		this.relativeAngle = angle;
	}

	public int getRelativeAngle() {
		return normalize(relativeAngle);
	}

	protected int normalize(double angle) {
		return (int)(((angle % 360) + 360) % 360);
	}

	public abstract Vector getStartPoint();

	public Vector getEndPoint() {
		return getStartPoint().plus(length, getAngle());
	}

	public abstract int getAngle();

	public List<Part> getChildren() {
		return children;
	}

	public Part sproutChild(int length, int thickness, int initialRelativeAngle) {
		Part child = new BodyPart(length, thickness, this, initialRelativeAngle);
		children.add(child);
		return child;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		Part other = (Part) obj;
		if (length != other.length || thickness != other.thickness)
			return false;
		if (Double.doubleToLongBits(relativeAngle) != Double
				.doubleToLongBits(other.relativeAngle))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[" + length + "," + thickness + "," + getAngle() + "]";
	}
}
