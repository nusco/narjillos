package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public abstract class OrganConnectionTest {
	
	@Test
	public void sendsNerveSignalsToItsChildren() {
		ClickNerve nerve1 = new ClickNerve();
		ClickNerve nerve2 = new ClickNerve();
		ClickNerve nerve3 = new ClickNerve();
		ClickNerve nerve4 = new ClickNerve();

		Organ head = new Head(nerve1);
		Organ child1 = head.sproutOrgan(nerve2);
		child1.sproutOrgan(nerve3);
		head.sproutOrgan(nerve4);

		head.tick(null);
		
		assertTrue(nerve1.clicked);
		assertTrue(nerve2.clicked);
		assertTrue(nerve3.clicked);
		assertTrue(nerve4.clicked);
	}
	
	class ClickNerve extends Nerve {
		public boolean clicked = false;
		
		@Override
		public Vector send(Vector inputSignal) {
			clicked = true;
			return null;
		}		
	}
}
