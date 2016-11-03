package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.NumberFormatter;

public class LifeFormEnergy implements Energy {

	private final double initialValue;

	private double value;

	private double maxForAge;

	private final double decay;

	// for deserialization only
	public LifeFormEnergy() {
		initialValue = decay = 0;
	}

	public LifeFormEnergy(double initialValue, double lifespan) {
		this.initialValue = initialValue;
		this.value = this.initialValue;
		this.maxForAge = this.initialValue * Configuration.CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY;
		this.decay = maxForAge / lifespan;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public double getMaximumValue() {
		return maxForAge;
	}

	@Override
	public boolean isZero() {
		return value <= 0;
	}

	@Override
	public void tick(double additionalEnergy) {
		if (maxForAge >= 0)
			maxForAge -= decay;

		increaseBy(additionalEnergy);
	}

	@Override
	public void increaseBy(double amount) {
		if (isZero())
			return; // once it's gone, it's gone

		value += amount;
		value = Math.max(0, Math.min(maxForAge, Math.max(0, value)));
	}

	@Override
	public void absorb(Energy other) {
		increaseBy(other.getValue());
		other.dropToZero();
	}

	@Override
	public void dropToZero() {
		value = 0;
	}

	@Override
	public void damage() {
		if (value < 10)
			increaseBy(-1);
		else
			value = value * 0.9;
	}

	@Override
	public int hashCode() {
		return (int) initialValue;
	}

	@Override
	public boolean equals(Object obj) {
		LifeFormEnergy other = (LifeFormEnergy) obj;
		return Double.doubleToLongBits(decay) == Double.doubleToLongBits(other.decay)
			&& Double.doubleToLongBits(initialValue) == Double.doubleToLongBits(other.initialValue)
			&& Double.doubleToLongBits(maxForAge) == Double.doubleToLongBits(other.maxForAge)
			&& Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
	}

	@Override
	public String toString() {
		return NumberFormatter.format(getValue()) + " of " + NumberFormatter.format(getMaximumValue());
	}
}
