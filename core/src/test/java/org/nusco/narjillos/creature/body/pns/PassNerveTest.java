package org.nusco.narjillos.creature.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PassNerveTest {

	@Test
	public void passesASignalWithoutChangingIt() {
		PassNerve nerve = new PassNerve();

		assertEquals(42, nerve.tick(42), 0);
	}
}
