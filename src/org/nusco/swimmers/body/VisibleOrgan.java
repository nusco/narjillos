package org.nusco.swimmers.body;

import java.util.LinkedList;
import java.util.List;

public abstract class VisibleOrgan implements Organ {

	private final int length;
	private final int thickness;
	private final int rgb;

	private double relativeAngle = 0;

	private List<Organ> children = new LinkedList<>();

	protected VisibleOrgan(int length, int thickness, int initialRelativeAngle, int rgb) {
		this.length = length;
		this.thickness = thickness;
		this.relativeAngle = initialRelativeAngle;
		this.rgb = rgb;
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

	@Override
	public double getRelativeAngle() {
		return normalize(relativeAngle);
	}

	protected double normalize(double angle) {
		return ((angle % 360) + 360) % 360;
	}

	public abstract Vector getStartPoint();

	/* (non-Javadoc)
	 * @see org.nusco.swimmers.body.Organ#getEndPoint()
	 */
	@Override
	public Vector getEndPoint() {
		return getStartPoint().plus(length, getAngle());
	}

	public abstract double getAngle();

	@Override
	public abstract Organ getParent();

	@Override
	public Organ getAsParent() {
		return this;
	}

	@Override
	public List<Organ> getChildren() {
		return children;
	}

	@Override
	public VisibleOrgan sproutVisibleOrgan(int length, int thickness, int initialRelativeAngle, int rgb) {
		VisibleOrgan child = new BodyPart(length, thickness, this, initialRelativeAngle, rgb);
		children.add(child);
		return child;
	}

	@Override
	public Organ sproutInvisibleOrgan() {
		Organ child = new NullOrgan(this);
		children.add(child);
		return child;
	}

	public boolean isVisible() {
		return true;
	}

	public int getRGB() {
		return rgb;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		VisibleOrgan other = (VisibleOrgan) obj;
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
