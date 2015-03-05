package org.nusco.narjillos.utilities;

public class StoppableThread extends Thread {

	private volatile boolean askedToStop = false;
	
	public boolean hasBeenAskedToStop() {
		return askedToStop;
	}
	
	public void askToStop() {
		askedToStop = true;
	}
}
