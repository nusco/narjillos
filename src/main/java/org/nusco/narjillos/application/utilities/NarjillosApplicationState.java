package org.nusco.narjillos.application.utilities;

public class NarjillosApplicationState implements AppState {

	private static final int FRAMES_PER_SECOND_WITH_LIGHT_ON = 30;

	private static final int FRAMES_PER_SECOND_WITH_LIGHT_OFF = 5;

	private static final int FRAMES_PERIOD_WITH_LIGHT_ON = 1000 / FRAMES_PER_SECOND_WITH_LIGHT_ON;

	private static final int FRAMES_PERIOD_WITH_LIGHT_OFF = 1000 / FRAMES_PER_SECOND_WITH_LIGHT_OFF;

	private Light light = Light.ON;

	private Speed speed = Speed.REALTIME;

	private Effects effects = Effects.ON;

	@Override
	public Light getLight() {
		return light;
	}

	@Override
	public Effects getEffects() {
		return effects;
	}

	public Speed getSpeed() {
		return speed;
	}

	public void toggleLight() {
		switch (light) {
			case INFRARED -> this.light = Light.ON;
			case ON -> {
				this.light = Light.OFF;
				this.speed = Speed.FAST;
			}
			case OFF -> {
				this.light = Light.ON;
				this.speed = Speed.REALTIME;
			}
		}
	}

	public void toggleInfrared() {
		switch (light) {
			case INFRARED -> this.light = Light.ON;
			case ON -> this.light = Light.INFRARED;
			case OFF -> {
				this.light = Light.INFRARED;
				this.speed = Speed.REALTIME;
			}
		}
	}

	public void speedUp() {
		speed = speed.up();
	}

	public void speedDown() {
		speed = speed.down();
	}

	public int getFramesPeriod() {
		if (getLight() == Light.OFF)
			return FRAMES_PERIOD_WITH_LIGHT_OFF;
		else
			return FRAMES_PERIOD_WITH_LIGHT_ON;
	}

	public void toggleEffects() {
		effects = effects.toggle();
	}

	public void toggleSpeed() {
		speed = speed.toggle();
	}
}
