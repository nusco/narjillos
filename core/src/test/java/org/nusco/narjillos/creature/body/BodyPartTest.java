package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

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
}
