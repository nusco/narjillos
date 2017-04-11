package org.nusco.narjillos.core.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SegmentTest {

	@Test
	public void isDescribedByAStartingPositionAndAVector() {
		Segment segment = new Segment(Vector.cartesian(10, 12), Vector.cartesian(13, 17));

		assertEquals(10, segment.getStartPoint().x, 0);
		assertEquals(12, segment.getStartPoint().y, 0);
		assertEquals(13, segment.getVector().x, 0);
		assertEquals(17, segment.getVector().y, 0);
	}

	@Test
	public void hasAMinimumDistanceFromAPoint() {
		Segment segment = new Segment(Vector.ZERO, Vector.cartesian(90, 0));

		assertEquals(10, Math.sqrt(segment.getMinimumDistanceFromPointSquared(Vector.cartesian(50, 10))), 0.001);
		assertEquals(0, Math.sqrt(segment.getMinimumDistanceFromPointSquared(Vector.cartesian(50, 0))), 0.001);
		assertEquals(30, Math.sqrt(segment.getMinimumDistanceFromPointSquared(Vector.cartesian(120, 0))), 0.001);
	}

	@Test
	public void canCalculateTheMinimumDistanceOnAZeroLengthSegment() {
		Segment segment = new Segment(Vector.cartesian(90, 0), Vector.cartesian(90, 0));

		assertEquals(40, Math.sqrt(segment.getMinimumDistanceFromPointSquared(Vector.cartesian(50, 0))), 0.001);
	}

	@Test
	public void hasAStartPoint() {
		Segment segment = new Segment(Vector.cartesian(1, 2), Vector.cartesian(4, 6));

		assertEquals(Vector.cartesian(1, 2), segment.getStartPoint());
	}

	@Test
	public void hasAnEndPoint() {
		Segment segment = new Segment(Vector.cartesian(1, 2), Vector.cartesian(4, 6));

		assertEquals(Vector.cartesian(5, 8), segment.getEndPoint());
	}

	@Test
	public void convertsToAString() {
		Segment segment = new Segment(Vector.cartesian(1, 2), Vector.cartesian(3, 4));

		assertEquals("[(1.0, 2.0), (3.0, 4.0)]", segment.toString());
	}
}
