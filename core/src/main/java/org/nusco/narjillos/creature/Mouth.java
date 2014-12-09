package org.nusco.narjillos.creature;

import org.nusco.narjillos.shared.physics.Angle;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;

public class Mouth {

	private final double LATERAL_VIEWFIELD = 135;

	private double directionAngle = 0;
	
	public void tick(Vector position, Vector target, double rotation) {
		try {
			double absoluteTargetAngle = target.minus(position).getAngle();
			double relativeTargetAngle = Angle.normalize(absoluteTargetAngle - rotation);
			
			boolean targetIsInViewField = Math.abs(relativeTargetAngle) < LATERAL_VIEWFIELD;
			if (targetIsInViewField) {
				pointTowards(absoluteTargetAngle);
				return;
			}
			
			boolean targetJustExitedTheViewField = Math.abs(directionAngle) < LATERAL_VIEWFIELD;
			if (targetJustExitedTheViewField)
				pointTowards(rotation + LATERAL_VIEWFIELD * Math.signum(relativeTargetAngle));

			// else keep pointing in the same direction
		} catch (ZeroVectorException e) {
			pointTowards(0);
		}
	}

	private void pointTowards(double angle) {
		double angleDifference = Angle.normalize(angle) - directionAngle;
		if (Math.abs(angleDifference) < 1)
			return;
		directionAngle += Math.signum(angleDifference);
	}

	public Vector getDirection() {
		return Vector.polar(directionAngle, 1);
	}

	@Override
	public boolean equals(Object obj) {
		return directionAngle == ((Mouth)obj).directionAngle;
	}
	
	@Override
	public int hashCode() {
		return (int)directionAngle;
	}
}
