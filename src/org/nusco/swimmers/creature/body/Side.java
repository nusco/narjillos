package org.nusco.swimmers.creature.body;

public enum Side {
	RIGHT,
	LEFT;
	
	public int toSign() {
		return (this == RIGHT) ? 1 : -1;
	}
}
