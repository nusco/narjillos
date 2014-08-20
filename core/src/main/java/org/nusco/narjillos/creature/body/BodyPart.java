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
	private volatile double cachedAbsoluteAngle;
	private volatile Vector cachedStartPoint;
	private volatile Vector cachedVector;
	private volatile Vector cachedEndPoint;
	private volatile Vector cachedCenterOfMass;
	private volatile Segment cachedPositionInSpace;

	public BodyPart(int length, int thickness, ColorByte color) {
		this.length = length;
		this.thickness = thickness;
		this.mass = Math.max(length * thickness, 1);
		this.color = color;
		initCaches();
	}

	private void initCaches() {
		this.cachedAbsoluteAngle = 0;
		this.cachedStartPoint = Vector.ZERO;
		this.cachedVector = Vector.polar(0, getLength());
		this.cachedEndPoint = cachedVector;
		this.cachedCenterOfMass = cachedVector.by(0.5);
		this.cachedPositionInSpace = new Segment(Vector.ZERO, cachedVector);
	}

	public final int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	// expressed in [millimeters]
	public double getMass() {
		return mass;
	}

	public ColorByte getColor() {
		return color;
	}

	protected final void updateCaches() {
		cachedAbsoluteAngle = calculateAbsoluteAngle();
		cachedVector = calculateVector();
		cachedStartPoint = calculateStartPoint();
		cachedEndPoint = calculateEndPoint();
		cachedPositionInSpace = calculatePositionInSpace();
		cachedCenterOfMass = calculateCenterOfMass();
	}

	private Vector calculateVector() {
		return Vector.polar(getAbsoluteAngle(), getLength());
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
}
