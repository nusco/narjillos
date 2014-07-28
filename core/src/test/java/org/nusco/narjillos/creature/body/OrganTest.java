package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public abstract class OrganTest {
	
	protected BodyPart organ;

	@Before
	public void setUpPart() {
		organ = createConcreteOrgan(20, 10);
	}

	public abstract BodyPart createConcreteOrgan(int length, int thickness);

	@Test
	public void hasALength() {
		assertEquals(20, organ.getLength());
	}

	@Test
	public void hasAThickness() {
		assertEquals(10, organ.getThickness());
	}

	@Test
	public abstract void hasAnEndPoint();
	
	@Test
	public void hasAMassProportionalToItsArea() {
		assertEquals(200, organ.getMass(), 0.01);
	}
}
