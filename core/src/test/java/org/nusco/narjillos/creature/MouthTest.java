package org.nusco.narjillos.creature;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;

public class MouthTest {

	@Test
	public void pointsAtTarget() {
		Mouth compass = new Mouth();
		
		Vector position = Vector.cartesian(10, 10);
		Vector target = Vector.cartesian(20, 0);
		compass.tick(position, target, (double) 0);
		
		assertTrue(compass.getDirection().almostEquals(Vector.polar(-45, 1)));
	}

	@Test
	public void tracksTargetInstantly() {
		Mouth mouth = new Mouth();
		
		Vector position = Vector.cartesian(0, 0);
		mouth.tick(position, Vector.cartesian(10, -10), 0);
		mouth.tick(position, Vector.cartesian(10, 10), 0);
		
		assertTrue(mouth.getDirection().almostEquals(Vector.polar(45, 1)));
	}

	@Test
	public void locksAtAMaximumOf135RelativeDegrees() {
		Mouth mouth = new Mouth();
		
		Vector position = Vector.cartesian(0, 0);
		Vector target = Vector.cartesian(-10, 0);

		mouth.tick(position, target, 0);
		assertTrue(mouth.getDirection().almostEquals(Vector.polar(135, 1)));
	}

	@Test
	public void locksAtAMinimumOfMinus135RelativeDegrees() {
		Mouth mouth = new Mouth();
		
		Vector position = Vector.cartesian(0, 0);
		Vector target = Vector.cartesian(-10, -1);
		
		mouth.tick(position, target, 0);
		assertTrue(mouth.getDirection().almostEquals(Vector.polar(-135, 1)));
	}

	@Test
	public void keepsPointingInTheSameDirectionIfTheTargetIsNotVisible() {
		Mouth mouth = new Mouth();
		
		Vector position = Vector.cartesian(0, 0);
		mouth.tick(position, Vector.cartesian(-10, 1), 0);
		mouth.tick(position, Vector.cartesian(-10, -1), 0);
		
		assertTrue(mouth.getDirection().almostEquals(Vector.polar(135, 1)));
	}

	@Test
	public void keepsTheLockedDirectionUntilTheTargetIsVisibleAgain() {
		Mouth mouth = new Mouth();
		
		Vector position = Vector.cartesian(0, 0);
		Vector target = Vector.cartesian(-10, 0);

		mouth.tick(position, target, 0);
		assertTrue(mouth.getDirection().almostEquals(Vector.polar(135, 1)));

		mouth.tick(position, target, -20);
		assertTrue(mouth.getDirection().almostEquals(Vector.polar(135, 1)));

		mouth.tick(position, target, -100);
		assertTrue(mouth.getDirection().almostEquals(Vector.polar(180, 1)));
	}
}
