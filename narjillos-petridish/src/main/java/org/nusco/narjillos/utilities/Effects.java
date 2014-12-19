package org.nusco.narjillos.utilities;

public enum Effects {
	ON,
	OFF;

	public Effects toggle() {
		return (this == ON) ? OFF : ON;
	}
}
