package org.nusco.swimmers.application;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class Cosmos extends Pond {

	public final static int SIZE = 4000;
	private static final int INITIAL_NUMBER_OF_FOOD_THINGS = 20;
	private static final int FOOD_RESPAWN_AVERAGE_INTERVAL = 1000;
	private static final int INITIAL_NUMBER_OF_SWIMMERS = 2;

	private int tickCounter = 0;

	public Cosmos() {
		super(SIZE);
		randomize();
	}

	private void updateTargets() {
		for (Thing thing : getThings("swimmer")) {
			Swimmer swimmer = (Swimmer) thing;
			Vector position = swimmer.getPosition();
			Vector locationOfClosestFood = find("food", position);
			swimmer.setCurrentTarget(locationOfClosestFood.minus(position));
		}
	}

	private void randomize() {
		for (int i = 0; i < INITIAL_NUMBER_OF_FOOD_THINGS; i++)
			spawnFood();

		for (int i = 0; i < INITIAL_NUMBER_OF_SWIMMERS; i++)
			spawnSwimmer();

		updateTargets();
	}

	@Override
	public void tick() {
		super.tick();

		if (Math.random() < 1.0 / FOOD_RESPAWN_AVERAGE_INTERVAL)
			spawnFood();

		if (tickCounter++ > 100) {
			tickCounter = 0;
			updateTargets();
		}
	}
}
