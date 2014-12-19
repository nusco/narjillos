package org.nusco.narjillos.shared.physics;

public class Angle {

	public static double normalize(double angle) {
		// TODO: why not use modulos?
		if(angle < -180)
			return angle + 360;
		if(angle > 180)
			return angle - 360;
		return angle;
	}
}
