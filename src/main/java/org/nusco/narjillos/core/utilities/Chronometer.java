package org.nusco.narjillos.core.utilities;

/**
 * Counts ticks.
 */
public class Chronometer {

	private int totalTicks = 0;
	private transient int ticks = 0;
	private transient long lastSecondStartTime = 0;
	private transient int ticksInLastSecond = 0;
	
	public synchronized void tick() {
		if (System.currentTimeMillis() - lastSecondStartTime > 1000) {
			lastSecondStartTime = System.currentTimeMillis();
			ticksInLastSecond = ticks;
			ticks = 0;
		}
		ticks++;
		totalTicks++;
	}

	public synchronized long getTotalTicks() {
		return totalTicks;
	}

	public synchronized int getTicksInLastSecond() {
		return ticksInLastSecond;
	}
}
