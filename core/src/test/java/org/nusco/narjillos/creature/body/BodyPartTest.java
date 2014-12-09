package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public abstract class BodyPartTest {
	
	protected BodyPart bodyPart;

	@Before
	public void setUpPart() {
		bodyPart = createConcreteOrgan(50, 20);
	}

	public abstract BodyPart createConcreteOrgan(int length, int thickness);

	@Test
	public void hasALengthThatGrowsWithTime() {
		assertEquals(5, bodyPart.getLength(), 0);
		
		fullyGrow(bodyPart);

		assertEquals(50, bodyPart.getLength(), 0);
	}

	@Test
	public void hasAThicknessThatGrowsWithTime() {
		assertEquals(1, bodyPart.getThickness(), 0);
		
		fullyGrow(bodyPart);
		
		assertEquals(20, bodyPart.getThickness(), 0);
	}

	@Test
	public abstract void hasAnEndPoint();
	
	@Test
	public void hasAMassProportionalToItsArea() {
		fullyGrow(bodyPart);
		
		assertEquals(1000, bodyPart.getMass(), 0.01);
	}
	
	@Test
	public void itsMassIsAlwaysAtLeast1() {
		BodyPart verySmallBodyPart = new BodyPart(0, 0, new ColorByte(0)) {

			@Override
			protected double calculateAbsoluteAngle() {
				return 0;
			}

			@Override
			protected Vector calculateStartPoint() {
				return null;
			}

			@Override
			protected Vector calculateCenterOfMass() {
				return null;
			}};

		assertEquals(1, verySmallBodyPart.getMass(), 0.0001);
	}

	protected void fullyGrow(BodyPart bodyPart) {
		while (!bodyPart.isFullyGrown())
			bodyPart.grow();
		bodyPart.updateCaches();
	}
}
