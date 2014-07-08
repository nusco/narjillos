package org.nusco.swimmers.application;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;
import org.nusco.swimmers.pond.Food;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class Cosmos extends Pond {

	public final static int SIZE = 10000;
	private static final int INITIAL_NUMBER_OF_FOOD_THINGS = 2;
	private static final int FOOD_RESPAWN_AVERAGE_INTERVAL = 1000;
	private static final int INITIAL_NUMBER_OF_SWIMMERS = 2;

	private int tickCounter = 0;

	public Cosmos() {
		super(SIZE);
		randomize();
	}

	private void updateTargets() {
		for (Thing thing : getThings())
			if (thing.getLabel().equals("swimmer")) {
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

	private void spawnFood() {
		add(new Food(), randomPosition());
	}

	private final void spawnSwimmer() {
		Swimmer swimmer = new Embryo(DNA.random()).develop();
		add(swimmer, randomPosition());
	}

	private Vector randomPosition() {
		double randomAngle = Math.random() * 360;
		double radius = SIZE / 2;
		double randomDistance = Math.random() * radius;
		return Vector.cartesian(radius, radius).plus(Vector.polar(randomAngle, randomDistance));
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
