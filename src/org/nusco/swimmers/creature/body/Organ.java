package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.utilities.ColorByte;

public abstract class Organ {

	protected final int length;
	protected final int thickness;
	protected final ColorByte hue;
	
	// caching - ugly, but huge performance benefits
	private Vector cachedStartPoint = null;
	private Vector cachedEndPoint = null;
	private Double cachedAbsoluteAngle = null;
	private Vector cachedMainAxis = null;
	private Vector cachedVector = null;
	private ColorByte cachedColor = null;

	public Organ(int length, int thickness, ColorByte hue) {
		this.length = length;
		this.thickness = thickness;
		this.hue = hue;
	}

	public synchronized int getLength() {
		return length;
	}

	public synchronized int getThickness() {
		return thickness;
	}

	protected synchronized ColorByte getHue() {
		return hue;
	}

	public synchronized double getMass() {
		return getLength() * getThickness() * 0.1;
	}

	protected synchronized void resetAllCaches() {
		cachedAbsoluteAngle = null;
		cachedStartPoint = null;
		cachedEndPoint = null;
		cachedMainAxis = null;
		cachedVector = null;
		// the color never changes, so there is no need to reset it
	}

	public synchronized final double getAbsoluteAngle() {
		if (cachedAbsoluteAngle == null)
			cachedAbsoluteAngle = calculateAbsoluteAngle();
		return cachedAbsoluteAngle;
	}

	protected abstract double calculateAbsoluteAngle();

	public synchronized final Vector getStartPoint() {
		if (cachedStartPoint == null)
			cachedStartPoint = calculateStartPoint();
		return cachedStartPoint;
	}

	protected abstract Vector calculateStartPoint();

	public synchronized final Vector getEndPoint() {
		if (cachedEndPoint == null)
			cachedEndPoint = getStartPoint().plus(getVector());
		return cachedEndPoint;
	}

	public synchronized final Vector getVector() {
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

	public synchronized final ColorByte getColor() {
		if (cachedColor == null)
			cachedColor = calculateColor();
		return cachedColor;
	}

	protected abstract ColorByte calculateColor();

	public synchronized Segment getSegment() {
		return new Segment(getStartPoint(), getVector());
	}

	@Override
	public int hashCode() {
		return hue.toByteSizedInt() ^ length ^ thickness;
	}

	@Override
	public boolean equals(Object obj) {
		Organ other = (Organ) obj;
		return hue == other.hue && length == other.length && thickness == other.thickness;
	}
}
