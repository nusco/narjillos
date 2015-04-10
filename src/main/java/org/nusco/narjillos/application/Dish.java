package org.nusco.narjillos.application;

import org.nusco.narjillos.ecosystem.Culture;

abstract class Dish {

	public abstract Culture getCulture();

	public abstract boolean tick();

	public abstract void terminate();

	public abstract boolean isBusy();

	public abstract String getPerformanceStatistics();
}
