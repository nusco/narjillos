package org.nusco.narjillos.core.physics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.configuration.Configuration;

public class ViscosityTest {

	@Test
	public void doesNotKickInUntilACertainVelocityIsReached() {
		for (int velocity = 0; velocity <= Configuration.PHYSICS_VISCOSITY_KICKIN_VELOCITY; velocity++)
			assertThat(Viscosity.limit(velocity)).isEqualTo(velocity);
	}

	@Test
	public void limitsVelocityOverTheKickInValue() {
		double previousVelocity = 0;
		for (double velocity = Configuration.PHYSICS_VISCOSITY_KICKIN_VELOCITY; velocity <= Viscosity.getMaxVelocity(); velocity += 0.3) {
			assertThat(velocity > previousVelocity).isTrue();
			previousVelocity = velocity;
		}
	}

	@Test
	public void neverGetsOverAMaxVelocity() {
		assertThat(Viscosity.getMaxVelocity()).isEqualTo(Viscosity.limit(Double.MAX_VALUE));
	}
}
