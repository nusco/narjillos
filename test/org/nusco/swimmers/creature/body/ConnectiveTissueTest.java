package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConnectiveTissueTest {

	protected ConnectiveTissue createConnectiveTissue() {
		return new ConnectiveTissue(null);
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
