package org.nusco.narjillos.embryogenesis.bodyplan;

import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.shared.utilities.ColorByte;

class MockOrgan extends MovingOrgan {

	private final int id;
	private final int sign;

	public MockOrgan(int id, ConnectedOrgan parent, int sign) {
		super(0, 0, new ColorByte(0), parent, null);
		this.id = id;
		this.sign = sign;
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
	public String toString() {
		StringBuffer result = new StringBuffer();
		if (sign < 0)
			result.append("^");
		result.append(Integer.toString(id));
		
		ConnectedOrgan[] children = getChildren().toArray(new MovingOrgan[0]);
		if (children.length == 0)
			return result.toString();
		
		result.append("-");
		
		if (children.length == 1) {
			result.append(children[0]);
			return result.toString();
		}
		
		result.append("(");
		for (int i = 0; i < children.length; i++) {
			result.append(children[i].toString());
			if (i < children.length - 1)
				result.append(", ");
		}
		result.append(")");
		return result.toString();
	}
}
