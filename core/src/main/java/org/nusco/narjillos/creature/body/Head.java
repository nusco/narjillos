package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Angle;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * The head of a creature, and the root of a tree of Organs.
 */
public class Head extends Organ {

	private static final double BASE_WAVE_FREQUENCY = 0.01;

	private final double metabolicRate;
	private final double percentEnergyToChildren;

	private Vector startPoint = Vector.ZERO;
	
	public Head(int adultLength, int adultThickness, ColorByte hue, double metabolicRate, double percentEnergyToChildren) {
		super(adultLength, adultThickness, hue, null, new WaveNerve(BASE_WAVE_FREQUENCY * metabolicRate));
		this.percentEnergyToChildren = percentEnergyToChildren;
		this.metabolicRate = metabolicRate;
	}

	@Override
	public double getMetabolicRate() {
		return metabolicRate;
	}

	public double getPercentEnergyToChildren() {
		return percentEnergyToChildren;
	}

	public void moveTo(Vector startPoint, double angle) {
		// we already reset the cache in setAngleToParent(), so
		// no need to do it twice here
		this.startPoint  = startPoint;
		updateAngleToParent(angle);
	}

	public void moveBy(Vector translation, double rotation) {
		Vector newStartPoint = getStartPoint().plus(translation);
		double newAngleToParent = Angle.normalize(getAngleToParent() + rotation);
		moveTo(newStartPoint, newAngleToParent);
	}

	@Override
	protected Vector calculateStartPoint() {
		return startPoint;
	}

	@Override
	protected double calculateAbsoluteAngle() {
		return getAngleToParent();
	}

	@Override
	protected Vector calculateCenterOfMass() {
		return getStartPoint().plus(getVector().by(0.5));
	}
	
	@Override
	protected double calculateNewAngleToParent(double targetAngle, double angleToTarget) {
		// The head never rotates on its own. It must be
		// explicitly repositioned by its client.
		return getAngleToParent();
	}
}