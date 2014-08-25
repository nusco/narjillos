package org.nusco.narjillos.shared.things;

public class Energy {
	static final double INITIAL_ENERGY_TO_MASS = 0.05;
	static final double MAX_ENERGY_TO_INITIAL_ENERGY = 2;

	private double value;
	private double maxForAge;

	private final double decay;
	private final double agonyLevel;

	public Energy(double mass, double maxLifespan) {
		value = mass * INITIAL_ENERGY_TO_MASS;
		maxForAge = value * Energy.MAX_ENERGY_TO_INITIAL_ENERGY;
		decay = maxForAge / maxLifespan;
		agonyLevel = decay * 300;
	}

	public double getValue() {
		return value;
	}

	public boolean isDepleted() {
		return value <= 0;
	}

	public double getMax() {
		return maxForAge;
	}

	public double getAgonyLevel() {
		return agonyLevel;
	}

	public void tick(double additionalEnergy) {
		maxForAge -= decay;

		if (isDepleted())
			return;

		increase(additionalEnergy);
	}

	public void consume(Thing thing) {
		increase(thing.getEnergy().getValue());
	}

	private void increase(double additionalEnergy) {
		value += additionalEnergy;
		value = Math.min(maxForAge, Math.max(0, value));
	}
}
