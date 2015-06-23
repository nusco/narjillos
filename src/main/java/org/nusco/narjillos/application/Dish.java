package org.nusco.narjillos.application;

import org.nusco.narjillos.ecosystem.Culture;

public interface Dish {

	public Culture getCulture();
	public void terminate();
	public boolean tick();
	public String getDishStatistics();
	public boolean isBusy();
}
