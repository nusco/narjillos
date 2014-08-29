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
		if (light == Light.INFRARED)
			this.light = Light.ON;
		else if (light == Light.ON) {
			this.light = Light.OFF;
			this.speed = Speed.HIGH;
		}
		else {
			this.light = Light.ON;
			this.speed = Speed.REALTIME;
		}
	}

	public void toggleInfrared() {
		if (light == Light.OFF) {
			this.light = Light.INFRARED;
			this.speed = Speed.REALTIME;
		}
		else if (light == Light.INFRARED)
			this.light = Light.ON;
		else
			this.light = Light.INFRARED;
	}

	public void toggleSpeed() {
		if (speed == Speed.REALTIME)
			this.speed = Speed.HIGH;
		else
			this.speed = Speed.REALTIME;
	}
}
