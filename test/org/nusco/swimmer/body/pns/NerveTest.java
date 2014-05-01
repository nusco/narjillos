package org.nusco.swimmer.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.physics.Vector;

public abstract class NerveTest {

	@Test
	public void emitsAPredeterminedInitialValueByDefault() {
		Nerve nerve = createNerve();

		assertEquals(Vector.ZERO_ONE, nerve.readOutputSignal());
	}

	protected abstract Nerve createNerve();
}
