package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
		ConnectedOrgan organ = getOrgan();
		ConnectedOrgan child1 = organ.addChild(new BodyPart(new BodyPartParameters(20, 10, organ, 45)));
		ConnectedOrgan child2 = organ.addChild(new BodyPart(new BodyPartParameters(20, 10, organ, -45)));

		List<ConnectedOrgan> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);

		assertEquals(expected, getOrgan().getChildren());
	}

	@Test
	public void canBeALeaf() {
		assertTrue(getOrgan().isLeaf());
	}

	@Test
	public void canBeANonLeaf() {
		ConnectedOrgan organ = getOrgan();
		organ.addChild(new BodyPart(new BodyPartParameters(20, 10, organ, 0)));
		assertFalse(organ.isLeaf());
	}

	protected ConnectedOrgan getOrgan() {
		return organ;
	}
}
