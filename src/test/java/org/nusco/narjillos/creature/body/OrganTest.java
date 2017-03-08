package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;

public abstract class OrganTest {

	ConnectedOrgan organ;

	@Before
	public void setUpPart() {
		organ = createConcreteOrgan(50, 20);
	}

	protected abstract ConnectedOrgan createConcreteOrgan(int length, int thickness);

	@Test
	public void hasALengthThatGrowsWithTime() {
		assertEquals(5, organ.getLength(), 0);

		organ.growToAdultFormWithChildren();

		assertEquals(50, organ.getLength(), 0);
	}

	@Test
	public void hasAThicknessThatGrowsWithTime() {
		assertEquals(1, organ.getThickness(), 0);

		organ.growToAdultFormWithChildren();

		assertEquals(20, organ.getThickness(), 0);
	}

	@Test
	public abstract void hasAnEndPoint();

	@Test
	public void hasAMassProportionalToItsArea() {
		organ.growToAdultFormWithChildren();

		assertEquals(1000, organ.getMass(), 0.01);
	}

	@Test
	public void itsMassIsAlwaysAtLeast1() {
		Organ verySmallBodyPart = new Organ(0, 0, new Fiber(0, 0, 0)) {

			@Override
			protected double calculateAbsoluteAngle() {
				return 0;
			}

			@Override
			protected Vector calculateStartPoint() {
				return null;
			}
		};

		assertEquals(1, verySmallBodyPart.getMass(), 0.0001);
	}
}
