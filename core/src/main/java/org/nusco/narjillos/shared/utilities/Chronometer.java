package org.nusco.narjillos.shared.utilities;

/**
 * Counts ticks.
 */
public class Chronometer {

	private int totalTicks = 0;
	private int ticks = 0;
	private long startTime = 0;
	private transient int ticksInLastSecond = 0;
	
	public synchronized void tick() {
		if (System.currentTimeMillis() - startTime > 1000) {
			startTime = System.currentTimeMillis();
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
