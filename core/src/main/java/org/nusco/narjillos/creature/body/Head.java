package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.physics.ForceField;
import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class Head extends Organ {

	private final double metabolicRate;
	
	public Head(int length, int thickness, ColorByte hue, double metabolicRate) {
		super(length, thickness, hue, null, new PassNerve());
		this.metabolicRate = metabolicRate;
	}

	public double getMetabolicRate() {
		return metabolicRate;
	}
	
	Vector getMainAxis() {
		return getVector().normalize(1).invert();
	}

	@Override
	protected Vector calculateStartPoint() {
		return Vector.ZERO;
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getAngleToParent();
	}

	@Override
	protected Vector calculateCenterOfMass() {
		return getVector().by(0.5);
	}
	
	@Override
	protected double calculateAngleToParent(double targetAngle, double skewing, ForceField forceField) {
		// the head never moves on its own
		return getAngleToParent();
	}

	@Override
	protected double getForcedBend() {
		return 0;
	}
}