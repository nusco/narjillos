package org.nusco.narjillos.creature.physics;

import org.junit.Test;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;

public class EnergyTest {

	@Test
	public void depletesWithMovement() {
		Energy energy = new Energy(100);
		energy.record(new Segment(Vector.ZERO, Vector.cartesian(0,  100)), null);
	}
}
