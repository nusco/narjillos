package org.nusco.narjillos.core.geometry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class BoundingBoxTest {

	@Test
	public void definesTheBoundariesOfASegment() {
		BoundingBox boundingBox = createBoundingBox(10, 20, 30, 40);

		assertThat(boundingBox.left).isEqualTo(10);
		assertThat(boundingBox.right).isEqualTo(40);
		assertThat(boundingBox.bottom).isEqualTo(20);
		assertThat(boundingBox.top).isEqualTo(60);
	}

	@Test
	public void canBeUnited() {
		Set<BoundingBox> shapes = new LinkedHashSet<>();
		shapes.add(new BoundingBox(10, 20, 30, 60));
		shapes.add(new BoundingBox(15, 30, -100, 40));
		var boundingBox = BoundingBox.union(shapes);

		assertThat(boundingBox.left).isEqualTo(10);
		assertThat(boundingBox.right).isEqualTo(30);
		assertThat(boundingBox.bottom).isEqualTo(-100);
		assertThat(boundingBox.top).isEqualTo(60);
	}

	@Test
	public void worksWithNegativeBoundaries() {
		var boundingBox = createBoundingBox(-10, -20, 30, 40);

		assertThat(boundingBox.left).isEqualTo(-10);
		assertThat(boundingBox.right).isEqualTo(20);
		assertThat(boundingBox.bottom).isEqualTo(-20);
		assertThat(boundingBox.top).isEqualTo(20);
	}

	@Test
	public void automaticallyFixesInvertedCoordinates() {
		var boundingBox = new BoundingBox(35, 10, 30, 20);

		assertThat(boundingBox.left).isEqualTo(10);
		assertThat(boundingBox.right).isEqualTo(35);
		assertThat(boundingBox.bottom).isEqualTo(20);
		assertThat(boundingBox.top).isEqualTo(30);
	}

	@Test
	public void worksWithAnySegmentOrientation() {
		var boundingBox = createBoundingBox(35, 40, -10, -20);

		assertThat(boundingBox.left).isEqualTo(25);
		assertThat(boundingBox.right).isEqualTo(35);
		assertThat(boundingBox.bottom).isEqualTo(20);
		assertThat(boundingBox.top).isEqualTo(40);
	}

	@Test
	public void cannotBeEmpty() {
		assertThatThrownBy(() -> BoundingBox.union(new LinkedHashSet<>()))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	public void overlapsWithAnotherBoundingBox() {
		var boundingBox1 = createBoundingBox(0, 0, 10, 10);
		var boundingBox2 = createBoundingBox(0, 9, 10, 10);

		assertThat(boundingBox1.overlaps(boundingBox2)).isTrue();
	}

	@Test
	public void doesNotOverlapIfItIsToTheRightOfTheOtherBoundingBox() {
		var boundingBox1 = createBoundingBox(0, 0, 10, 10);
		var boundingBox2 = createBoundingBox(11, 0, 10, 10);

		assertThat(boundingBox1.overlaps(boundingBox2)).isFalse();
	}

	@Test
	public void doesNotOverlapIfItIsToTheLeftOfTheOtherBoundingBox() {
		var boundingBox1 = createBoundingBox(0, 0, 10, 10);
		var boundingBox2 = createBoundingBox(-11, 0, 10, 10);

		assertThat(boundingBox1.overlaps(boundingBox2)).isFalse();
	}

	@Test
	public void doesNotOverlapIfItIsBelowTheOtherBoundingBox() {
		var boundingBox1 = createBoundingBox(0, 0, 10, 10);
		var boundingBox2 = createBoundingBox(0, 11, 10, 10);

		assertThat(boundingBox1.overlaps(boundingBox2)).isFalse();
	}

	@Test
	public void doesNotOverlapIfItIsAboveTheOtherBoundingBox() {
		var boundingBox1 = createBoundingBox(0, 0, 10, 10);
		var boundingBox2 = createBoundingBox(0, -11, 10, 10);

		assertThat(boundingBox1.overlaps(boundingBox2)).isFalse();
	}

	@Test
	public void doesNotOverlapIfItTouchesOnTheEdge() {
		var boundingBox1 = createBoundingBox(0, 0, 10, 10);
		var boundingBox2 = createBoundingBox(0, 10, 20, 20);

		assertThat(boundingBox1.overlaps(boundingBox2)).isFalse();
	}

	@Test
	public void overlapsItself() {
		var boundingBox = createBoundingBox(0, 0, 10, 10);

		assertThat(boundingBox.overlaps(boundingBox)).isTrue();
	}

	@Test
	public void checksForContainment() {
		var big = new BoundingBox(0, 30, 0, 60);
		var tall = new BoundingBox(1, 29, 1, 100);
		var small = new BoundingBox(0, 0, 10, 10);

		assertThat(small.isContainedIn(big)).isTrue();
		assertThat(big.isContainedIn(small)).isFalse();

		assertThat(small.isContainedIn(tall)).isFalse();

		assertThat(big.isContainedIn(big)).isTrue();
	}

	@Test
	public void neverOverlapsIfBothBoxesHaveAreaZero() {
		var boundingBox = createBoundingBox(10, 10, 0, 0);

		assertThat(boundingBox.overlaps(boundingBox)).isFalse();
	}

	@Test
	public void canStillOverlapIfTheFirstBoxHasAreaZero() {
		var boundingBox1 = createBoundingBox(5, 5, 0, 0);
		var boundingBox2 = createBoundingBox(-10, -10, 20, 20);

		assertThat(boundingBox1.overlaps(boundingBox2)).isTrue();
	}

	@Test
	public void canStillOverlapIfTheSecondBoxHasAreaZero() {
		var boundingBox1 = createBoundingBox(0, 0, 10, 10);
		var boundingBox2 = createBoundingBox(5, 5, 0, 0);

		assertThat(boundingBox1.overlaps(boundingBox2)).isTrue();
	}

	@Test
	public void canBePunctiform() {
		var boundingBox = BoundingBox.punctiform(Vector.cartesian(10, 20));

		assertThat(boundingBox).isEqualTo(new BoundingBox(10, 10, 20, 20));
	}

	private BoundingBox createBoundingBox(int x, int y, int width, int height) {
		return new Segment(Vector.cartesian(x, y), Vector.cartesian(width, height)).getBoundingBox();
	}
}
