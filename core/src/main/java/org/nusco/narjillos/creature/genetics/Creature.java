package org.nusco.narjillos.creature.genetics;

import org.nusco.narjillos.shared.things.Thing;

public interface Creature extends Thing {

	public DNA getDNA();
	public DNA reproduce();
	public void feedOn(Thing thing);
	public boolean isDead();
}
