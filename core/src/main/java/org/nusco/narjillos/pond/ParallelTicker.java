package org.nusco.narjillos.pond;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.nusco.narjillos.shared.things.Thing;

class ParallelTicker {

	private final TickThread[] workers;

	public ParallelTicker(int numberOfWorkers) {
		workers = new TickThread[numberOfWorkers];
		for (int i = 0; i < numberOfWorkers; i++) {
			workers[i] = new TickThread();
			workers[i].start();
		}
	}

	public void tick(Set<? extends Thing> things) {
		int counter = 0;
		for (Thing thing : things) {
			workers[counter % workers.length].add(thing);
			counter++;
		}
	}
}

class TickThread extends Thread {

	private final BlockingQueue<Thing> things = new LinkedBlockingQueue<>();

	public void add(Thing thing) {
		try {
			things.put(thing);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isPaused() {
		return things.isEmpty();
	}

	@Override
	public void run() {
		while (true)
			try {
				things.take().tick();
			} catch (InterruptedException e) {
			}
	}
}
