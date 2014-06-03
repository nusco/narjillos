package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public abstract class OrganConnectionTest {
	
	@Test
	public void sendsNerveSignalsToItsChildren() {
		Organ head = new Head(new CounterNerve());
		Organ child1 = head.sproutOrgan(new CounterNerve());
		Organ child2 = head.sproutOrgan(new CounterNerve());
		Organ child1_1 = child1.sproutOrgan(new CounterNerve());
		
		head.setNerve(new CounterNerve());
		child1.setNerve(new CounterNerve());
		child2.setNerve(new CounterNerve());
		child1_1.setNerve(new CounterNerve());
		
		head.tick(Vector.ZERO_ONE);
		
		assertEquals(2, head.getNerve().getOutputSignal().getLength(), 0.0);
		assertEquals(3, child1.getNerve().getOutputSignal().getLength(), 0.0);
		assertEquals(3, child2.getNerve().getOutputSignal().getLength(), 0.0);
		assertEquals(4, child1_1.getNerve().getOutputSignal().getLength(), 0.0);
	}
	
	class CounterNerve extends Nerve {
		@Override
		public Vector process(Vector inputSignal) {
			return Vector.polar(0, inputSignal.getLength() + 1);
		}		
	}
}
