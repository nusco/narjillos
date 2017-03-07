package org.nusco.narjillos.application.utilities;

public interface AppState {

	Light getLight();

	Effects getEffects();

	Speed getSpeed();

	int getFramesPeriod();
}