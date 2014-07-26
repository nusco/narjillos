package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.body.ForceField;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

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
	
	@Test
	public void recordsMovements() {
		BodyPart organ = new Head(1, 0, new ColorByte(0), 1);
		BodyPart child1 = organ.sproutOrgan(2, 0, new ColorByte(0), 0, 0);
		child1.sproutOrgan(3, 0, new ColorByte(0), 0, 0);

		final int[] movement = new int[3];
		ForceField recorder = new ForceField() {
			private int counter = 0;
			
			@Override
			public void record(Segment beforeMovement, Organ organ) {
				movement[counter++] = (int)organ.getLength();
			}
		};
		
		organ.tick(Vector.ZERO, recorder);
		
		assertArrayEquals(new int[] {1, 2, 3}, movement);
	}
}
