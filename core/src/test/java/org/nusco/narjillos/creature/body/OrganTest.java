package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public abstract class OrganTest {
	
	protected Organ organ;

	@Before
	public void setUpPart() {
		organ = createConcreteOrgan(20, 10);
	}

	public abstract Organ createConcreteOrgan(int length, int thickness);

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
		assertEquals(20, organ.getMass(), 0.01);
	}
}
