package org.nusco.swimmers.body;

import org.nusco.swimmers.neural.Neuron;

public abstract class VisibleOrgan extends Organ {

	private final int length;
	private final int thickness;
	private final int rgb;
	private final double relativeAngle;

	protected VisibleOrgan(int length, int thickness, int relativeAngle, int rgb, Neuron neuron, Organ parent) {
		super(neuron, parent);
		this.length = length;
		this.thickness = thickness;
		this.relativeAngle = Angle.normalize(relativeAngle);
		this.rgb = rgb;
	}

	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	public abstract Vector getStartPoint();

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
	public double getRelativeAngle() {
		return relativeAngle;
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
