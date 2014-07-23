package org.nusco.swimmers.creature.physics;

import org.junit.Test;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;

public class EnergyTest {

	@Test
	public void depletesWithMovement() {
		Energy energy = new Energy(100);
		energy.moveEvent(new Segment(Vector.ZERO, Vector.cartesian(0,  100)), null);
	}
}
