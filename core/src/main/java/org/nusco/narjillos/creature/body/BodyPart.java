package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * A piece of a body.
 * 
 * Contains all the geometric qualities of the body part (like length, thickness
 * and mass), and does the painstaking calculations needed to come up with
 * angles, etc.
 * 
 * This code is not something I'm proud of. The caching code confuses the heck
 * out of me as much as it does to you. Try to understand... it's for
 * performance.
 */
public abstract class BodyPart {

	private static final int MINIMUM_LENGTH_AT_BIRTH = 5;
	private static final int MINIMUM_THICKNESS_AT_BIRTH = 1;
	private static final double GROWTH_RATE = 0.01;

	private final double adultLength;
	private final int adultThickness;
	private final double adultMass;
	private final ColorByte color;
	private double length;
	private double thickness;

	// Caching - ugly, but has huge performance benefits.
	// All volatile, to avoid too much synchronization
	private volatile double cachedAbsoluteAngle;
	private volatile Vector cachedStartPoint;
	private volatile Vector cachedVector;
	private volatile Vector cachedEndPoint;
	private volatile double cachedMass;
	private volatile Vector cachedCenterOfMass;
	private volatile Segment cachedPositionInSpace;

	public BodyPart(int adultLength, int adultThickness, ColorByte color) {
		this.adultLength = adultLength;
		this.adultThickness = adultThickness;
		this.adultMass = Math.max(adultLength * adultThickness, 1);

		this.length = Math.min(MINIMUM_LENGTH_AT_BIRTH, adultLength);
		this.thickness = Math.min(MINIMUM_THICKNESS_AT_BIRTH, adultThickness);
		this.color = color;

		initCaches();
	}

	private void initCaches() {
		this.cachedAbsoluteAngle = 0;
		this.cachedStartPoint = Vector.ZERO;
		this.cachedVector = Vector.polar(0, getLength());
		this.cachedEndPoint = cachedVector;
		this.cachedPositionInSpace = new Segment(Vector.ZERO, cachedVector);
		this.cachedMass = calculateMass();
		this.cachedCenterOfMass = cachedVector.by(0.5);
	}

	public final void updateCaches() {
		cachedAbsoluteAngle = calculateAbsoluteAngle();
		cachedVector = calculateVector();
		cachedStartPoint = calculateStartPoint();
		cachedEndPoint = calculateEndPoint();
		cachedPositionInSpace = calculatePositionInSpace();
		cachedMass = calculateMass();
		cachedCenterOfMass = calculateCenterOfMass();
	}

	public final double getLength() {
		return length;
	}

	public double getThickness() {
		return thickness;
	}

	public void grow() {
		if (isFullyGrown())
			return;

		length = Math.min(adultLength, getLength() + GROWTH_RATE);
		thickness = Math.min(adultThickness, getThickness() + GROWTH_RATE);

		// optimization: let the client update the caches
	}

	public boolean isFullyGrown() {
		return getLength() >= adultLength && getThickness() >= adultThickness;
	}

	public double getMass() {
		return cachedMass;
	}

	public double getAdultMass() {
		return adultMass;
	}

	public ColorByte getColor() {
		return color;
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

	private double calculateMass() {
		return Math.max(getLength() * getThickness(), 1);
	}

	protected abstract Vector calculateCenterOfMass();

	public Segment getPositionInSpace() {
		return cachedPositionInSpace;
	}

	private Segment calculatePositionInSpace() {
		return new Segment(getStartPoint(), getVector());
	}

	public boolean isAtrophic() {
		return adultLength == 0;
	}

	public double getAdultLength() {
		return adultLength;
	}

	public int getAdultThickness() {
		return adultThickness;
	}
}
