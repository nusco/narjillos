package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class AtrophicOrganTest {

	protected AtrophicOrgan createConnectiveTissue() {
		return new AtrophicOrgan(new Head(1, 1, new ColorByte(0), 1));
	}

	@Test
	public void hasNoVisibleShape() {
		assertEquals(0, createConnectiveTissue().getLength(), 0.0);
		assertEquals(0, createConnectiveTissue().getThickness(), 0.0);
	}

	@Test
	public void hasNoMass() {
		assertEquals(0, createConnectiveTissue().getMass(), 0.0);
	}
}
