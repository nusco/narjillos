package org.nusco.narjillos.application.utilities;

public interface AppState {

	public Light getLight();

	public Effects getEffects();

	public Speed getSpeed();

	public int getFramesPeriod();
}