package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.shared.utilities.ColorByte;

public abstract class OrganTest extends BodyPartTest {

	@Test
	public abstract void hasAParent();
	
	@Test
	public void hasAnEmptyListOfChildPartsByDefault() {
		assertEquals(Collections.EMPTY_LIST, getBodyPart().getChildren());
	}

	protected Organ getBodyPart() {
		return (Organ)bodyPart;
	}
	
	@Test
	public void knowsItsChildren() {
		Organ r = getBodyPart();
		Organ child1 = r.addChild(new BodySegment(20, 10, new ColorByte(100), r, 0, 45, 0, 0));
		Organ r1 = getBodyPart();
		Organ child2 = r1.addChild(new BodySegment(20, 10, new ColorByte(100), r1, 0, -45, 0, 0));

		List<Organ> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, getBodyPart().getChildren());
	}

	@Test
	public void sproutsOrgans() {
		Organ r = getBodyPart();
		BodyPart child = r.addChild(new BodySegment(20, 12, new ColorByte(100), r, 0, 45, 0, 0));
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
	}
}
