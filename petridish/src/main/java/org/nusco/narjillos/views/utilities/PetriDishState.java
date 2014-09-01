package org.nusco.narjillos.views.utilities;

public class PetriDishState {
	private volatile Light light = Light.ON;
	private volatile Speed speed = Speed.REALTIME;

	public Light getLight() {
		return light;
	}

	public Speed getSpeed() {
		return speed;
	}

	public void toggleLight() {
		switch (light) {
		case INFRARED:
			this.light = Light.ON;
			break;
		case ON:
			this.light = Light.OFF;
			this.speed = Speed.HIGH;
			break;
		case OFF:
			this.light = Light.ON;
			this.speed = Speed.REALTIME;
			break;
		}
	}

	public void toggleInfrared() {
		switch (light) {
		case INFRARED:
			this.light = Light.ON;
			break;
		case ON:
			this.light = Light.INFRARED;
			break;
		case OFF:
			this.light = Light.INFRARED;
			this.speed = Speed.REALTIME;
			break;
		}
	}

	public void toggleSpeed() {
		switch (speed) {
		case REALTIME:
			this.speed = Speed.HIGH;
			break;
		case HIGH:
			this.speed = Speed.REALTIME;
			break;
		case PAUSED:
			this.speed = Speed.REALTIME;
			break;
		}
	}
	
	public void togglePause() {
		switch (speed) {
		case REALTIME:
			this.speed = Speed.PAUSED;
			break;
		case HIGH:
			this.speed = Speed.PAUSED;
			break;
		case PAUSED:
			this.speed = Speed.REALTIME;
			break;
		}
	}
}
