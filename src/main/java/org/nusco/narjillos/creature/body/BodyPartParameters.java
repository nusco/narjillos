package org.nusco.narjillos.creature.body;

public class BodyPartParameters {
	private int adultLength;
	private int adultThickness;
	private ConnectedOrgan parent;
	private int delay;
	private int amplitude;
	private int angleToParentAtRest;
	private int skewing;
	private int redShift;
	private int greenShift;
	private int blueShift;
	
	public BodyPartParameters(int adultLength, int adultThickness, ConnectedOrgan parent, int angleToParentAtRest) {
		this.adultLength = adultLength;
		this.adultThickness = adultThickness;
		this.parent = parent;
		this.angleToParentAtRest = angleToParentAtRest;
	}

	public int getAdultLength() {
		return adultLength;
	}

	public void setAdultLength(int adultLength) {
		this.adultLength = adultLength;
	}

	public int getAdultThickness() {
		return adultThickness;
	}

	public void setAdultThickness(int adultThickness) {
		this.adultThickness = adultThickness;
	}

	public ConnectedOrgan getParent() {
		return parent;
	}

	public void setParent(ConnectedOrgan parent) {
		this.parent = parent;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(int amplitude) {
		this.amplitude = amplitude;
	}

	public int getAngleToParentAtRest() {
		return angleToParentAtRest;
	}

	public void setAngleToParentAtRest(int angleToParentAtRest) {
		this.angleToParentAtRest = angleToParentAtRest;
	}

	public int getSkewing() {
		return skewing;
	}

	public void setSkewing(int skewing) {
		this.skewing = skewing;
	}

	public int getRedShift() {
		return redShift;
	}

	public void setRedShift(int redShift) {
		this.redShift = redShift;
	}

	public int getGreenShift() {
		return greenShift;
	}

	public void setGreenShift(int greenShift) {
		this.greenShift = greenShift;
	}

	public int getBlueShift() {
		return blueShift;
	}

	public void setBlueShift(int blueShift) {
		this.blueShift = blueShift;
	}
}