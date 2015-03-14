package org.nusco.narjillos.utilities;


public class AncestryAppState implements AppState {
	private static final int FRAMES_PERIOD = 1000 / 60;

	@Override
	public Light getLight() {
		return Light.ON;
	}

	@Override
	public Effects getEffects() {
		return Effects.ON;
	}

	@Override
	public Speed getSpeed() {
		return Speed.REALTIME;
	}

	@Override
	public int getFramesPeriod() {
		return FRAMES_PERIOD;
	}
}
