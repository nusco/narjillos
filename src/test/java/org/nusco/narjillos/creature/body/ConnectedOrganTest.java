package org.nusco.narjillos.creature.body;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public abstract class ConnectedOrganTest extends OrganTest {

	@Test
	public abstract void hasAParent();

	@Test
	public void hasAnEmptyListOfChildrenByDefault() {
		assertThat(getOrgan().getChildren()).isEmpty();
	}

	@Test
	public void knowsItsChildren() {
		ConnectedOrgan organ = getOrgan();
		ConnectedOrgan child1 = organ.addChild(new BodyPart(new BodyPartParameters(20, 10, organ, 45)));
		ConnectedOrgan child2 = organ.addChild(new BodyPart(new BodyPartParameters(20, 10, organ, -45)));

		assertThat(getOrgan().getChildren()).containsExactly(child1, child2);
	}

	@Test
	public void canBeALeaf() {
		assertThat(getOrgan().isLeaf()).isTrue();
	}

	@Test
	public void canBeANonLeaf() {
		ConnectedOrgan organ = getOrgan();
		organ.addChild(new BodyPart(new BodyPartParameters(20, 10, organ, 0)));

		assertThat(getOrgan().isLeaf()).isFalse();
	}

	ConnectedOrgan getOrgan() {
		return organ;
	}
}
