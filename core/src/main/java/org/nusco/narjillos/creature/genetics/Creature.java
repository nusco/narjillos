package org.nusco.narjillos.creature.genetics;

import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;

public interface Creature extends Thing {

	public DNA getDNA();
	public DNA reproduce(RanGen ranGen);
	public void feedOn(Thing thing);
	public boolean isDead();
}
