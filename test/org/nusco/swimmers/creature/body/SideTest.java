package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SideTest {

	@Test
	public void hasAMatchingSign() {
		assertEquals(1, Side.RIGHT.toSign());
		assertEquals(-1, Side.LEFT.toSign());
	}
}
