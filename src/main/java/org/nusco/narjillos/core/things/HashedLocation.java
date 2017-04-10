package org.nusco.narjillos.core.things;

import java.util.Objects;
/**
 * A location in Space. There is no grid location (0, *) or (*,0). Both coordinate
 * move from location -1 to location 1.
 */
class HashedLocation {

	public static final long GRID_SIZE = 400;

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

	static  long inc(long coordinate) {
		long result = coordinate + 1;
		return result == 0 ? 1 : result;
	}

	static long dec(long coordinate) {
		long result = coordinate - 1;
		return result == 0 ? -1 : result;
	}

	private static long toGrid(double n) {
		if (n < 0)
			return -toGrid(-n);
		return (long) (n / GRID_SIZE) + 1;
	}
}
