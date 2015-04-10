package org.nusco.narjillos.core.physics;

public class Angle {

	public static double normalize(double angle) {
		if(angle < -180)
			return angle + 360;
		if(angle > 180)
			return angle - 360;
		return angle;
	}
}
