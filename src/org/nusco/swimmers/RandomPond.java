package org.nusco.swimmers;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;
import org.nusco.swimmers.pond.Food;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class RandomPond extends Pond {

	private static final int NUMBER_OF_FOOD_THINGS = 30;
	private static final int NUMBER_OF_SWIMMERS = 100;

	private int tickCounter = 0;

	public RandomPond() {
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

	public final void randomize() {
		for (int i = 0; i < NUMBER_OF_FOOD_THINGS; i++)
			add(new Food(), randomCoordinate(), randomCoordinate());

		for (int i = 0; i < NUMBER_OF_SWIMMERS; i++)
			addRandomSwimmer();

		updateTargets();
	}

	private final void addRandomSwimmer() {
		Swimmer swimmer = new Embryo(DNA.random()).develop();
		add(swimmer, randomCoordinate(), randomCoordinate());
	}

	private final long randomCoordinate() {
		return (long) (Math.random() * Pond.USEFUL_AREA_SIZE);
	}

	@Override
	public void tick() {
		super.tick();

		if(tickCounter++ > 100) {
			tickCounter = 0;
			updateTargets();
		}
	}
}
