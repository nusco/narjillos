package org.nusco.narjillos.core.geometry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

public class SegmentTest {

	private static final double EXPECTED_PRECISION = 0.001;

	@Test
	public void isDescribedByAStartingPositionAndAVector() {
		var segment = new Segment(Vector.cartesian(10, 12), Vector.cartesian(13, 17));

		assertThat(segment.getStartPoint().x).isEqualTo(10.0);
		assertThat(segment.getStartPoint().y).isEqualTo(12.0);
		assertThat(segment.getVector().x).isEqualTo(13.0);
		assertThat(segment.getVector().y).isEqualTo(17.0);
	}

	@Test
	public void hasAMinimumDistanceFromAPoint() {
		var segment = new Segment(Vector.ZERO, Vector.cartesian(90, 0));

		double distSqrd;

		distSqrd = Math.sqrt(segment.getMinimumDistanceFromPointSquared(Vector.cartesian(50, 10)));
		assertThat(distSqrd).isEqualTo(10.0, within(EXPECTED_PRECISION));

		distSqrd = Math.sqrt(segment.getMinimumDistanceFromPointSquared(Vector.cartesian(50, 0)));
		assertThat(distSqrd).isEqualTo(0.0, within(EXPECTED_PRECISION));

		distSqrd = Math.sqrt(segment.getMinimumDistanceFromPointSquared(Vector.cartesian(120, 0)));
		assertThat(distSqrd).isEqualTo(30.0, within(EXPECTED_PRECISION));
	}

	@Test
	public void canCalculateTheMinimumDistanceOnAZeroLengthSegment() {
		var segment = new Segment(Vector.cartesian(90, 0), Vector.cartesian(90, 0));

		double distSqrd = Math.sqrt(segment.getMinimumDistanceFromPointSquared(Vector.cartesian(50, 0)));
		assertThat(distSqrd).isEqualTo(40.0, within(EXPECTED_PRECISION));
	}

	@Test
	public void hasAStartPoint() {
		var segment = new Segment(Vector.cartesian(1, 2), Vector.cartesian(4, 6));

		assertThat(segment.getStartPoint()).isEqualTo(Vector.cartesian(1, 2));
	}

	@Test
	public void hasAnEndPoint() {
		var segment = new Segment(Vector.cartesian(1, 2), Vector.cartesian(4, 6));

		assertThat(segment.getEndPoint()).isEqualTo(Vector.cartesian(5, 8));
	}

	@Test
	public void convertsToAString() {
		var segment = new Segment(Vector.cartesian(1, 2), Vector.cartesian(3, 4));

		assertThat("[(1.0, 2.0), (3.0, 4.0)]").isEqualTo(segment.toString());
	}
}
