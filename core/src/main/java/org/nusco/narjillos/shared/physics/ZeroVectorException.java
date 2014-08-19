package org.nusco.narjillos.shared.physics;

public class ZeroVectorException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ZeroVectorException() {
		super("Illegal operation on vector zero");
	}
}
