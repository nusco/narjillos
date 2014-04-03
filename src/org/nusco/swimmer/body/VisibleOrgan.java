package org.nusco.swimmer.body;

import org.nusco.swimmer.body.pns.Nerve;
import org.nusco.swimmer.physics.Angle;
import org.nusco.swimmer.physics.Vector;

abstract class VisibleOrgan extends Organ {

	private final int length;
	private final int thickness;
	private final int rgb;
	private final double relativeAngle;

	protected VisibleOrgan(int length, int thickness, int relativeAngle, int rgb, Nerve neuron, Organ parent) {
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
	public void tick() {
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof VisibleOrgan))
			return false;
		VisibleOrgan other = (VisibleOrgan) obj;
		if (getLength() != other.getLength() || getThickness() != other.getThickness() || getRGB() != other.getRGB())
			return false;
		if (Double.doubleToLongBits(getRelativeAngle()) != Double.doubleToLongBits(other.getRelativeAngle()))
			return false;
		if (!getStartPoint().equals(other.getStartPoint()))
			return false;
		if(getParent() == null) {
			if (other.getParent() != null)
				return false;
		}
		else if (!getParent().equals(other.getParent()))
			return false;
		return true;
	}	
	
	@Override
	public String toString() {
		return "[" + length + "," + thickness + "," + getAngle() + "]";
	}
}
