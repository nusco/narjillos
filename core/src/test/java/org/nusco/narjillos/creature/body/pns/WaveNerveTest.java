package org.nusco.narjillos.creature.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;

public class WaveNerveTest {

	@Test
	public void generatesASinusWave() {
		final int length = 10;
		final double lengthAt45Degrees = 7.07;

		WaveNerve nerve = new WaveNerve(1.0 / 8);
		Vector signal = Vector.cartesian(0, length);

		assertApproxEqualsVector(Vector.ZERO, nerve.tick(signal));
		assertApproxEqualsVector(Vector.cartesian(-lengthAt45Degrees, 0), nerve.tick(signal));
		assertApproxEqualsVector(Vector.cartesian(-length, 0), nerve.tick(signal));
		assertApproxEqualsVector(Vector.cartesian(-lengthAt45Degrees, 0), nerve.tick(signal));

		// on the negative side, the wave is twice as fast
		assertApproxEqualsVector(Vector.cartesian(lengthAt45Degrees, 0), nerve.tick(signal));
		assertApproxEqualsVector(Vector.cartesian(lengthAt45Degrees, 0), nerve.tick(signal));
		assertApproxEqualsVector(Vector.ZERO, nerve.tick(signal));
	}

	private void assertApproxEqualsVector(Vector v1, Vector v2) {
		assertEquals(v1.toString(), v2.toString());
	}
}
