package org.nusco.swimmers.creature.physics;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.swimmers.shared.physics.Vector;

public class PropulsionTest {

	Propulsion propulsion = new Propulsion(Vector.polar(45, 1));

	@Before
	public void setUpForces() {
		propulsion.addForce(Vector.polar(45, 1));
		propulsion.addForce(Vector.polar(45, 2));
		propulsion.addForce(Vector.polar(135, 10));
	}

	@Test
	public void collectsTangentialForce() {
		assertTrue(propulsion.getTangentialForce().almostEquals(Vector.polar(45,  3)));
	}
}
