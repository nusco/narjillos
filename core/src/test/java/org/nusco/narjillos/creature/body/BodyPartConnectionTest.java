package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.utilities.ColorByte;

public abstract class BodyPartConnectionTest {
	
	@Test
	public void sendsNerveSignalsToItsChildren() {
		final ClickNerve nerve1 = new ClickNerve();
		final ClickNerve nerve2 = new ClickNerve();
		final ClickNerve nerve3 = new ClickNerve();

		Organ head = new Head(0, 0, new ColorByte(0), 1);
		Organ child1 = head.addChild(new BodySegment(nerve1));
		child1.addChild(new BodySegment(nerve2));
		head.addChild(new BodySegment(nerve3));

		head.updateAngleToParent(0, 0);
		
		assertTrue(nerve1.clicked);
		assertTrue(nerve2.clicked);
		assertTrue(nerve3.clicked);
	}
	
	class ClickNerve implements Nerve {
		public boolean clicked = false;
		
		@Override
		public double tick(double inputSignal) {
			clicked = true;
			return 0;
		}		
	}
}
