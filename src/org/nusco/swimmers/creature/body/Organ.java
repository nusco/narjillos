package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;

public abstract class Organ {

	protected final int length;
	protected final int thickness;
	protected final int hue;
	
	// caching - ugly, but huge performance benefits
	private Vector cachedStartPoint = null;
	private Vector cachedEndPoint = null;
	private Double cachedAbsoluteAngle = null;
	private Vector cachedMainAxis = null;
	private Vector cachedVector = null;
	private Integer cachedColor = null;

	public Organ(int length, int thickness, int hue) {
		this.length = length;
		this.thickness = thickness;
		this.hue = hue;
	}

	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	protected int getHue() {
		return hue;
	}

	public double getMass() {
		return getLength() * getThickness() * 0.1;
	}

	protected void resetAllCaches() {
		cachedAbsoluteAngle = null;
		cachedStartPoint = null;
		cachedEndPoint = null;
		cachedMainAxis = null;
		cachedVector = null;
		cachedColor = null;
	}

	public final double getAbsoluteAngle() {
		if (cachedAbsoluteAngle == null)
			cachedAbsoluteAngle = calculateAbsoluteAngle();
		return cachedAbsoluteAngle;
	}

	protected abstract double calculateAbsoluteAngle();

	public final Vector getStartPoint() {
		if (cachedStartPoint == null)
			cachedStartPoint = calculateStartPoint();
		return cachedStartPoint;
	}

	protected abstract Vector calculateStartPoint();

	public final Vector getEndPoint() {
		if (cachedEndPoint == null)
			cachedEndPoint = getStartPoint().plus(Vector.polar(getAbsoluteAngle(), length));
		return cachedEndPoint;
	}

	public final Vector getVector() {
		if (cachedVector == null)
			cachedVector = Vector.polar(getAbsoluteAngle(), getLength());
		return cachedVector;
	}

	protected final Vector getMainAxis() {
		if (cachedMainAxis == null)
			cachedMainAxis = calculateMainAxis();
		return cachedMainAxis;
	}

	protected abstract Vector calculateMainAxis();

	public final int getColor() {
		if (cachedColor == null)
			cachedColor = calculateColor();
		return cachedColor;
	}

	protected abstract int calculateColor();

	public Segment getSegment() {
		return new Segment(getStartPoint(), getVector());
	}

	@Override
	public int hashCode() {
		return hue ^ length ^ thickness;
	}

	@Override
	public boolean equals(Object obj) {
		Organ other = (Organ) obj;
		return hue == other.hue && length == other.length && thickness == other.thickness;
	}

}
