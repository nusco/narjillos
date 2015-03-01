package org.nusco.narjillos;

import org.nusco.narjillos.ecosystem.Environment;

public abstract class Lab {

	public abstract Environment getEnvironment();

	public abstract boolean tick();

	public abstract void terminate();

	public abstract boolean isBusy();

	public abstract String getPerformanceStatistics();
}
