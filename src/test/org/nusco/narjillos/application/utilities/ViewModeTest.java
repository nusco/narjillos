package org.nusco.narjillos.application.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ViewModeTest {

	private final NarjillosApplicationState viewMode = new NarjillosApplicationState();

	@Test
	public void turningOffTheLightSwitchesToHighSpeed() {
		viewMode.toggleLight();

		assertEquals(Speed.FAST, viewMode.getSpeed());
	}

	@Test
	public void turningOnTheLightSwitchesToRealtimeSpeed() {
		viewMode.toggleLight();
		assertEquals(Speed.FAST, viewMode.getSpeed());

		viewMode.toggleLight();

		assertEquals(Speed.REALTIME, viewMode.getSpeed());
	}

	@Test
	public void turningOnInfraredFromDarknessSwitchesToRealtimeSpeed() {
		viewMode.toggleLight();
		assertEquals(Speed.FAST, viewMode.getSpeed());

		viewMode.toggleInfrared();

		assertEquals(Speed.REALTIME, viewMode.getSpeed());
	}

	@Test
	public void turningOffInfraredSwitchesToRegularLight() {
		viewMode.toggleInfrared();
		assertEquals(Light.INFRARED, viewMode.getLight());

		viewMode.toggleInfrared();

		assertEquals(Light.ON, viewMode.getLight());
	}
}
