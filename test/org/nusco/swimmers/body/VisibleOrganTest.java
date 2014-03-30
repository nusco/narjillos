package org.nusco.swimmers.body;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public abstract class VisibleOrganTest {

	protected static int THICKNESS = 8;
	
	protected VisibleOrgan organ;

	@Before
	public void setUpPart() {
		organ = createVisibleOrgan();
	}

	public abstract VisibleOrgan createVisibleOrgan();

	@Test
	public void isVisible() {
		assertTrue(organ.isVisible());
	}

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
	public void hasARelativeAngle() {
		organ.setRelativeAngle(15);
		assertEquals(15, organ.getRelativeAngle(), 0);
	}
	
	@Test
	public void theRelativeAngleIsNormalizedInDegrees() {
		organ.setRelativeAngle(-10);
		assertEquals(350, organ.getRelativeAngle(), 0);
		organ.setRelativeAngle(370);
		assertEquals(10, organ.getRelativeAngle(), 0);
	}

	@Test
	public void canSproutVisibleOrgans() {
		VisibleOrgan child = organ.sproutVisibleOrgan(20, 12, 45, 100);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
		assertEquals(45, child.getRelativeAngle(), 0);
	}

	@Test
	public void canSproutInvisibleOrgans() {
		Organ child = organ.sproutInvisibleOrgan();
		assertFalse(child.isVisible());
	}
	
	@Test
	public void knowsItsChildren() {
		VisibleOrgan child1 = organ.sproutVisibleOrgan(20, THICKNESS, 45, 100);
		VisibleOrgan child2 = organ.sproutVisibleOrgan(20, THICKNESS, 45, 100);

		List<VisibleOrgan> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, organ.getChildren());
	}
}
