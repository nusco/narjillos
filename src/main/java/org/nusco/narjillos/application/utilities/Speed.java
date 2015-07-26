package org.nusco.narjillos.application.utilities;

import javafx.scene.paint.Color;

public enum Speed {

	REALTIME("real time"), FAST("high speed"), SLOW("slow motion"), PAUSED("paused");

	private static final int TICKS_PER_SECOND = 30;

	private String description;

	private Speed(String description) {
		this.description = description;
	}

	public int getTicksPeriod() {
		switch (this) {
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

	public Speed up() {
		switch (this) {
		case PAUSED:
			return Speed.SLOW;
		case SLOW:
			return Speed.REALTIME;
		case REALTIME:
			return Speed.FAST;
		case FAST:
		default:
			return this;
		}
	}

	public Speed down() {
		switch (this) {
		case FAST:
			return Speed.REALTIME;
		case REALTIME:
			return Speed.SLOW;
		case SLOW:
			return Speed.PAUSED;
		case PAUSED:
		default:
			return this;
		}
	}

	public Speed toggle() {
		switch (this) {
		case REALTIME:
			return Speed.SLOW;
		default:
			return Speed.REALTIME;
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

	@Override
	public String toString() {
		return description;
	}
}
