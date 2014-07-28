package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class Head extends BodySegment {

	private final double metabolicRate;
	
	public Head(int length, int thickness, ColorByte hue, double metabolicRate) {
		super(length, thickness, hue, new PassNerve(), 0, null);
		this.metabolicRate = metabolicRate;
	}

	public double getMetabolicRate() {
		return metabolicRate;
	}

	@Override
	protected Vector calculateStartPoint() {
		return Vector.ZERO;
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getAngleToParent();
	}
	
	protected Vector getMainAxis() {
		return getVector().normalize(1).invert();
	}

	@Override
	protected Vector calculateCenterOfMass() {
		return getVector().by(0.5);
	}

	@Override
	protected double calculateAngleToParent(double targetAngle, ForceField forceField) {
		return getAngleToParent();
	}

	@Override
	protected double getForcedBend() {
		// the head never bends. (OK, this hierarchy is starting
		// to look stupid. TODO: go back to making the head
		// a sibling of BodySegment rather than a subclass
		return 0;
	}
}