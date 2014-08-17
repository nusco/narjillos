package org.nusco.narjillos.creature.body.physics;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.physics.ForceField;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class ForceFieldTest {

	ForceField forceField = new ForceField();

	@Before
	public void setUpForces() {
		forceField.addForce(new Segment(Vector.ZERO, Vector.cartesian(3, 4)));
		forceField.addForce(new Segment(Vector.ZERO, Vector.cartesian(6, 0)));
		forceField.addForce(new Segment(Vector.ZERO, Vector.cartesian(0, 7)));
	}
	
	@Test
	public void recordsMovements() {
		Organ organ = new Head(1, 0, new ColorByte(0), 1);
		Organ child1 = organ.sproutOrgan(2, 0, new ColorByte(0), 0, 0, 0);
		child1.sproutOrgan(3, 0, new ColorByte(0), 0, 0, 0);

		final int[] movement = new int[3];
		ForceField recorder = new ForceField() {
			private int counter = 0;
			
			@Override
			public void record(Segment beforeMovement, BodyPart organ) {
				movement[counter++] = (int)organ.getLength();
			}
		};
		
		organ.recordForce(recorder);
		
		assertArrayEquals(new int[] {1, 2, 3}, movement);
	}
}
