package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public abstract class OrganTest extends BodyPartTest {

	@Test
	public abstract void hasAParent();
	
	@Test
	public void hasAnEmptyListOfChildPartsByDefault() {
		assertEquals(Collections.EMPTY_LIST, part.getChildren());
	}
	
	@Test
	public void knowsItsChildren() {
		BodyPart child1 = part.sproutOrgan(20, 10, 45, 100, 0);
		BodyPart child2 = part.sproutOrgan(20, 10, -45, 100, 0);

		List<BodyPart> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, part.getChildren());
	}

	@Test
	public void sproutsVisibleOrgans() {
		Organ child = part.sproutOrgan(20, 12, 45, 100, 0);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
	}
}
