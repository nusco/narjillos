package org.nusco.swimmers.body;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public abstract class PartTest {

	protected static int THICKNESS = 8;
	
	protected Part part;

	@Before
	public void setUpPart() {
		part = createPart();
	}

	public abstract Part createPart();

	@Test
	public void hasALength() {
		assertEquals(20, part.getLength());
	}

	@Test
	public void hasAThickness() {
		assertEquals(THICKNESS, part.getThickness());
	}

	@Test
	public abstract void hasAnEndPoint();
	
	@Test
	public void hasAnEmptyListOfChildPartsByDefault() {
		assertEquals(Collections.EMPTY_LIST, part.getChildren());
	}
	
	@Test
	public void hasARelativeAngle() {
		part.setRelativeAngle(15);
		assertEquals(15, part.getRelativeAngle());
	}
	
	@Test
	public void theRelativeAngleIsNormalizedInDegrees() {
		part.setRelativeAngle(-10);
		assertEquals(350, part.getRelativeAngle());
		part.setRelativeAngle(370);
		assertEquals(10, part.getRelativeAngle());
	}

	@Test
	public void canSproutChildParts() {
		Part child = part.sproutChild(20, 12, 45);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
		assertEquals(45, child.getRelativeAngle());
	}

	@Test
	public void knowsItsChildren() {
		Part child1 = part.sproutChild(20, THICKNESS, 45);
		Part child2 = part.sproutChild(20, THICKNESS, 45);

		List<Part> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, part.getChildren());
	}
}
