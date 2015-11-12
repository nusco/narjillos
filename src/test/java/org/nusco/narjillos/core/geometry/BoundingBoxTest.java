package org.nusco.narjillos.core.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

public class BoundingBoxTest {
	
	private static class SegmentShapedThing implements SegmentShape {

		private final double x1;
		private final double y1;
		private final double x2;
		private final double y2;

		public SegmentShapedThing(double x1, double y1, double x2, double y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		@Override
		public Segment toSegment() {
			return new Segment(Vector.cartesian(x1, y1), Vector.cartesian(x2 - x1, y2 - y1));
		}
	}

	public final Set<SegmentShape> shapes = new LinkedHashSet<>();
	
	@Test
	public void definesTheBoundariesOfASegment() {
		shapes.add(new SegmentShapedThing(10, 20, 30, 40));
		BoundingBox boundingBox = new BoundingBox(shapes);
		
		assertEquals(10, boundingBox.left, 0.0);
		assertEquals(30, boundingBox.right, 0.0);
		assertEquals(20, boundingBox.bottom, 0.0);
		assertEquals(40, boundingBox.top, 0.0);
	}

	@Test
	public void worksWithNegativeBoundaries() {
		shapes.add(new SegmentShapedThing(-10, -20, 30, 40));
		BoundingBox boundingBox = new BoundingBox(shapes);
		
		assertEquals(-10, boundingBox.left, 0.0);
		assertEquals(30, boundingBox.right, 0.0);
		assertEquals(-20, boundingBox.bottom, 0.0);
		assertEquals(40, boundingBox.top, 0.0);
	}

	@Test
	public void worksWithSegmentsInAnyOrientation() {
		shapes.add(new SegmentShapedThing(30, 40, -10, -20));
		BoundingBox boundingBox = new BoundingBox(shapes);
		
		
		assertEquals(-10, boundingBox.left, 0.0);
		assertEquals(30, boundingBox.right, 0.0);
		assertEquals(-20, boundingBox.bottom, 0.0);
		assertEquals(40, boundingBox.top, 0.0);
	}

	@Test
	public void isEmptyIfThereAreNoSegments() {
		BoundingBox boundingBox = new BoundingBox(shapes);
		
		assertEquals(0, boundingBox.left, 0.0);
		assertEquals(0, boundingBox.right, 0.0);
		assertEquals(0, boundingBox.bottom, 0.0);
		assertEquals(0, boundingBox.top, 0.0);
	}

	@Test
	public void checksOverlaps() {
		Set<SegmentShape> shapes1 = new LinkedHashSet<>();
		shapes1.add(new SegmentShapedThing(0, 0, 10, 10));
		BoundingBox boundingBox1 = new BoundingBox(shapes1);
		
		Set<SegmentShape> shapes2 = new LinkedHashSet<>();
		shapes2.add(new SegmentShapedThing(0, 11, 20, 20));
		BoundingBox boundingBox2 = new BoundingBox(shapes2);
		
		Set<SegmentShape> shapes3 = new LinkedHashSet<>();
		shapes3.add(new SegmentShapedThing(-5, -5, 5, 5));
		BoundingBox boundingBox3 = new BoundingBox(shapes3);
		
		assertFalse(boundingBox1.overlaps(boundingBox2));
		assertTrue(boundingBox1.overlaps(boundingBox3));
		assertFalse(boundingBox2.overlaps(boundingBox3));
	}

	@Test
	public void overlapsItself() {
		shapes.add(new SegmentShapedThing(0, 0, 10, 10));
		BoundingBox boundingBox = new BoundingBox(shapes);
		
		assertTrue(boundingBox.overlaps(boundingBox));
	}

	@Test
	public void doesNotOverlapIfItTouchesOnTheEdge() {
		Set<SegmentShape> shapes1 = new LinkedHashSet<>();
		shapes1.add(new SegmentShapedThing(0, 0, 10, 10));
		BoundingBox boundingBox1 = new BoundingBox(shapes1);
		
		Set<SegmentShape> shapes2 = new LinkedHashSet<>();
		shapes2.add(new SegmentShapedThing(0, 10, 20, 20));
		BoundingBox boundingBox2 = new BoundingBox(shapes2);
		
		assertFalse(boundingBox1.overlaps(boundingBox2));
	}

	@Test
	public void neverOverlapsIfBothBoxesHaveAreaZero() {
		Set<SegmentShape> shapes = new LinkedHashSet<>();
		shapes.add(new SegmentShapedThing(10, 10, 10, 10));
		BoundingBox boundingBox = new BoundingBox(shapes);
		
		assertFalse(boundingBox.overlaps(boundingBox));
	}

	@Test
	public void canStillOverlapsIfOnlyOneBoxHasAreaZero() {
		Set<SegmentShape> shapes1 = new LinkedHashSet<>();
		shapes1.add(new SegmentShapedThing(0, 0, 0, 0));
		BoundingBox boundingBox1 = new BoundingBox(shapes1);
		
		Set<SegmentShape> shapes2 = new LinkedHashSet<>();
		shapes2.add(new SegmentShapedThing(-10, -10, 10, 10));
		BoundingBox boundingBox2 = new BoundingBox(shapes2);
		
		Set<SegmentShape> shapes3 = new LinkedHashSet<>();
		shapes3.add(new SegmentShapedThing(1, 1, 5, 5));
		BoundingBox boundingBox3 = new BoundingBox(shapes3);
		
		assertTrue(boundingBox1.overlaps(boundingBox2));
		assertFalse(boundingBox1.overlaps(boundingBox3));
	}
}
