package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public abstract class OrganTest {

	protected static int THICKNESS = 8;
	
	protected Organ organ;

	@Before
	public void setUpPart() {
		organ = createOrgan();
	}

	public abstract Organ createOrgan();

	@Test
	public void hasALength() {
		assertEquals(20, organ.getLength());
	}

	@Test
	public void hasAThickness() {
		assertEquals(THICKNESS, organ.getThickness());
	}

	@Test
	public abstract void hasAnEndPoint();

	@Test
	public abstract void hasAParent();

	@Test
	public void hasAColorValue() {
		assertEquals(100, organ.getColor());
	}
	
	@Test
	public void hasAnEmptyListOfChildPartsByDefault() {
		assertEquals(Collections.EMPTY_LIST, organ.getChildren());
	}

	@Test
	public void canSproutVisibleOrgans() {
		Organ child = organ.sproutOrgan(20, 12, 45, 100);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
	}
	
	@Test
	public void knowsItsChildren() {
		Organ child1 = organ.sproutOrgan(20, THICKNESS, 45, 100);
		Organ child2 = organ.sproutOrgan(20, THICKNESS, -45, 100);

		List<Organ> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, organ.getChildren());
	}
	
	@Test
	public void hasAMassEqualToItsArea() {
		double expectedMass = organ.getLength() * organ.getThickness();
		
		assertEquals(expectedMass, organ.getMass(), 0.01);
	}
}
