package org.nusco.swimmers.pond;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.shared.things.Thing;

class ParallelTicker {

	private static final int NUMBER_OF_WORKERS = 3;

	public void runJobs(List<? extends Thing> list) {
		TickThread[] workers = createWorkerThreads(list);
		waitUntilDone(workers);
	}

	private TickThread[] createWorkerThreads(List<? extends Thing> list) {
		TickThread[] workers = new TickThread[NUMBER_OF_WORKERS];
		for (int i = 0; i < workers.length; i++)
			workers[i] = new TickThread();

		int counter = 0;
		for (final Thing thing : list) {
			workers[counter % NUMBER_OF_WORKERS].add(thing);
			counter++;
		}

		for (int i = 0; i < workers.length; i++)
			workers[i].start();
		return workers;
	}

	private void waitUntilDone(TickThread[] workers) {
		while (!allWorkersAreDone(workers));
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
	}

	private boolean allWorkersAreDone(TickThread[] workers) {
		for (TickThread thread : workers)
			if (!thread.isDone())
				return false;
		return true;
	}
}

class TickThread extends Thread {

	private List<Thing> things = new LinkedList<>();
	private boolean done = false;
	
	public void add(Thing thing) {
		things.add(thing);
	}
	
	@Override
	public void run() {
		for (Thing thing : things)
			thing.tick();
		setDone();
	}
	
	public synchronized boolean isDone() {
		return done;
	}
	
	public synchronized void setDone() {
		done = true;
	}
}
