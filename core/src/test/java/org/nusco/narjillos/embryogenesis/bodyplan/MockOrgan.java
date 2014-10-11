package org.nusco.narjillos.embryogenesis.bodyplan;

import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.shared.utilities.ColorByte;

class MockOrgan extends Organ {

	private final int id;

	public MockOrgan(int id, Organ parent) {
		super(0, 0, new ColorByte(0), parent, null);
		this.id = id;
	}

	@Override
	protected double calculateNewAngleToParent(double targetAngle, double angleToTarget) {
		return 0;
	}

	@Override
	protected double getMetabolicRate() {
		return 0;
	}

	@Override
	protected double calculateAbsoluteAngle() {
		return 0;
	}

	@Override
	public int getId() {
		return id;
	}
}
