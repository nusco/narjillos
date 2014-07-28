package org.nusco.narjillos.pond;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Vector;

// TODO: I should really make everything in Pond and its subclasses
// more thread-safe. Right now many external interventions from
// another thread have the potential to break it.
public class PrivatePond extends Pond {

	private final static int SIZE = 3000;
	private final static Vector CENTER = Vector.cartesian(SIZE / 5, SIZE / 2);
	
	private final static ConcurrentLinkedQueue<DNA> narjillos = new ConcurrentLinkedQueue<DNA>();

	public PrivatePond(DNA dna) {
		super(SIZE);
		createManyNarjillos(dna);

		spawnFood(CENTER);
		spawnNextMutation();
	}

	// This must be done at the very beginning, because mutations imply
	// random number generation - and RanGen doesn't work from a separate
	// thread. A private pond must be called from multiple threads, so
	// we need to do all the random stuff during initialization.
	private void createManyNarjillos(DNA dna) {
		narjillos.add(dna);
		for (int i = 0; i < 1000; i++)
			narjillos.add(DNA.random());
	}

	private void spawnNextMutation() {
		spawnNarjillo(Vector.cartesian(SIZE / 3 * 2, SIZE / 2), narjillos.poll());
		updateTargets();
	}

	public synchronized void replaceFood(Vector newFoodLocation) {
		clearFood();
		spawnFood(newFoodLocation);
		updateTargets();
	}

	public synchronized void replaceNarjillo() {
		if (narjillos.isEmpty())
			return;
		clearCreatures();
		spawnNextMutation();
	}
}
