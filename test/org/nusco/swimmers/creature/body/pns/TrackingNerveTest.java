package org.nusco.swimmers.creature.body.pns;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.swimmers.physics.Vector;

public class TrackingNerveTest {

	TrackingNerve tracking = new TrackingNerve();

	@Test
	public void pointsAt180DegreesAtBeginning() {
		assertEquals(180, tracking.tick(Vector.polar(180, 1)).getAngle(), 0.001);
	}

	@Test
	public void outputsASignalWithTheSameLengthAsTheInput() {
		Vector targetDirection = Vector.polar(0, 99);
		Vector outputVector = tracking.tick(targetDirection);
		
		assertEquals(99, outputVector.getLength(), 0.001);
	}

	@Test
	public void turnsClockwiseTowardsTargetDirection() {
		Vector targetDirection = Vector.polar(90, 1);
		Vector outputVector = tracking.tick(targetDirection);
		
		assertEquals(180 - TrackingNerve.ROTATION_SPEED, outputVector.getAngle(), 0.0);
	}

	@Test
	public void turnsCounterClockwiseTowardsTargetDirection() {
		tracking.setAngle(90 - TrackingNerve.ROTATION_SPEED);
		
		Vector targetDirection = Vector.polar(100, 1);
		Vector outputVector = tracking.tick(targetDirection);
			
		assertEquals(90, outputVector.getAngle(), 0.001);
	}
	
	@Test
	public void turnsClockwiseTowardsTargetDirectionAroundZero() {
		tracking.setAngle(TrackingNerve.ROTATION_SPEED);
		
		Vector targetDirection = Vector.polar(-1, 1);
		Vector outputVector = tracking.tick(targetDirection);
		
		assertEquals(0, outputVector.getAngle(), 0.001);
	}

	@Test
	public void turnsCounterClockwiseTowardsTargetDirectionAroundZero() {
		tracking.setAngle(-TrackingNerve.ROTATION_SPEED);
		
		Vector targetDirection = Vector.polar(1, 1);
		Vector outputVector = tracking.tick(targetDirection);
		
		assertEquals(0, outputVector.getAngle(), 0.001);
	}

	@Test
	public void turnsClockwiseTowardsTargetDirectionAround180Degrees() {
		tracking.setAngle(180 + TrackingNerve.ROTATION_SPEED);
		
		Vector targetDirection = Vector.polar(90, 1);
		Vector outputVector = tracking.tick(targetDirection);
		
		assertEquals(180, outputVector.getAngle(), 0.001);
	}

	@Test
	public void turnsCounterClockwiseTowardsTargetDirectionAround180Degrees() {
		tracking.setAngle(180 - TrackingNerve.ROTATION_SPEED);
		
		Vector targetDirection = Vector.polar(200, 1);
		Vector outputVector = tracking.tick(targetDirection);
		
		assertEquals(180, outputVector.getAngle(), 0.001);
	}

	@Test
	public void doesNotTurnIfTheAngleDifferenceIsLessThanTheHisteresisValue() {
		double smallValue = TrackingNerve.ROTATION_HISTERESIS / 2;

		Vector targetDirection = Vector.polar(180 + smallValue, 99);
		Vector outputVector = tracking.tick(targetDirection);
		
		assertTrue(outputVector.almostEquals(Vector.polar(180, 99)));
	}

	@Test
	public void stopsTurningOnceAlignedWithTargetDirection() {
		Vector targetDirection = Vector.polar(90, 1);
		
		Vector outputVector = Vector.polar(180, 1);
		while(outputVector.getAngle() > (90 + TrackingNerve.ROTATION_HISTERESIS))
			outputVector = tracking.tick(targetDirection);

		Vector outputVector1 = tracking.tick(targetDirection);
		Vector outputVector2 = tracking.tick(targetDirection);
		assertTrue(outputVector1.almostEquals(outputVector2));
	}
}
