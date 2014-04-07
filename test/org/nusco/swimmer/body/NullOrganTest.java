package org.nusco.swimmer.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nusco.swimmer.physics.Vector;

public class NullOrganTest {

	@Test
	public void isInvisible() {
		assertFalse(new NullOrgan(null).isVisible());
	}

	@Test
	public void hasAParent() {
		VisibleOrgan head = new Head(10, 10, 100);
		Organ nullOrgan = head.sproutInvisibleOrgan();

		assertEquals(head, nullOrgan.getParent());
	}

	@Test
	public void hasChildren() {
		VisibleOrgan head = new Head(10, 10, 100);
		Organ nullOrgan = head.sproutInvisibleOrgan();
		Organ child1 = nullOrgan.sproutVisibleOrgan(10, 10, 10, 100);
		Organ child2 = nullOrgan.sproutVisibleOrgan(10, 10, -10, 100);
		
		List<Organ> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, nullOrgan.getChildren());
	}

	@Test
	public void doesntAppearAmongstChildrensAncestors() {
		VisibleOrgan head = new Head(10, 10, 100);
		Organ nullOrgan = head.sproutInvisibleOrgan();
		Organ child = nullOrgan.sproutVisibleOrgan(10, 10, 10, 100);
		
		assertEquals(head, child.getParent());
	}

	@Test
	public void itsGeometricDataIsAllZeros() {
		VisibleOrgan head = new Head(10, 10, 100);
		Organ nullOrgan = head.sproutInvisibleOrgan();

		assertEquals(0, nullOrgan.getLength());
		assertEquals(0, nullOrgan.getThickness());
		assertEquals(0, nullOrgan.getAngle(), 0);
		assertEquals(0, nullOrgan.getRGB(), 0);
	}

	@Test
	public void itsRelativeAngleIsTheSameAsItsParent() {
		VisibleOrgan head = new Head(10, 10, 100);
		Organ child = head.sproutVisibleOrgan(10, 10, 45, 100);
		Organ nullOrgan = child.sproutInvisibleOrgan();

		assertEquals(45, nullOrgan.getRelativeAngle(), 0);
	}

	@Test
	public void itBeginsAndEndsWhereItsParentEnds() {
		Head head = new Head(15, 10, 100);
		head.placeAt(Vector.cartesian(20, 30));
		Organ nullOrgan = new NullOrgan(head).sproutInvisibleOrgan();

		assertEquals(Vector.cartesian(35, 30), nullOrgan.getStartPoint());
		assertEquals(Vector.cartesian(35, 30), nullOrgan.getEndPoint());
	}

	@Test
	public void canSproutVisibleOrgans() {
		Organ child = new NullOrgan(null).sproutVisibleOrgan(20, 12, 45, 100);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
		assertEquals(45, child.getRelativeAngle(), 0);
	}

	@Test
	public void canSproutInvisibleOrgans() {
		Organ child = new NullOrgan(null).sproutInvisibleOrgan();
		assertFalse(child.isVisible());
	}
}
