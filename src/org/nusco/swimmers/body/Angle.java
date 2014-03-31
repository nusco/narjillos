package org.nusco.swimmers.body;

public class Angle {
	protected static double normalize(double angleInDegrees) {
		double angle0to360 = ((angleInDegrees % 360) + 360) % 360;
		if(angle0to360 <= 180)
			return angle0to360;
		return -(360 - angle0to360);
	}
}
