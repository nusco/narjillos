package org.nusco.narjillos.creature.body.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.nusco.narjillos.creature.body.physics.Viscosity.KICK_IN_VELOCITY;

import org.junit.Test;

public class ViscosityTest {

	@Test
	public void doesntKickInUntilACertainVelocity() {
		for (int velocity = 0; velocity <= KICK_IN_VELOCITY; velocity++)
			assertEquals(velocity, Viscosity.limit(velocity), 0.0);
	}

	@Test
	public void limitsVelocityOverTheKickInValue() {
		double previousVelocity = 0;
		for (double velocity = KICK_IN_VELOCITY; velocity < Viscosity.getMaxVelocity(); velocity += 0.3) {
			assertTrue(velocity > previousVelocity);
			previousVelocity = velocity;
		}
	}

	@Test
	public void neverGetsOverAMaxVelocity() {
		assertEquals(Viscosity.limit(Double.MAX_VALUE), Viscosity.getMaxVelocity(), 0.0);
	}
}
