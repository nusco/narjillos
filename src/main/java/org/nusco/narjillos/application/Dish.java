package org.nusco.narjillos.application;

import org.nusco.narjillos.experiment.environment.Environment;

/**
 * A microscope dish. It contains an environment for narjillos.
 */
public interface Dish {

	public boolean tick();
	public Environment getEnvironment();
	public String getStatistics();
	public void terminate();
	public boolean isBusy();
}
