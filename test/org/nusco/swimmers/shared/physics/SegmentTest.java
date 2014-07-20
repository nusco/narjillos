package org.nusco.swimmers.shared.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SegmentTest {
	@Test
	public void isIdentifiedByTwoVectors() {
		Segment segment = new Segment(Vector.cartesian(10, 12), Vector.cartesian(13, 17));
		
		assertEquals(10, segment.startPoint.x, 0);
		assertEquals(12, segment.startPoint.y, 0);
		assertEquals(13, segment.vector.x, 0);
		assertEquals(17, segment.vector.y, 0);
	}

	@Test
	public void hasAMinimumDistanceFromAPoint() {
		Segment segment = new Segment(Vector.ZERO, Vector.cartesian(90, 0));
		
		assertEquals(10, segment.getMinimumDistanceFromPoint(Vector.cartesian(50, 10)), 0.001);
		assertEquals(0, segment.getMinimumDistanceFromPoint(Vector.cartesian(50, 0)), 0.001);
		assertEquals(30, segment.getMinimumDistanceFromPoint(Vector.cartesian(120, 0)), 0.001);
	}

	@Test
	public void canCalculateTheMinimumDistanceOnAZeroLengthSegment() {
		Segment segment = new Segment(Vector.cartesian(90, 0), Vector.cartesian(90, 0));
		
		assertEquals(40, segment.getMinimumDistanceFromPoint(Vector.cartesian(50, 0)), 0.001);
	}
}
