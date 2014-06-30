package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.ConnectiveTissue;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.creature.body.pns.PassNerve;
import org.nusco.swimmers.shared.physics.Vector;

public class ConnectiveTissueSpecificTest {

	@Test
	public void isAVectorWithAP() {
		Organ head = new Head(10, 10, 100);
		Organ connectiveTissue = head.sproutConnectiveTissue();

		assertEquals(head, connectiveTissue.getParent());
	}

	@Test
	public void hasChildren() {
		Organ head = new Head(10, 10, 100);
		Organ connectiveTissue = head.sproutConnectiveTissue();
		Organ child1 = connectiveTissue.sproutOrgan(10, 10, 10, 100);
		Organ child2 = connectiveTissue.sproutOrgan(10, 10, -10, 100);
		
		List<Organ> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, connectiveTissue.getChildren());
	}

	@Test
	public void itsGeometricDataHasSimpleDefaultValues() {
		Organ head = new Head(10, 10, 100);
		Organ connectiveTissue = head.sproutConnectiveTissue();

		assertEquals(0, connectiveTissue.getLength());
		assertEquals(0, connectiveTissue.getThickness());
	}

	@Test
	public void itsColorIsTheSameAsItsParents() {
		Organ head = new Head(10, 10, 100);
		Organ connectiveTissue = head.sproutConnectiveTissue();

		assertEquals(100, connectiveTissue.getColor(), 0);
	}
	
	@Test
	public void itBeginsAndEndsWhereItsParentEnds() {
		Head head = new Head(15, 10, 100);
		Organ connectiveTissue = new ConnectiveTissue(head).sproutConnectiveTissue();

		assertEquals(Vector.cartesian(15, 0), connectiveTissue.getStartPoint());
		assertEquals(Vector.cartesian(15, 0), connectiveTissue.getEndPoint());
	}

	@Test
	public void canSproutVisibleOrgans() {
		Organ child = new ConnectiveTissue(new Head(0, 0, 0)).sproutOrgan(20, 12, 45, 100);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
	}

	@Test
	public void hasAPassNerve() {
		Nerve nerve = new ConnectiveTissue(null).getNerve();
				
		assertEquals(PassNerve.class, nerve.getClass());
	}
}
