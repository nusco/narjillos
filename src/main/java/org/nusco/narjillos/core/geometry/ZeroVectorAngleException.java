package org.nusco.narjillos.core.geometry;

import java.io.Serial;

public class ZeroVectorAngleException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	public ZeroVectorAngleException() {
		super("Illegal operation: trying to get angle of zero-length vector");
	}
}
