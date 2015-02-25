package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.utilities.Configuration;

// TODO: this needs some refactoring in the interface, method names, etc.
public class Energy {
	private final double initialValue;
	private double value;
	private double maxForAge;
	private final double decay;

	public Energy(double initialValue, double lifespan) {
		this.initialValue = initialValue;
		this.value = this.initialValue;
		this.maxForAge = this.initialValue * Configuration.CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY;
		this.decay = maxForAge / lifespan;
	}

	public double getValue() {
		return value;
	}

	public double getInitialValue() {
		return initialValue;
	}

	public double getMax() {
		return maxForAge;
	}

	public boolean isDepleted() {
		return value <= 0;
	}

	public double getPercentOfInitialValue() {
		if (value == 0)
			return 0;
		return Math.min(1, value / initialValue);
	}

	public void tick(double energySpent, double energyGained) {
		maxForAge -= decay;

		if (isDepleted())
			return;

		increaseBy(energyGained - energySpent);
	}

	public void consume(Thing thing) {
		increaseBy(thing.getEnergy().getValue());
		thing.getEnergy().deplete();
	}

	private void increaseBy(double amount) {
		value += amount;
		value = Math.max(0, Math.min(maxForAge, Math.max(0, value)));
	}

	public double transfer(double percentFromZeroToOne) {
		double transferredEnergy = getValue() * percentFromZeroToOne;
		if (getValue() - transferredEnergy < getInitialValue())
			return 0; // Short on energy. Refuse to transfer.
		increaseBy(-transferredEnergy);
		return transferredEnergy;
	}

	private void deplete() {
		value = 0;
	}
}
