package org.nusco.swimmers.creature.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public abstract class NerveTest {

	@Test
	public void emitsAPredeterminedInitialValueByDefault() {
		Nerve nerve = createNerve();

		assertEquals(Vector.ZERO_ONE, nerve.getOutputSignal());
	}

	protected abstract Nerve createNerve();
}
