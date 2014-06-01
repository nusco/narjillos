package org.nusco.swimmer.body;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public abstract class VisibleOrganTest {

	protected static int THICKNESS = 8;
	
	protected Organ organ;

	@Before
	public void setUpPart() {
		organ = createVisibleOrgan();
	}

	public abstract Organ createVisibleOrgan();

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
	public void hasAnRGBValue() {
		assertEquals(100, organ.getRGB());
	}

	@Test
	public void returnsItselfAsAParent() {
		assertEquals(organ, organ.getAsParent());
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
		assertEquals(45, child.getRelativeAngle(), 0);
	}
	
	@Test
	public void knowsItsChildren() {
		Organ child1 = organ.sproutOrgan(20, THICKNESS, 45, 100);
		Organ child2 = organ.sproutOrgan(20, THICKNESS, 45, 100);

		List<Organ> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, organ.getChildren());
	}
}
