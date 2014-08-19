package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class Head extends Organ {

	private final double metabolicRate;
	private Vector startPoint = Vector.ZERO;
	
	public Head(int length, int thickness, ColorByte hue, double metabolicRate) {
		super(length, thickness, hue, null, new PassNerve());
		this.metabolicRate = metabolicRate;
	}

	public double getMetabolicRate() {
		return metabolicRate;
	}
	
	Vector getMainAxis() throws ZeroVectorException {
		return getVector().normalize(1).invert();
	}

	@Override
	protected Vector calculateStartPoint() {
		return startPoint;
	}

	void setPosition(Vector startPoint, double angle) {
		// we already reset the cache in setAngleToParent(), so
		// no need to do it twice here
		this.startPoint  = startPoint;
		setAngleToParent(angle);
	}

	public void move(Vector translation, int rotation) {
		Vector newStartPoint = getStartPoint().plus(translation);
		double newAngleToParent = getAngleToParent() + rotation;
		setPosition(newStartPoint, newAngleToParent);
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
	protected double calculateNewAngleToParent(double targetAngle, double skewing) {
		// The head never rotates on its own. It must be
		// explicitly repositioned by its client.
		return getAngleToParent();
	}

	@Override
	protected double getForcedBend() {
		return 0;
	}
}