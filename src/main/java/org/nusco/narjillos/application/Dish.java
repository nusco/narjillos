package org.nusco.narjillos.application;

import org.nusco.narjillos.experiment.environment.Environment;

/**
 * A microscope dish. It contains an environment for narjillos.
 */
public interface Dish {

	boolean tick();

	Environment getEnvironment();

	String getStatistics();

	void terminate();

	boolean isBusy();
}
