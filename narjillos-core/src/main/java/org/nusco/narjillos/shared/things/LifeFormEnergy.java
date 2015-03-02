package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.utilities.Configuration;

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
	public double getLevel() {
		if (isZero())
			return 0;
		return Math.min(1, value / initialValue);
	}

	@Override
	public void tick(double energySpent, double energyGained) {
		maxForAge -= decay;

		if (isZero())
			return;

		increaseBy(energyGained - energySpent);
	}

	@Override
	public void dropToZero() {
		value = 0;
	}

	@Override
	public void steal(Energy other) {
		increaseBy(other.getValue());
		other.dropToZero();
	}

	@Override
	public double donate(double percent) {
		double transferredEnergy = getValue() * percent;
		if (getValue() - transferredEnergy < initialValue)
			return 0; // Short on energy. Refuse to transfer.
		increaseBy(-transferredEnergy);
		return transferredEnergy;
	}

	private void increaseBy(double amount) {
		value += amount;
		value = Math.max(0, Math.min(maxForAge, Math.max(0, value)));
	}
}
