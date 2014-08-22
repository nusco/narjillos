package org.nusco.narjillos.shared.things;

public class Energy {
	static final double MASS_TO_ENERGY_RATIO = 0.1;
	static final double ENERGY_PER_FOOD_ITEM_RATIO = 1;
	static final double MAX_ENERGY_RATIO = 2;

	private double value;
	private double maxForAge;
	
	private final double decay;
	private final double agonyLevel;

	public Energy(double mass, double maxLifespan) {
		value = mass * MASS_TO_ENERGY_RATIO;
		maxForAge = value * Energy.MAX_ENERGY_RATIO;
		decay = maxForAge / maxLifespan;
		agonyLevel = decay * 300; // TODO: tweak
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
