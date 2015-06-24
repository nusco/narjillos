package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.core.physics.Angle;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.physics.ZeroVectorAngleException;
import org.nusco.narjillos.core.utilities.Configuration;

/**
 * The mouth of a creature. It reactively points towards food.
 */
public class Mouth {

	private double directionAngle = 0;
	
	public void tick(Vector position, Vector target, double rotation) {
		try {
			double absoluteTargetAngle = target.minus(position).getAngle();
			double relativeTargetAngle = Angle.normalize(absoluteTargetAngle - rotation);
			
			boolean targetIsInViewField = Math.abs(relativeTargetAngle) < Configuration.CREATURE_LATERAL_VIEWFIELD;
			if (targetIsInViewField) {
				shiftSmoothlyTowards(absoluteTargetAngle);
				return;
			}
			
			boolean targetJustExitedTheViewField = Math.abs(directionAngle) < Configuration.CREATURE_LATERAL_VIEWFIELD;
			if (targetJustExitedTheViewField)
				shiftSmoothlyTowards(rotation + Configuration.CREATURE_LATERAL_VIEWFIELD * Math.signum(relativeTargetAngle));

			// else keep pointing in the same direction
		} catch (ZeroVectorAngleException e) {
			shiftSmoothlyTowards(0);
		}
	}

	public Vector getDirection() {
		return Vector.polar(directionAngle, 1);
	}

	private void shiftSmoothlyTowards(double angle) {
		double angleDifference = Angle.normalize(angle) - directionAngle;
		if (Math.abs(angleDifference) < 1)
			return;
		directionAngle += Math.signum(angleDifference);
	}
}
