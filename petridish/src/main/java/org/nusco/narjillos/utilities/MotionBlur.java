package org.nusco.narjillos.utilities;

public enum MotionBlur {
	ON,
	OFF;

	public MotionBlur toggle() {
		return (this == ON) ? OFF : ON;
	}
}
