package org.nusco.narjillos.creature.body.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.nusco.narjillos.creature.body.physics.Viscosity.KICK_IN_VELOCITY;
import static org.nusco.narjillos.creature.body.physics.Viscosity.MAX_VELOCITY;

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
		for (int i = 0; i <= MAX_VELOCITY; i++) {
			double velocity = Viscosity.limit(KICK_IN_VELOCITY + i);
			assertTrue(velocity > previousVelocity);
			previousVelocity = velocity;
		}
	}

	@Test
	public void neverGetsPastAMaxVelocity() {
		assertEquals(MAX_VELOCITY, Viscosity.limit(Double.MAX_VALUE), 0.0);
	}
}
