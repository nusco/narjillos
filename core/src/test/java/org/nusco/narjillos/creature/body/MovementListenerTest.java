package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class MovementListenerTest {
	@Test
	public void wholeBodySendsMoveEvents() {
		BodyPart organ = new Head(1, 0, new ColorByte(0), 1);
		BodyPart child1 = organ.sproutOrgan(2, 0, 0, new ColorByte(0), 0);
		child1.sproutOrgan(3, 0, 0, new ColorByte(0), 0);

		final int[] movement = new int[3];
		MovementListener listener = new MovementListener() {
			private int counter = 0;
			
			@Override
			public void moveEvent(Segment beforeMovement, Organ organ) {
				movement[counter++] = (int)organ.getLength();
			}
		};
		organ.setMovementListener(listener);
		
		organ.tick(Vector.ZERO);
		
		assertArrayEquals(new int[] {1, 2, 3}, movement);
	}
}
