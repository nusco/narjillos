package org.nusco.narjillos.core.things;

import java.util.Objects;

class HashedLocation {

	private static final long GRID_SIZE = 300;

	private final long lx;

	private final long ly;

	public HashedLocation(long lx, long ly) {
		this.lx = lx;
		this.ly = ly;
	}

	public HashedLocation(Thing thing) {
		this.lx = toGrid(thing.getCenter().x);
		this.ly = toGrid(thing.getCenter().y);
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

	private long toGrid(double n) {
		return (long) (n / GRID_SIZE);
	}
}
