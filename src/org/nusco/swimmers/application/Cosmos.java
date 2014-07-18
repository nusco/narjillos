package org.nusco.swimmers.application;

import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.utilities.RanGen;

public class Cosmos extends Pond {

	public final static int SIZE = 10_000;
	private static final int INITIAL_NUMBER_OF_FOOD_THINGS = 100;
	private static final int FOOD_RESPAWN_AVERAGE_INTERVAL = 100;
	private static final int INITIAL_NUMBER_OF_SWIMMERS = 50;

	public Cosmos() {
		super(SIZE);
		randomize();
	}

	private void randomize() {
		for (int i = 0; i < INITIAL_NUMBER_OF_FOOD_THINGS; i++)
			spawnFood(randomPosition());

		DNA randomGenes = DNA.random();
		for (int i = 0; i < INITIAL_NUMBER_OF_SWIMMERS; i++)
			spawnSwimmer(randomPosition(), randomGenes);
	}

	@Override
	public void tick() {
		if (RanGen.nextDouble() < 1.0 / FOOD_RESPAWN_AVERAGE_INTERVAL)
			spawnFood(randomPosition());

		super.tick();
	}

	protected Vector randomPosition() {
		double randomAngle = RanGen.nextDouble() * 360;
		double radius = getSize() / 2;
		double randomDistance = RanGen.nextDouble() * radius;
		return Vector.cartesian(radius, radius).plus(Vector.polar(randomAngle, randomDistance));
	}
}
