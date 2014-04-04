package org.nusco.swimmer.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.physics.Vector;

class DoubleNerve extends Nerve {
	@Override
	public Vector process(Vector inputSignal) {
		return new Vector(inputSignal.getX() * 2, inputSignal.getY() * 2);
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

		nerve1.send(new Vector(1, 1));
		
		assertEquals(new Vector(2, 2), nerve1.readOutputSignal());
		assertEquals(new Vector(4, 4), nerve2.readOutputSignal());
		assertEquals(new Vector(4, 4), nerve3.readOutputSignal());
		assertEquals(new Vector(8, 8), nerve4.readOutputSignal());
	}
}
