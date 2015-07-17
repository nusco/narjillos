package org.nusco.narjillos.application.utilities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.experiment.environment.Ecosystem;

public class ViewportVisibilityTest {

	Viewport viewport;
	
	@Before
	public void setUpViewPort() {
		//
		// (0, 0)                                                    (200, 0)      
		//    ____________________________________________________________
		//    |                                                          |
		//    |                   ECOSYSTEM (200 x 200)                  |
		//    |                                                          |
		//    |                                                          |
		//    |                                                          |
		//    |                                                          |
		//    |         (50, 80)                         (150, 80)       |
		//    |              ______________________________              |
		//    |              |                            |              |
		//    |              |                            |              |
		//    |              |     VIEWPORT (100 x 40)    |              |
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

		viewport = new Viewport(new Ecosystem(200, false));
		viewport.zoomTo(1);
		stabilizeViewport();
		viewport.setSizeSC(Vector.cartesian(100, 40));
		assertTrue(viewport.getCenterEC().approximatelyEquals(Vector.cartesian(100, 100)));
	}

	@Test
	public void knowsWhetherAPointIsOutsideTheViewport() {
		assertFalse(viewport.isVisible(Vector.cartesian(49, 49), 0));
		assertFalse(viewport.isVisible(Vector.cartesian(151, 151), 0));
		assertFalse(viewport.isVisible(Vector.cartesian(100, 10), 0));
		assertFalse(viewport.isVisible(Vector.cartesian(10, 100), 0));
	}

	@Test
	public void knowsWhetherAPointIsInsideTheViewport() {
		assertTrue(viewport.isVisible(Vector.cartesian(51, 81), 0));
		assertTrue(viewport.isVisible(Vector.cartesian(149, 119), 0));
	}

	@Test
	public void takesTheLongestAxisDistanceAsAReferenceToDetermineVisibility() {
		assertTrue(viewport.isVisible(Vector.cartesian(51, 51), 0));
		assertTrue(viewport.isVisible(Vector.cartesian(149, 149), 0));
	}

	@Test
	public void hasATolerance() {
		assertFalse(viewport.isVisible(Vector.cartesian(49, 49), 0));
		assertTrue(viewport.isVisible(Vector.cartesian(49, 49), 3));
		
		assertFalse(viewport.isVisible(Vector.cartesian(1, 1), 0));
		assertTrue(viewport.isVisible(Vector.cartesian(1, 1), 100));
	}

	@Test
	public void approximateToTheLongesViewportSize() {
		assertTrue(viewport.isVisible(Vector.cartesian(100, 70), 0));
	}

	@Test
	public void takesZoomIntoAccountToDecideOnVisibility() {
		assertFalse(viewport.isVisible(Vector.cartesian(49, 79), 0));

		viewport.zoomOut();

		stabilizeViewport();
		
		assertTrue(viewport.isVisible(Vector.cartesian(49, 79), 0));

		assertFalse(viewport.isVisible(Vector.cartesian(1, 21), 0));

		assertFalse(viewport.isVisible(Vector.cartesian(1, 21), 0));
	}

	@Test
	public void takesPositionIntoAccountToDecideOnVisibility() {
		assertFalse(viewport.isVisible(Vector.cartesian(49, 79), 0));
		assertTrue(viewport.isVisible(Vector.cartesian(149, 119), 0));
		
		viewport.moveBy(Vector.cartesian(-10, -10));
		stabilizeViewport();

		assertTrue(viewport.isVisible(Vector.cartesian(49, 79), 0));
		assertFalse(viewport.isVisible(Vector.cartesian(150, 120), 0));
	}

	private void stabilizeViewport() {
		for (int i = 0; i < 300; i++)
			viewport.tick();
	}
}
