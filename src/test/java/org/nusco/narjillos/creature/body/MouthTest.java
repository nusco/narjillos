package org.nusco.narjillos.creature.body;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.geometry.ZeroVectorAngleException;

public class MouthTest {

	private final Mouth mouth = new Mouth();

	@Test
	public void pointsAtTarget() {
		var position = Vector.cartesian(10, 10);
		var target = Vector.cartesian(20, 0);
		tickManyTimes(position, target, 0);

		assertMouthPointsTowards(Vector.polar(-45, 1));
	}

	@Test
	public void tracksMovingTarget() {
		var position = Vector.cartesian(0, 0);
		tickManyTimes(position, Vector.cartesian(10, -10), 0);
		tickManyTimes(position, Vector.cartesian(10, 10), 0);

		assertMouthPointsTowards(Vector.polar(45, 1));
	}

	@Test
	public void locksAtAMaximumOf135RelativeDegrees() {
		var position = Vector.cartesian(0, 0);
		var target = Vector.cartesian(-10, 0);

		tickManyTimes(position, target, 0);
		assertMouthPointsTowards(Vector.polar(135, 1));
	}

	@Test
	public void locksAtAMinimumOfMinus135RelativeDegrees() {
		var position = Vector.cartesian(0, 0);
		var target = Vector.cartesian(-10, -1);

		tickManyTimes(position, target, 0);
		assertMouthPointsTowards(Vector.polar(-135, 1));
	}

	@Test
	public void keepsPointingInTheSameDirectionIfTheTargetIsNotVisible() {
		var position = Vector.cartesian(0, 0);
		tickManyTimes(position, Vector.cartesian(-10, 1), 0);
		tickManyTimes(position, Vector.cartesian(-10, -1), 0);

		assertMouthPointsTowards(Vector.polar(135, 1));
	}

	@Test
	public void keepsTheLockedDirectionUntilTheTargetIsVisibleAgain() {
		var position = Vector.cartesian(0, 0);
		var target = Vector.cartesian(-10, 0);

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
			assertThat(direction.getAngle()).isEqualTo(mouth.getDirection().getAngle(), within(1.0));
		} catch (ZeroVectorAngleException e) {
			throw new RuntimeException(e);
		}
	}
}
