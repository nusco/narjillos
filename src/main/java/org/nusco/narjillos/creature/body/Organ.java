package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.core.physics.Segment;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.utilities.Configuration;

/**
 * A piece of body.
 * 
 * Describes the geometry of the organ (length, thickness, mass, ...). It also
 * goes through the calculations needed to come up with angles, etc.
 * 
 * It grows from a small minimum size at birth to adult size.
 * 
 * This class is a micro-framework (sigh). In and by itself, it cannot change
 * state (apart from the growBy() method). Subclasses are supposed to override
 * calculateStartPoint() and calculateAbsoluteAngle(), and then call either
 * updateGeometry() or updatePosition() when they want to update the state of
 * the Organ.
 */
public abstract class Organ {

	private final double adultLength;
	private final int adultThickness;
	private final double adultMass;
	private final Fiber fiber;
	private double length;
	private double thickness;

	// Caching - ugly, but has huge performance benefits. These will be updated
	// at start, and in the update() methods.
	// All volatile, to avoid too much synchronization.
	private volatile double cachedAbsoluteAngle;
	private volatile Vector cachedStartPoint;
	private volatile Vector cachedVector;
	private volatile Vector cachedEndPoint;
	private volatile double cachedMass;
	private volatile Vector cachedCenterOfMass;
	private volatile Segment cachedPositionInSpace;

	public Organ(int adultLength, int adultThickness, Fiber fiber) {
		this.adultLength = adultLength;
		this.adultThickness = adultThickness;
		this.adultMass = Math.max(adultLength * adultThickness, 1);

		this.length = Math.min(Configuration.ORGAN_MINIMUM_LENGTH_AT_BIRTH, adultLength);
		this.thickness = Math.min(Configuration.ORGAN_MINIMUM_THICKNESS_AT_BIRTH, adultThickness);
		this.fiber = fiber;

		this.cachedAbsoluteAngle = 0;
		this.cachedStartPoint = Vector.ZERO;
		this.cachedVector = Vector.polar(0, getLength());
		this.cachedEndPoint = cachedVector;
		this.cachedPositionInSpace = new Segment(Vector.ZERO, cachedVector);
		this.cachedMass = calculateMass();
		this.cachedCenterOfMass = cachedVector.by(0.5);
	}

	// Must be called to update the state of the organ. It will call into
	// calculateAbsoluteAngle() and calculateStartPoint().
	public final void updateGeometry() {
		cachedAbsoluteAngle = calculateAbsoluteAngle();
		cachedVector = calculateVector();
		cachedStartPoint = calculateStartPoint();
		cachedEndPoint = calculateEndPoint();
		cachedPositionInSpace = calculatePositionInSpace();
		cachedMass = calculateMass();
		cachedCenterOfMass = calculateCenterOfMass();
	}

	// Like updateGeometry() but much cheaper. It can only be called after pure
	// translations. Don't call after a rotation, because it doesn't update
	// angles.
	public final void updatePosition() {
		cachedStartPoint = calculateStartPoint();
		cachedEndPoint = calculateEndPoint();
		cachedPositionInSpace = calculatePositionInSpace();
		cachedCenterOfMass = calculateCenterOfMass();
	}

	public void growBy(int amount) {
		if (isFullyGrown())
			return;

		length = Math.min(adultLength, getLength() + Configuration.ORGAN_GROWTH_RATE * amount);
		thickness = Math.min(adultThickness, getThickness() + Configuration.ORGAN_GROWTH_RATE * amount);

		// Optimization: don't update the geometry here. Instead, let the
		// client do it - it knows when that's needed.
	}

	public boolean isFullyGrown() {
		return getLength() >= adultLength && getThickness() >= adultThickness;
	}

	public Segment getPositionInSpace() {
		return cachedPositionInSpace;
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

	public final double getLength() {
		return length;
	}

	public double getThickness() {
		return thickness;
	}

	public Fiber getFiber() {
		return fiber;
	}

	public double getMass() {
		return cachedMass;
	}

	public double getAdultMass() {
		return adultMass;
	}

	public final double getAbsoluteAngle() {
		return cachedAbsoluteAngle;
	}

	public final Vector getStartPoint() {
		return cachedStartPoint;
	}

	public final Vector getEndPoint() {
		return cachedEndPoint;
	}

	public final Vector getCenterOfMass() {
		return cachedCenterOfMass;
	}

	public String getVisualHash() {
		int approximateAdultLength = (int)(getAdultLength() / 25);
		int approximateAdultThickness = (int)(getAdultThickness() / 5);
		return getFiber().getVisualHash() + "_" + approximateAdultLength + "_" + approximateAdultThickness;
	}
	
	// The next two methods give subclasses a chance to change the geometry of
	// the Organ. These are the only methods that can change the state of the
	// Organ (apart from growBy(). Everything else in the organ will be
	// calculated after these.
	protected abstract Vector calculateStartPoint();
	protected abstract double calculateAbsoluteAngle();

	private Vector calculateVector() {
		return Vector.polar(getAbsoluteAngle(), getLength());
	}

	private Vector getVector() {
		return cachedVector;
	}

	private Vector calculateEndPoint() {
		return getStartPoint().plus(getVector());
	}

	private double calculateMass() {
		return Math.max(getLength() * getThickness(), 1);
	}

	private Vector calculateCenterOfMass() {
		return getStartPoint().plus(getVector().by(0.5));
	}

	private Segment calculatePositionInSpace() {
		return new Segment(getStartPoint(), getVector());
	}
}
