package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.shared.physics.Vector;

public abstract class OrganConnectionTest {
	
	@Test
	public void sendsNerveSignalsToItsChildren() {
		final ClickNerve nerve1 = new ClickNerve();
		final ClickNerve nerve2 = new ClickNerve();
		final ClickNerve nerve3 = new ClickNerve();
		final ClickNerve nerve4 = new ClickNerve();

		BodyPart head = new Head(0, 0, 0, 1) {
			@Override
			public Nerve getNerve() {
				return nerve1;
			}
		};
		BodyPart child1 = head.sproutOrgan(nerve2);
		child1.sproutOrgan(nerve3);
		head.sproutOrgan(nerve4);

		head.tick(null);
		
		assertTrue(nerve1.clicked);
		assertTrue(nerve2.clicked);
		assertTrue(nerve3.clicked);
		assertTrue(nerve4.clicked);
	}
	
	class ClickNerve implements Nerve {
		public boolean clicked = false;
		
		@Override
		public Vector tick(Vector inputSignal) {
			clicked = true;
			return null;
		}		
	}
}
