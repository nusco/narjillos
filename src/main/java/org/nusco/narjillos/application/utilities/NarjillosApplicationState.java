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

	@Override
	public Speed getSpeed() {
		return speed;
	}

	public void toggleLight() {
		switch (light) {
		case INFRARED:
			this.light = Light.ON;
			break;
		case ON:
			this.light = Light.OFF;
			this.speed = Speed.FAST;
			break;
		case OFF:
			this.light = Light.ON;
			this.speed = Speed.REALTIME;
			break;
		}
	}

	public void toggleInfrared() {
		switch (light) {
		case INFRARED:
			this.light = Light.ON;
			break;
		case ON:
			this.light = Light.INFRARED;
			break;
		case OFF:
			this.light = Light.INFRARED;
			this.speed = Speed.REALTIME;
			break;
		}
	}
	
	public void speedUp() {
		speed = speed.up();
	}
	
	public void speedDown() {
		speed = speed.down();
	}

	@Override
	public int getFramesPeriod() {
		if (getLight() == Light.OFF)
			return FRAMES_PERIOD_WITH_LIGHT_OFF;
		else
			return FRAMES_PERIOD_WITH_LIGHT_ON;
	}

	public void toggleEffects() {
		effects = effects.toggle();
	}
}
