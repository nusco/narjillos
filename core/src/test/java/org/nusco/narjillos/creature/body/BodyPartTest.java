package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public abstract class BodyPartTest {
	
	protected BodyPart part;

	@Before
	public void setUpPart() {
		part = createConcreteBodyPart(20, 10);
	}

	public abstract BodyPart createConcreteBodyPart(int length, int thickness);

	@Test
	public void hasALength() {
		assertEquals(20, part.getLength());
	}

	@Test
	public void hasAThickness() {
		assertEquals(10, part.getThickness());
	}

	@Test
	public abstract void hasAnEndPoint();
	
	@Test
	public void hasAMassProportionalToItsArea() {
		assertEquals(20, part.getMass(), 0.01);
	}
}
