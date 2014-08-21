package org.nusco.narjillos.creature;

public class Energy {
	static final double MASS_TO_ENERGY_RATIO = 0.1;
	static final double ENERGY_PER_FOOD_ITEM_RATIO = 1;
	static final double MAX_ENERGY_RATIO = 2;

	private double value;
	private double maxForAge;
	
	private final double decay;
	private final double energyPerFoodItem;
	private final double agonyLevel;

	public Energy(double mass, double maxLifespan) {
		value = mass * MASS_TO_ENERGY_RATIO;
		maxForAge = value * Energy.MAX_ENERGY_RATIO;
		decay = maxForAge / maxLifespan;
		energyPerFoodItem = maxForAge * Energy.ENERGY_PER_FOOD_ITEM_RATIO;
		agonyLevel = decay * 300; // TODO: tweak
	}
	
	public double getValue() {
		return value;
	}

	public boolean isDead() {
		return value <= 0;
	}

	double getMax() {
		return maxForAge;
	}

	public boolean isInAgony() {
		return value <= agonyLevel;
	}

	public double getMaxEnergy() {
		return maxForAge;
	}

	double getAgonyLevel() {
		return agonyLevel;
	}

	void tick(double additionalEnergy) {
		maxForAge -= decay;

		if (isDead())
			return;
		
		increase(additionalEnergy);
	}

	void increaseByFeeding() {
		increase(energyPerFoodItem);
	}

	private void increase(double additionalEnergy) {
		value += additionalEnergy;
		if (value > maxForAge)
			value = maxForAge;
		if (value < 0)
			value = 0;
	}
}
