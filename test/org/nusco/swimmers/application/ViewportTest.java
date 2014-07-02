package org.nusco.swimmers.application;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.shared.physics.Vector;

public class ViewportTest {

	Viewport viewport = new Viewport();

	@Test
	public void hasDefaultSizes() {
		assertEquals(Viewport.SIZE_X, viewport.getSizeX());
		assertEquals(Viewport.SIZE_Y, viewport.getSizeY());
	}

	@Test
	public void canBeResized() {
		viewport.setSize(1000, 900);
		
		assertEquals(1000, viewport.getSizeX());
		assertEquals(900, viewport.getSizeY());
	}

	@Test
	public void hasADefaultPosition() {
		assertEquals(Vector.ZERO, viewport.getPosition());
	}

	@Test
	public void canBeMoved() {
		Vector position = Vector.cartesian(100, 200);
		viewport.moveTo(position);
		
		assertEquals(position, viewport.getPosition());
	}
}
