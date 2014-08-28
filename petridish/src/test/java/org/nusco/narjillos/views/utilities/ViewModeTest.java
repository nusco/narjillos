package org.nusco.narjillos.views.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ViewModeTest {

	ViewMode viewMode = new ViewMode();
	
	@Test
	public void turningOffTheLightSwitchesToHighSpeed() {
		viewMode.toggleLight();

		assertEquals(Speed.HIGH, viewMode.getSpeed());
	}
	
	@Test
	public void turningOnTheLightSwitchesToRealtimeSpeed() {
		viewMode.toggleLight();
		assertEquals(Speed.HIGH, viewMode.getSpeed());

		viewMode.toggleSpeed();
		
		assertEquals(Speed.REALTIME, viewMode.getSpeed());
	}
	
	@Test
	public void turningOnInfraredFromDarknessSwitchesToRealtimeSpeed() {
		viewMode.toggleLight();
		assertEquals(Speed.HIGH, viewMode.getSpeed());

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
