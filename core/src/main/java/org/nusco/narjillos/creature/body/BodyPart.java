package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public abstract class BodyPart {

	private final int length;
	private final int thickness;
	private final double mass;
	private final ColorByte color;

	// Caching - ugly, but has huge performance benefits.
	// All volatile, to avoid too much synchronization
	private volatile Vector cachedStartPoint = Vector.ZERO;
	private volatile Vector cachedEndPoint = Vector.ZERO;
	private volatile double cachedAbsoluteAngle = 0;
	private volatile Vector cachedVector = Vector.ZERO;
	private volatile Vector cachedCenterOfMass = Vector.ZERO;
	private volatile Segment cachedPositionInSpace = new Segment(Vector.ZERO, Vector.ZERO);

	public BodyPart(int length, int thickness, ColorByte color) {
		this.length = length;
		this.thickness = thickness;
		this.mass = Math.max(length * thickness, 1);
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

	protected final void updateCaches() {
		cachedAbsoluteAngle = calculateAbsoluteAngle();
		cachedVector = Vector.polar(getAbsoluteAngle(), getLength());
		cachedStartPoint = calculateStartPoint();
		cachedEndPoint = calculateEndPoint();
		cachedPositionInSpace = calculatePositionInSpace();
		cachedCenterOfMass = calculateCenterOfMass();
	}

	public final double getAbsoluteAngle() {
		return cachedAbsoluteAngle;
	}

	protected abstract double calculateAbsoluteAngle();

	public final Vector getStartPoint() {
		return cachedStartPoint;
	}

	protected abstract Vector calculateStartPoint();

	private Vector calculateEndPoint() {
		return getStartPoint().plus(getVector());
	}

	public final Vector getEndPoint() {
		return cachedEndPoint;
	}

	final Vector getVector() {
		return cachedVector;
	}

	public final Vector getCenterOfMass() {
		return cachedCenterOfMass;
	}

	protected abstract Vector calculateCenterOfMass();

	public Segment getPositionInSpace() {
		return cachedPositionInSpace;
	}

	private Segment calculatePositionInSpace() {
		return new Segment(getStartPoint(), getVector());
	}

	@Override
	public int hashCode() {
		return length ^ thickness;
	}

	@Override
	public boolean equals(Object obj) {
		BodyPart other = (BodyPart) obj;
		return length == other.length && thickness == other.thickness && color == other.color;
	}
}
