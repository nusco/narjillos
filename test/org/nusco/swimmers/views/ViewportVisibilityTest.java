package org.nusco.swimmers.views;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;

public class ViewportVisibilityTest {

	Viewport viewport;
	
	@Before
	public void setUpViewPort() {
		//
		// (0, 0)                                                    (200, 0)      
		//    ____________________________________________________________
		//    |                                                          |
		//    |                     POND (200 x 200)                     |
		//    |                                                          |
		//    |                                                          |
		//    |                                                          |
		//    |                                                          |
		//    |         (50, 80)                         (150, 80)       |
		//    |              ______________________________              |
		//    |              |                            |              |
		//    |              |                            |              |
		//    |              |     VIEWPORT (100 x 50)    |              |
		//    |              |                            |              |
		//    |              ______________________________              |
		//    |         (50, 120)                        (150, 120)      |
		//    |                                                          |
		//    |                                                          |
		//    |                                                          |
		//    |                                                          |
		//    |                                                          |
		//    |                                                          |
		//    ____________________________________________________________
		// (0, 200)                                                  (200, 200)      

		viewport = new Viewport(new Pond(200));
		viewport.setSize(Vector.cartesian(100, 40));
		assertTrue(viewport.getCenter().almostEquals(Vector.cartesian(100, 100)));
	}

	@Test
	public void knowsWhetherAPointIsOutsideTheViewport() {
		assertFalse(viewport.isVisible(Vector.cartesian(49, 49), 0));
		assertFalse(viewport.isVisible(Vector.cartesian(151, 151), 0));
		assertFalse(viewport.isVisible(Vector.cartesian(100, 10), 0));
		assertFalse(viewport.isVisible(Vector.cartesian(10, 100), 0));
	}

	@Test
	public void hasATolerance() {
		assertFalse(viewport.isVisible(Vector.cartesian(49, 49), 0));
		assertTrue(viewport.isVisible(Vector.cartesian(49, 49), 3));
		
		assertFalse(viewport.isVisible(Vector.cartesian(1, 1), 0));
		assertTrue(viewport.isVisible(Vector.cartesian(1, 1), 100));
	}

	@Test
	public void knowsWhetherAPointIsInsideTheViewport() {
		assertTrue(viewport.isVisible(Vector.cartesian(51, 81), 0));
		assertTrue(viewport.isVisible(Vector.cartesian(149, 119), 0));
	}

	@Test
	public void approximateToTheLongesViewportSize() {
		assertTrue(viewport.isVisible(Vector.cartesian(100, 70), 0));
	}

	@Test
	public void takesZoomIntoAccountToDecideOnVisibility() {
		assertFalse(viewport.isVisible(Vector.cartesian(49, 79), 0));
		
		viewport.zoomOut();

		assertTrue(viewport.isVisible(Vector.cartesian(49, 79), 0));

		assertFalse(viewport.isVisible(Vector.cartesian(1, 21), 0));
		
		for (int i = 0; i < 50; i++)
			viewport.zoomIn();

		assertFalse(viewport.isVisible(Vector.cartesian(1, 21), 0));
	}
}
