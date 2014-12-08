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
		bodyPart = createConcreteOrgan(20, 10);
	}

	public abstract BodyPart createConcreteOrgan(int length, int thickness);

	@Test
	public void hasALengthThatGrowsWithTime() {
		assertEquals(10, bodyPart.getLength(), 0);
		
		fullyGrowBodyPart();

		assertEquals(20, bodyPart.getLength(), 0);
	}

	@Test
	public void hasAThicknessThatGrowsWithTime() {
		assertEquals(1, bodyPart.getThickness(), 0);
		
		fullyGrowBodyPart();
		
		assertEquals(10, bodyPart.getThickness(), 0);
	}

	@Test
	public abstract void hasAnEndPoint();
	
	@Test
	public void hasAMassProportionalToItsArea() {
		fullyGrowBodyPart();
		
		assertEquals(200, bodyPart.getMass(), 0.01);
	}

	protected void fullyGrowBodyPart() {
		for (int i = 0; i < 1000; i++)
			bodyPart.grow();
		bodyPart.updateCaches();
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
}
