package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.configuration.Configuration;
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
	public synchronized double getValue() {
		return value;
	}

	@Override
	public synchronized double getMaximumValue() {
		return maxForAge;
	}

	@Override
	public synchronized boolean isZero() {
		return value <= 0;
	}

	@Override
	public synchronized void tick(double additionalEnergy) {
		if (maxForAge >= 0)
			maxForAge -= decay;

		increaseBy(additionalEnergy);
	}

	@Override
	public synchronized void increaseBy(double amount) {
		if (isZero())
			return; // once it's gone, it's gone

		value += amount;
		value = Math.max(0, Math.min(maxForAge, Math.max(0, value)));
	}

	@Override
	public synchronized void absorb(Energy other) {
		increaseBy(other.getValue());
		other.dropToZero();
	}

	@Override
	public synchronized void dropToZero() {
		value = 0;
	}

	@Override
	public synchronized void damage() {
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LifeFormEnergy that = (LifeFormEnergy) o;
		return Double.compare(that.initialValue, initialValue) == 0
			&& Double.compare(that.value, value) == 0
			&& Double.compare(that.maxForAge, maxForAge) == 0
			&& Double.compare(that.decay, decay) == 0;
	}

	@Override
	public String toString() {
		return NumberFormatter.format(getValue()) + " of " + NumberFormatter.format(getMaximumValue());
	}
}
