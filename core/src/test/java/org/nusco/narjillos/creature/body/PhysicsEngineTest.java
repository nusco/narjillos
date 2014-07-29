package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class PhysicsEngineTest {

	PhysicsEngine engine = new PhysicsEngine();

	@Before
	public void setUpForces() {
		engine.addForce(new Segment(Vector.ZERO, Vector.cartesian(3, 4)));
		engine.addForce(new Segment(Vector.ZERO, Vector.cartesian(6, 0)));
		engine.addForce(new Segment(Vector.ZERO, Vector.cartesian(0, 7)));
	}
	
	@Test
	public void recordsMovements() {
		BodyPart organ = new Head(1, 0, new ColorByte(0), 1);
		BodyPart child1 = organ.sproutOrgan(2, 0, new ColorByte(0), 0, 0);
		child1.sproutOrgan(3, 0, new ColorByte(0), 0, 0);

		final int[] movement = new int[3];
		PhysicsEngine recorder = new PhysicsEngine() {
			private int counter = 0;
			
			@Override
			public void record(Segment beforeMovement, Organ organ) {
				movement[counter++] = (int)organ.getLength();
			}
		};
		
		organ.tick(0, 0, recorder);
		
		assertArrayEquals(new int[] {1, 2, 3}, movement);
	}
}
