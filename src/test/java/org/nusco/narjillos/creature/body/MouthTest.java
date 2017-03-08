package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.geometry.ZeroVectorAngleException;

public class MouthTest {

	private Mouth mouth = new Mouth();

	@Test
	public void pointsAtTarget() {
		Vector position = Vector.cartesian(10, 10);
		Vector target = Vector.cartesian(20, 0);
		tickManyTimes(position, target, 0);

		assertMouthPointsTowards(Vector.polar(-45, 1));
	}

	@Test
	public void tracksMovingTarget() {
		Vector position = Vector.cartesian(0, 0);
		tickManyTimes(position, Vector.cartesian(10, -10), 0);
		tickManyTimes(position, Vector.cartesian(10, 10), 0);

		assertMouthPointsTowards(Vector.polar(45, 1));
	}

	@Test
	public void locksAtAMaximumOf135RelativeDegrees() {
		Vector position = Vector.cartesian(0, 0);
		Vector target = Vector.cartesian(-10, 0);

		tickManyTimes(position, target, 0);
		assertMouthPointsTowards(Vector.polar(135, 1));
	}

	@Test
	public void locksAtAMinimumOfMinus135RelativeDegrees() {
		Vector position = Vector.cartesian(0, 0);
		Vector target = Vector.cartesian(-10, -1);

		tickManyTimes(position, target, 0);
		assertMouthPointsTowards(Vector.polar(-135, 1));
	}

	@Test
	public void keepsPointingInTheSameDirectionIfTheTargetIsNotVisible() {
		Vector position = Vector.cartesian(0, 0);
		tickManyTimes(position, Vector.cartesian(-10, 1), 0);
		tickManyTimes(position, Vector.cartesian(-10, -1), 0);

		assertMouthPointsTowards(Vector.polar(135, 1));
	}

	@Test
	public void keepsTheLockedDirectionUntilTheTargetIsVisibleAgain() {
		Vector position = Vector.cartesian(0, 0);
		Vector target = Vector.cartesian(-10, 0);

		tickManyTimes(position, target, 0);
		assertMouthPointsTowards(Vector.polar(135, 1));

		tickManyTimes(position, target, -20);
		assertMouthPointsTowards(Vector.polar(135, 1));

		tickManyTimes(position, target, -100);
		assertMouthPointsTowards(Vector.polar(180, 1));
	}

	private void tickManyTimes(Vector position, Vector target, double rotation) {
		for (int i = 0; i < 150; i++)
			mouth.tick(position, target, rotation);
	}

	private void assertMouthPointsTowards(Vector direction) {
		try {
			assertEquals(mouth.getDirection().getAngle(), direction.getAngle(), 1);
		} catch (ZeroVectorAngleException e) {
			throw new RuntimeException(e);
		}
	}
}
