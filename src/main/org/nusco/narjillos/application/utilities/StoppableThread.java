package org.nusco.narjillos.application.utilities;

/**
 * Because none of the methods in java.lang.Thread seems to do this simple job
 * without strings attached. Just check in the run() method, and exit if the
 * thread has been asked to stop.
 */
public class StoppableThread extends Thread {

	private volatile boolean askedToStop = false;

	public boolean hasBeenAskedToStop() {
		return askedToStop;
	}

	public void askToStop() {
		askedToStop = true;
	}
}
