package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public abstract class Organ {

	protected final int length;
	protected final int thickness;
	protected final ColorByte color;
	
	// caching - ugly, but huge performance benefits
	private Vector cachedStartPoint = null;
	private Vector cachedEndPoint = null;
	private Double cachedAbsoluteAngle = null;
	private Vector cachedMainAxis = null;
	private Vector cachedVector = null;

	public Organ(int length, int thickness, ColorByte color) {
		this.length = length;
		this.thickness = thickness;
		this.color = color;
	}

	public synchronized int getLength() {
		return length;
	}

	public synchronized int getThickness() {
		return thickness;
	}

	public ColorByte getColor() {
		return color;
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

	public synchronized Segment getSegment() {
		return new Segment(getStartPoint(), getVector());
	}

	@Override
	public int hashCode() {
		return length ^ thickness;
	}

	@Override
	public boolean equals(Object obj) {
		Organ other = (Organ) obj;
		return length == other.length && thickness == other.thickness && color == other.color;
	}
}
