package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.Nerve;

public abstract class NeuralConnectionTest {
	
	@Test
	public void sendsNerveSignalsToItsChildren() {
		final ClickNerve nerve1 = new ClickNerve();
		final ClickNerve nerve2 = new ClickNerve();
		final ClickNerve nerve3 = new ClickNerve();

		MovingOrgan head = new Head(0, 0, 0, 0, 0, 1, 0.5);
		ConnectedOrgan child1 = head.addChild(new BodyPart(nerve1));
		child1.addChild(new BodyPart(nerve2));
		head.addChild(new BodyPart(nerve3));

		head.tick(0, 0, 1);
		
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
