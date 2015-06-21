package org.nusco.narjillos.ecosystem.chemistry;

import static org.nusco.narjillos.ecosystem.chemistry.Element.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ElementTest {
	
	@Test
	public void hasAByproduct() {
		assertEquals(HYDROGEN, OXYGEN.getByproduct());
		assertEquals(NITROGEN, HYDROGEN.getByproduct());
		assertEquals(OXYGEN, NITROGEN.getByproduct());
	}
}
