package org.nusco.narjillos.creature.physics;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;

public class ForceFieldTest {

	ForceField forceField = new ForceField();

	@Before
	public void setUpForces() {
		forceField.addForce(Vector.cartesian(3, 4));
		forceField.addForce(Vector.cartesian(6, 0));
		forceField.addForce(Vector.cartesian(0, 7));
	}

	@Test
	public void collectsTotalForce() {
		assertTrue(forceField.getTotalForce().almostEquals(Vector.cartesian(9, 11)));
	}
}
