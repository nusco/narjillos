package org.nusco.narjillos.core.things;

import java.util.Objects;

class HashedLocation {

	private static final long GRID_SIZE = 300;

	final long lx;

	final long ly;

	private HashedLocation(long lx, long ly) {
		this.lx = lx;
		this.ly = ly;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		HashedLocation that = (HashedLocation) o;
		return lx == that.lx && ly == that.ly;
	}

	@Override
	public int hashCode() {
		return Objects.hash(lx, ly);
	}

	@Override
	public String toString() {
		return String.format("[%s, %s]", lx, ly);
	}

	public static HashedLocation at(long lx, long ly) {
		return new HashedLocation(lx, ly);
	}

	public static HashedLocation ofCoordinates(double x, double y) {
		return new HashedLocation(toGrid(x), toGrid(y));
	}

	private static long toGrid(double n) {
		if (n < 0)
			return -toGrid(-n);
		return (long) (n / GRID_SIZE) + 1;
	}
}
