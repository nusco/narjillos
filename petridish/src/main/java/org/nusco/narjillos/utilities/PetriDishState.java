package org.nusco.narjillos.utilities;

import org.nusco.narjillos.shared.things.Thing;

public class PetriDishState {
	private volatile Light light = Light.ON;
	private volatile Speed speed = Speed.REALTIME;
	private volatile Thing lockedOn = Thing.NULL;

	public Light getLight() {
		return light;
	}

	public Speed getSpeed() {
		return speed;
	}

	public Thing getLockedOn() {
		return lockedOn;
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

	public void shiftSpeed() {
		this.speed = speed.shift();
	}
	
	public void togglePause() {
		switch (speed) {
		case REALTIME:
			this.speed = Speed.PAUSED;
			break;
		case HIGH:
			this.speed = Speed.PAUSED;
			break;
		case SLOW:
			this.speed = Speed.PAUSED;
			break;
		case PAUSED:
			this.speed = Speed.REALTIME;
			break;
		default:
			break;
		}
	}

	public void lockOn(Thing narjillo) {
		lockedOn = narjillo;
	}

	public Thing unlock() {
		return lockedOn = Thing.NULL;
	}

	public boolean isLocked() {
		return lockedOn != Thing.NULL;
	}
}
