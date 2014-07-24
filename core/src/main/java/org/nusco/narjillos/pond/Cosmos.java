package org.nusco.narjillos.pond;

import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Cosmos extends Pond {

	public final static int SIZE = 20_000;
	private static final int INITIAL_NUMBER_OF_FOOD_THINGS = 100;
	private static final int FOOD_RESPAWN_AVERAGE_INTERVAL = 60;
	private static final int INITIAL_NUMBER_OF_SWIMMERS = 50;

	public Cosmos() {
		super(SIZE);
		randomize();
	}

	private void randomize() {
		for (int i = 0; i < INITIAL_NUMBER_OF_FOOD_THINGS; i++)
			spawnFood(randomPosition());

		for (int i = 0; i < INITIAL_NUMBER_OF_SWIMMERS; i++)
			spawnNarjilo(randomPosition(), DNA.random());
	}

	@Override
	public synchronized void tick() {
		super.tick();

		if (RanGen.nextDouble() < 1.0 / FOOD_RESPAWN_AVERAGE_INTERVAL)
			spawnFood(randomPosition());
	}

	protected Vector randomPosition() {
		double randomX = RanGen.nextDouble() * getSize();
		double randomY = RanGen.nextDouble() * getSize();
		return Vector.cartesian(randomX, randomY);
	}
}
