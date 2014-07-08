package org.nusco.swimmers.creature.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.swimmers.shared.physics.Vector;

public class ForceFieldTest {

	ForceField forceField = new ForceField(Vector.polar(45, 1));

	@Before
	public void setUpForces() {
		forceField.addForce(Vector.polar(45, 1));
		forceField.addForce(Vector.polar(45, 2));
		forceField.addForce(Vector.polar(135, 10));
	}

	@Test
	public void collectsTangentialForce() {
		assertTrue(forceField.getTangentialForce().almostEquals(Vector.polar(45,  3)));
	}

	@Test
	public void collectsTotalAmountOfForce() {
		assertEquals(13, forceField.getAmount(), 0);
	}
}
