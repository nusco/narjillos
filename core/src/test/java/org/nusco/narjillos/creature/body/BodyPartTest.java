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
	public void hasALength() {
		assertEquals(20, bodyPart.getLength());
	}

	@Test
	public void hasAThickness() {
		assertEquals(10, bodyPart.getThickness());
	}

	@Test
	public abstract void hasAnEndPoint();
	
	@Test
	public void hasAMassProportionalToItsArea() {
		assertEquals(200, bodyPart.getMass(), 0.01);
	}
	
	@Test
	public void theMassIsAlwaysAtLeast1() {
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
