package org.nusco.narjillos.core.chemistry;

import static org.junit.Assert.assertEquals;
import static org.nusco.narjillos.core.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.core.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.core.chemistry.Element.OXYGEN;
import static org.nusco.narjillos.core.chemistry.Element.ZERO;

import org.junit.Test;

public class ElementTest {

	@Test
	public void convertsToAShortString() {
		assertEquals("O", OXYGEN.toString());
		assertEquals("H", HYDROGEN.toString());
		assertEquals("N", NITROGEN.toString());
		assertEquals("Z", ZERO.toString());
	}

	@Test
	public void convertsAnIntegerToANonZeroElement() {
		assertEquals(OXYGEN, Element.fromInteger(0));
		assertEquals(HYDROGEN, Element.fromInteger(1));
		assertEquals(NITROGEN, Element.fromInteger(2));
		assertEquals(OXYGEN, Element.fromInteger(3));
		assertEquals(HYDROGEN, Element.fromInteger(4));
		assertEquals(NITROGEN, Element.fromInteger(5));
		assertEquals(OXYGEN, Element.fromInteger(6));
	}
}
