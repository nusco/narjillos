package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.core.chemistry.Element;

public class HeadParameters {
	private int adultLength;
	private int adultThickness;
	private int red;
	private int green;
	private int blue;
	private double metabolicRate;
	private double waveBeatRatio;
	private Element byproduct = Element.OXYGEN;
	private double energyToChildren;
	private int eggVelocity;
	private int eggInterval;

	public HeadParameters() {
	}

	public HeadParameters(int adultLength, int adultThickness) {
		this.adultLength = adultLength;
		this.adultThickness = adultThickness;
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

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public double getMetabolicRate() {
		return metabolicRate;
	}

	public void setMetabolicRate(double metabolicRate) {
		this.metabolicRate = metabolicRate;
	}

	public double getWaveBeatRatio() {
		return waveBeatRatio;
	}

	public void setWaveBeatRatio(double waveBeatRatio) {
		this.waveBeatRatio = waveBeatRatio;
	}

	public Element getByproduct() {
		return byproduct;
	}

	public void setByproduct(Element byproduct) {
		this.byproduct = byproduct;
	}

	public double getEnergyToChildren() {
		return energyToChildren;
	}

	public void setEnergyToChildren(double energyToChildren) {
		this.energyToChildren = energyToChildren;
	}

	public int getEggVelocity() {
		return eggVelocity;
	}

	public void setEggVelocity(int eggVelocity) {
		this.eggVelocity = eggVelocity;
	}

	public int getEggInterval() {
		return eggInterval;
	}

	public void setEggInterval(int eggInterval) {
		this.eggInterval = eggInterval;
	}
}