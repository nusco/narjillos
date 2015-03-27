package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public abstract class ConnectedOrganTest extends OrganTest {

	@Test
	public abstract void hasAParent();
	
	@Test
	public void hasAnEmptyListOfChildrenByDefault() {
		assertEquals(Collections.EMPTY_LIST, getOrgan().getChildren());
	}

	@Test
	public void knowsItsChildren() {
		ConnectedOrgan r = getOrgan();
		ConnectedOrgan child1 = r.addChild(new BodyPart(20, 10, 0, 0, 0, r, 0, 45, 0, 0));
		ConnectedOrgan r1 = getOrgan();
		ConnectedOrgan child2 = r1.addChild(new BodyPart(20, 10, 0, 0, 0, r1, 0, -45, 0, 0));

		List<ConnectedOrgan> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, getOrgan().getChildren());
	}

	protected ConnectedOrgan getOrgan() {
		return (ConnectedOrgan) organ;
	}	
}
