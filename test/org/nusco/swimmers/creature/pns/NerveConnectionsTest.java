package org.nusco.swimmers.creature.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

class DoubleNerve extends Nerve {
	@Override
	public Vector process(Vector inputSignal) {
		return Vector.cartesian(inputSignal.getX() * 2, inputSignal.getY() * 2);
	}	
}

public class NerveConnectionsTest {

	@Test
	public void passesSignalToChildren() {
		Nerve nerve1 = new DoubleNerve();
		Nerve nerve2 = new DoubleNerve();
		Nerve nerve3 = new DoubleNerve();
		Nerve nerve4 = new DoubleNerve();
		nerve1.connectTo(nerve2);
		nerve1.connectTo(nerve3);
		nerve3.connectTo(nerve4);

		nerve1.send(Vector.cartesian(1, 1));
		
		assertEquals(Vector.cartesian(2, 2), nerve1.readOutputSignal());
		assertEquals(Vector.cartesian(4, 4), nerve2.readOutputSignal());
		assertEquals(Vector.cartesian(4, 4), nerve3.readOutputSignal());
		assertEquals(Vector.cartesian(8, 8), nerve4.readOutputSignal());
	}
}
