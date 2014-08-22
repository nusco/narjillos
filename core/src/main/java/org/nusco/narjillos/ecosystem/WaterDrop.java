package org.nusco.narjillos.ecosystem;

import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.RanGen;

public class WaterDrop extends Ecosystem {

	private final static int SIZE = 20_000;
	private static final int INITIAL_NUMBER_OF_FOOD_PIECES = 200;
	private static final int MAX_NUMBER_OF_FOOD_PIECES = 600;
	private static final int FOOD_RESPAWN_AVERAGE_INTERVAL = 100;
	private static final int INITIAL_NUMBER_OF_NARJILLOS = 150;

	public WaterDrop() {
		this(null);
	}

	public WaterDrop(DNA dna) {
		super(SIZE);
		
		for (int i = 0; i < INITIAL_NUMBER_OF_FOOD_PIECES; i++)
			spawnFood(randomPosition());

		for (int i = 0; i < INITIAL_NUMBER_OF_NARJILLOS; i++) {
			if (dna == null)
				spawnNarjillo(randomPosition(), DNA.random());
			else
				spawnNarjillo(randomPosition(), dna);
		}
	}

	@Override
	public synchronized void tick() {
		super.tick();

		if (getNumberOfFoodPieces() < MAX_NUMBER_OF_FOOD_PIECES &&
				RanGen.nextDouble() < 1.0 / FOOD_RESPAWN_AVERAGE_INTERVAL)
			spawnFood(randomPosition());
	}

	protected Vector randomPosition() {
		double randomX = RanGen.nextDouble() * getSize();
		double randomY = RanGen.nextDouble() * getSize();
		return Vector.cartesian(randomX, randomY);
	}
}
