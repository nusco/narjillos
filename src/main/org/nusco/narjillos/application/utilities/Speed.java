package org.nusco.narjillos.application.utilities;

import javafx.scene.paint.Color;

public enum Speed {

	REALTIME("real time"), FAST("high speed"), SLOW("slow motion"), PAUSED("paused");

	private static final int TICKS_PER_SECOND = 30;

	private final String description;

	Speed(String description) {
		this.description = description;
	}

	public int getTicksPeriod() {
        return switch (this) {
            case REALTIME -> 1000 / TICKS_PER_SECOND;
            case FAST -> 1 / Integer.MAX_VALUE;
            case SLOW -> 10_000 / TICKS_PER_SECOND;
            case PAUSED -> 0;
        };
	}

	public Speed up() {
        return switch (this) {
            case PAUSED -> Speed.SLOW;
            case SLOW -> Speed.REALTIME;
            case REALTIME -> Speed.FAST;
            default -> this;
        };
	}

	public Speed down() {
        return switch (this) {
            case FAST -> Speed.REALTIME;
            case REALTIME -> Speed.SLOW;
            case SLOW -> Speed.PAUSED;
            default -> this;
        };
	}

	public Speed toggle() {
		return switch (this) {
			case REALTIME -> Speed.SLOW;
			default -> Speed.REALTIME;
		};
	}

	public Color getViewColor() {
        return switch (this) {
            case FAST -> Color.WHITE;
            case REALTIME -> Color.LIGHTGREEN;
            case SLOW -> Color.GREEN;
            case PAUSED -> Color.DARKGREEN;
        };
	}

	@Override
	public String toString() {
		return description;
	}
}
