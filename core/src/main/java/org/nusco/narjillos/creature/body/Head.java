package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Angle;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public strictfp class Head extends Organ {

	private static final double WAVE_SIGNAL_FREQUENCY = 0.01;

	private final double percentEnergyToChildren;
	private final double metabolicRate;
	private Vector startPoint = Vector.ZERO;
	
	public Head(int length, int thickness, ColorByte hue, double metabolicRate, double percentEnergyToChildren) {
		super(length, thickness, hue, null, new WaveNerve(WAVE_SIGNAL_FREQUENCY * metabolicRate));
		this.percentEnergyToChildren = percentEnergyToChildren;
		this.metabolicRate = metabolicRate;
	}

	public double getPercentEnergyToChildren() {
		return percentEnergyToChildren;
	}

	@Override
	protected Vector calculateStartPoint() {
		return startPoint;
	}

	public void moveTo(Vector startPoint, double angle) {
		// we already reset the cache in setAngleToParent(), so
		// no need to do it twice here
		this.startPoint  = startPoint;
		setAngleToParent(angle);
	}

	public void moveBy(Vector translation, double rotation) {
		Vector newStartPoint = getStartPoint().plus(translation);
		double newAngleToParent = Angle.normalize(getAngleToParent() + rotation);
		moveTo(newStartPoint, newAngleToParent);
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

	@Override
	public double getMetabolicRate() {
		return metabolicRate;
	}
	
	public Vector getMainAxis() {
		return Vector.polar(getAbsoluteAngle(), 1).invert();
	}
}