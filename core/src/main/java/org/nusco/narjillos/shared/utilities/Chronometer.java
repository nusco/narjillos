package org.nusco.narjillos.shared.utilities;

public class Chronometer {

	private int ticks = 0;
	private long startTime = 0;
	private int ticksInLastSecond = 0;
	
	public synchronized void tick() {
		if (System.currentTimeMillis() - startTime > 1000) {
			startTime = System.currentTimeMillis();
			ticksInLastSecond = ticks;
			ticks = 0;
		}
		ticks ++;
	}

	public synchronized int getTicksInLastSecond() {
		return ticksInLastSecond;
	}
}
