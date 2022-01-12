package org.nusco.narjillos.application.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ViewModeTest {

	private final NarjillosApplicationState viewMode = new NarjillosApplicationState();

	@Test
	public void turningOffTheLightSwitchesToHighSpeed() {
		viewMode.toggleLight();

		assertThat(viewMode.getSpeed()).isEqualTo(Speed.FAST);
	}

	@Test
	public void turningOnTheLightSwitchesToRealtimeSpeed() {
		viewMode.toggleLight();
		assertThat(viewMode.getSpeed()).isEqualTo(Speed.FAST);

		viewMode.toggleLight();
		assertThat(viewMode.getSpeed()).isEqualTo(Speed.REALTIME);
	}

	@Test
	public void turningOnInfraredFromDarknessSwitchesToRealtimeSpeed() {
		viewMode.toggleLight();
		assertThat(viewMode.getSpeed()).isEqualTo(Speed.FAST);

		viewMode.toggleInfrared();
		assertThat(viewMode.getSpeed()).isEqualTo(Speed.REALTIME);
	}

	@Test
	public void turningOffInfraredSwitchesToRegularLight() {
		viewMode.toggleInfrared();
		assertThat(viewMode.getLight()).isEqualTo(Light.INFRARED);

		viewMode.toggleInfrared();
		assertThat(viewMode.getLight()).isEqualTo(Light.ON);
	}
}
