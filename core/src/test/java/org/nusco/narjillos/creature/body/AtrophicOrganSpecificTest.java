package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class AtrophicOrganSpecificTest {

	@Test
	public void isAVectorWithAP() {
		BodyPart head = new Head(10, 10, new ColorByte(100), 1);
		BodyPart connectiveTissue = head.sproutAtrophicOrgan();

		assertEquals(head, connectiveTissue.getParent());
	}

	@Test
	public void hasChildren() {
		BodyPart head = new Head(10, 10, new ColorByte(100), 1);
		BodyPart connectiveTissue = head.sproutAtrophicOrgan();
		BodyPart child1 = connectiveTissue.sproutOrgan(10, 10, 10, new ColorByte(100), 0);
		BodyPart child2 = connectiveTissue.sproutOrgan(10, 10, -10, new ColorByte(100), 0);
		
		List<BodyPart> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, connectiveTissue.getChildren());
	}

	@Test
	public void itsGeometricDataHasSimpleDefaultValues() {
		BodyPart head = new Head(10, 10, new ColorByte(100), 1);
		Organ connectiveTissue = head.sproutAtrophicOrgan();

		assertEquals(0, connectiveTissue.getLength());
		assertEquals(0, connectiveTissue.getThickness());
	}

	@Test
	public void itsColorIsTheSameAsItsParents() {
		BodyPart head = new Head(10, 10, new ColorByte(100), 1);
		Organ connectiveTissue = head.sproutAtrophicOrgan();

		assertEquals(new ColorByte(100), connectiveTissue.getColor());
	}
	
	@Test
	public void itBeginsAndEndsWhereItsParentEnds() {
		Head head = new Head(15, 10, new ColorByte(100), 1);
		Organ connectiveTissue = new AtrophicOrgan(head).sproutAtrophicOrgan();

		assertEquals(Vector.cartesian(15, 0), connectiveTissue.getStartPoint());
		assertEquals(Vector.cartesian(15, 0), connectiveTissue.getEndPoint());
	}

	@Test
	public void canSproutVisibleOrgans() {
		Organ child = new AtrophicOrgan(new Head(0, 0, new ColorByte(0), 1)).sproutOrgan(20, 12, 45, new ColorByte(100), 0);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
	}

	@Test
	public void hasAPassNerve() {
		Nerve nerve = new AtrophicOrgan(null).getNerve();
				
		assertEquals(PassNerve.class, nerve.getClass());
	}
}
