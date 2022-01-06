package org.nusco.narjillos.core.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

public class BoundingBoxTest {

	@Test
	public void definesTheBoundariesOfASegment() {
		BoundingBox boundingBox = createBoundingBox(10, 20, 30, 40);

		assertEquals(10, boundingBox.left, 0.0);
		assertEquals(40, boundingBox.right, 0.0);
		assertEquals(20, boundingBox.bottom, 0.0);
		assertEquals(60, boundingBox.top, 0.0);
	}

	@Test
	public void canBeUnited() {
		Set<BoundingBox> shapes = new LinkedHashSet<>();
		shapes.add(new BoundingBox(10, 20, 30, 60));
		shapes.add(new BoundingBox(15, 30, -100, 40));
		BoundingBox boundingBox = BoundingBox.union(shapes);

		assertEquals(10, boundingBox.left, 0.0);
		assertEquals(30, boundingBox.right, 0.0);
		assertEquals(-100, boundingBox.bottom, 0.0);
		assertEquals(60, boundingBox.top, 0.0);
	}

	@Test
	public void worksWithNegativeBoundaries() {
		BoundingBox boundingBox = createBoundingBox(-10, -20, 30, 40);

		assertEquals(-10, boundingBox.left, 0.0);
		assertEquals(20, boundingBox.right, 0.0);
		assertEquals(-20, boundingBox.bottom, 0.0);
		assertEquals(20, boundingBox.top, 0.0);
	}

	@Test
	public void automaticallyFixesInvertedCoordinates() {
		BoundingBox boundingBox = new BoundingBox(35, 10, 30, 20);

		assertEquals(10, boundingBox.left, 0.0);
		assertEquals(35, boundingBox.right, 0.0);
		assertEquals(20, boundingBox.bottom, 0.0);
		assertEquals(30, boundingBox.top, 0.0);
	}

	@Test
	public void worksWithAnySegmentOrientation() {
		BoundingBox boundingBox = createBoundingBox(35, 40, -10, -20);

		assertEquals(25, boundingBox.left, 0.0);
		assertEquals(35, boundingBox.right, 0.0);
		assertEquals(20, boundingBox.bottom, 0.0);
		assertEquals(40, boundingBox.top, 0.0);
	}

	@Test(expected = RuntimeException.class)
	public void cannotBeEmpty() {
		BoundingBox.union(new LinkedHashSet<>());
	}

	@Test
	public void overlapsWithAnotherBoundingBox() {
		BoundingBox boundingBox1 = createBoundingBox(0, 0, 10, 10);
		BoundingBox boundingBox2 = createBoundingBox(0, 9, 10, 10);

		assertTrue(boundingBox1.overlaps(boundingBox2));
	}

	@Test
	public void doesNotOverlapIfItIsToTheRightOfTheOtherBoundingBox() {
		BoundingBox boundingBox1 = createBoundingBox(0, 0, 10, 10);
		BoundingBox boundingBox2 = createBoundingBox(11, 0, 10, 10);

		assertFalse(boundingBox1.overlaps(boundingBox2));
	}

	@Test
	public void doesNotOverlapIfItIsToTheLeftOfTheOtherBoundingBox() {
		BoundingBox boundingBox1 = createBoundingBox(0, 0, 10, 10);
		BoundingBox boundingBox2 = createBoundingBox(-11, 0, 10, 10);

		assertFalse(boundingBox1.overlaps(boundingBox2));
	}

	@Test
	public void doesNotOverlapIfItIsBelowTheOtherBoundingBox() {
		BoundingBox boundingBox1 = createBoundingBox(0, 0, 10, 10);
		BoundingBox boundingBox2 = createBoundingBox(0, 11, 10, 10);

		assertFalse(boundingBox1.overlaps(boundingBox2));
	}

	@Test
	public void doesNotOverlapIfItIsAboveTheOtherBoundingBox() {
		BoundingBox boundingBox1 = createBoundingBox(0, 0, 10, 10);
		BoundingBox boundingBox2 = createBoundingBox(0, -11, 10, 10);

		assertFalse(boundingBox1.overlaps(boundingBox2));
	}

	@Test
	public void doesNotOverlapIfItTouchesOnTheEdge() {
		BoundingBox boundingBox1 = createBoundingBox(0, 0, 10, 10);
		BoundingBox boundingBox2 = createBoundingBox(0, 10, 20, 20);

		assertFalse(boundingBox1.overlaps(boundingBox2));
	}

	@Test
	public void overlapsItself() {
		BoundingBox boundingBox = createBoundingBox(0, 0, 10, 10);

		assertTrue(boundingBox.overlaps(boundingBox));
	}

	@Test
	public void checksForContainment() {
		BoundingBox big = new BoundingBox(0, 30, 0, 60);
		BoundingBox tall = new BoundingBox(1, 29, 1, 100);
		BoundingBox small = new BoundingBox(0, 0, 10, 10);

		assertTrue(small.isContainedIn(big));
		assertFalse(big.isContainedIn(small));

		assertFalse(small.isContainedIn(tall));

		assertTrue(big.isContainedIn(big));
	}

	@Test
	public void neverOverlapsIfBothBoxesHaveAreaZero() {
		BoundingBox boundingBox = createBoundingBox(10, 10, 0, 0);

		assertFalse(boundingBox.overlaps(boundingBox));
	}

	@Test
	public void canStillOverlapIfTheFirstBoxHasAreaZero() {
		BoundingBox boundingBox1 = createBoundingBox(5, 5, 0, 0);
		BoundingBox boundingBox2 = createBoundingBox(-10, -10, 20, 20);

		assertTrue(boundingBox1.overlaps(boundingBox2));
	}

	@Test
	public void canStillOverlapIfTheSecondBoxHasAreaZero() {
		BoundingBox boundingBox1 = createBoundingBox(0, 0, 10, 10);
		BoundingBox boundingBox2 = createBoundingBox(5, 5, 0, 0);

		assertTrue(boundingBox1.overlaps(boundingBox2));
	}

	@Test
	public void canBePunctiform() {
		BoundingBox boundingBox = BoundingBox.punctiform(Vector.cartesian(10, 20));

		assertEquals(boundingBox, new BoundingBox(10, 10, 20, 20));
	}

	private BoundingBox createBoundingBox(int x, int y, int width, int height) {
		return new Segment(Vector.cartesian(x, y), Vector.cartesian(width, height)).getBoundingBox();
	}
}
