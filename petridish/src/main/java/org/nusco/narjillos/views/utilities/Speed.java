package org.nusco.narjillos.views.utilities;

public enum Speed {
	REALTIME("real time"),
	HIGH("high speed");

	private String description;

	private Speed(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
