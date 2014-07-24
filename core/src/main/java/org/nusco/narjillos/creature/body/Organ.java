package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public abstract class Organ {

	private final int length;
	private final int thickness;
	private final double mass;
	private final ColorByte color;
	
	// caching - ugly, but huge performance benefits
	private Vector cachedStartPoint = null;
	private Vector cachedEndPoint = null;
	private Double cachedAbsoluteAngle = null;
	private Vector cachedMainAxis = null;
	private Vector cachedVector = null;

	public Organ(int length, int thickness, ColorByte color) {
		this.length = length;
		this.thickness = thickness;
		this.mass = length * thickness * 0.1;
		this.color = color;
	}

	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	public double getMass() {
		return mass;
	}

	public ColorByte getColor() {
		return color;
	}

	protected synchronized void resetAllCaches() {
		cachedAbsoluteAngle = null;
		cachedStartPoint = null;
		cachedEndPoint = null;
		cachedMainAxis = null;
		cachedVector = null;
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

	public synchronized Segment getSegment() {
		return new Segment(getStartPoint(), getVector());
	}

	protected final Vector getMainAxis() {
		if (cachedMainAxis == null)
			cachedMainAxis = calculateMainAxis();
		return cachedMainAxis;
	}

	protected abstract Vector calculateMainAxis();

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
