package org.nusco.narjillos.shared.things;

public class Energy {
	static final double MAX_ENERGY_TO_INITIAL_ENERGY = 5;

	private final double initialValue;
	private double value;
	private double maxForAge;
	private final double decay;

	public Energy(double initialValue, double lifespan) {
		this.initialValue = initialValue;
		this.value = this.initialValue;
		this.maxForAge = this.initialValue * Energy.MAX_ENERGY_TO_INITIAL_ENERGY;
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

	public void tick(double energySpent) {
		maxForAge -= decay;

		if (isDepleted())
			return;

		decreaseBy(energySpent);
	}

	public void consume(Thing thing) {
		increaseBy(thing.getEnergy().getValue());
	}

	private void increaseBy(double amount) {
		value += amount;
		value = Math.max(0, Math.min(maxForAge, Math.max(0, value)));
	}

	public double chunkOff(double percentFromZeroToOne) {
		double donation = getValue() * percentFromZeroToOne;
		if (getValue() - donation < getInitialValue())
			return 0; // Short on energy. Refuse donation.
		decreaseBy(donation);
		return donation;
	}

	private void decreaseBy(double amount) {
		increaseBy(-amount);
	}
}
