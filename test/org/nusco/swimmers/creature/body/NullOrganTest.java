package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.NullOrgan;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.creature.pns.PassNerve;
import org.nusco.swimmers.physics.Vector;

public class NullOrganTest {

	@Test
	public void hasNoVisibleShape() {
		assertEquals(0, new NullOrgan(null).getLength(), 0.0);
		assertEquals(0, new NullOrgan(null).getThickness(), 0.0);
	}

	@Test
	public void hasAParent() {
		Organ head = new Head(10, 10, 100);
		Organ nullOrgan = head.sproutNullOrgan();

		assertEquals(head, nullOrgan.getParent());
	}

	@Test
	public void hasChildren() {
		Organ head = new Head(10, 10, 100);
		Organ nullOrgan = head.sproutNullOrgan();
		Organ child1 = nullOrgan.sproutOrgan(10, 10, 10, Side.RIGHT, 100);
		Organ child2 = nullOrgan.sproutOrgan(10, 10, -10, Side.LEFT, 100);
		
		List<Organ> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, nullOrgan.getChildren());
	}

	@Test
	public void itsGeometricDataIsAllZeros() {
		Organ head = new Head(10, 10, 100);
		Organ nullOrgan = head.sproutNullOrgan();

		assertEquals(0, nullOrgan.getLength());
		assertEquals(0, nullOrgan.getThickness());
		assertEquals(0, nullOrgan.getAngle(), 0);
		assertEquals(0, nullOrgan.getRGB(), 0);
	}

	@Test
	public void itsRelativeAngleIsTheSameAsItsParent() {
		Organ head = new Head(10, 10, 100);
		Organ child = head.sproutOrgan(10, 10, 45, Side.RIGHT, 100);
		Organ nullOrgan = child.sproutNullOrgan();

		assertEquals(45, nullOrgan.getRelativeAngle(), 0);
	}

	@Test
	public void itBeginsAndEndsWhereItsParentEnds() {
		Head head = new Head(15, 10, 100);
		head.placeAt(Vector.cartesian(20, 30));
		Organ nullOrgan = new NullOrgan(head).sproutNullOrgan();

		assertEquals(Vector.cartesian(35, 30), nullOrgan.getStartPoint());
		assertEquals(Vector.cartesian(35, 30), nullOrgan.getEndPoint());
	}

	@Test
	public void canSproutVisibleOrgans() {
		Organ child = new NullOrgan(new Head(0, 0, 0)).sproutOrgan(20, 12, 45, Side.RIGHT, 100);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
		assertEquals(45, child.getRelativeAngle(), 0);
	}

	@Test
	public void hasAPassNerve() {
		Nerve nerve = new NullOrgan(null).getNerve();
				
		assertEquals(PassNerve.class, nerve.getClass());
	}
}
