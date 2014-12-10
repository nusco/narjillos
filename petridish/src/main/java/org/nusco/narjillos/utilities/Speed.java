package org.nusco.narjillos.utilities;

import javafx.scene.paint.Color;

public enum Speed {
	
	REALTIME("real time"),
	FAST("high speed"),
	SLOW("slow motion"),
	PAUSED("paused");

	private static final int TICKS_PER_SECOND = 25;

	private String description;

	private Speed(String description) {
		this.description = description;
	}

	public int getTicksPeriod() {
		switch(this) {
		case REALTIME:
			return 1000 / TICKS_PER_SECOND;
		case FAST:
			return 1 / Integer.MAX_VALUE;
		case SLOW:
			return 10_000 / TICKS_PER_SECOND;
		case PAUSED:
			return 0;
		default:
			throw new RuntimeException("Unknown speed state: " + this);
		}
	}

	@Override
	public String toString() {
		return description;
	}

	public Speed shift() {
		switch (this) {
		case REALTIME:
			return Speed.FAST;
		case FAST:
			return Speed.SLOW;
		case SLOW:
			return Speed.REALTIME;
		case PAUSED:
			return Speed.REALTIME;
		default:
			throw new RuntimeException("Unknown speed state: " + this);
		}
	}

	public Color getViewColor() {
		switch (this) {
		case FAST:
			return Color.WHITE;
		case REALTIME:
			return Color.LIGHTGREEN;
		case SLOW:
			return Color.GREEN;
		case PAUSED:
			return Color.DARKGREEN;
		default:
			throw new RuntimeException("Unknown speed state: " + this);
		}
	}
}
