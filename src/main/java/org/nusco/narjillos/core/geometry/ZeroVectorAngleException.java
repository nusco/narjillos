package org.nusco.narjillos.core.geometry;

public class ZeroVectorAngleException extends Exception {

	private static final long serialVersionUID = 1L;

	public ZeroVectorAngleException() {
		super("Illegal operation: trying to get angle of zero-length vector");
	}
}
