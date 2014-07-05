package org.nusco.swimmers.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.views.Viewport;

public class ViewportVisibilityTest {

	Viewport viewport;
	
	@Before
	public void setUpViewPort() {
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
		//

		viewport = new Viewport(new Pond(200));
		viewport.setSize(100, 40);
		assertEquals(100, viewport.getCenterX(), 0.1);
		assertEquals(100, viewport.getCenterY(), 0.1);
	}

	@Test
	public void knowsWhetherAPointIsOutsideTheViewport() {
		assertFalse(viewport.isVisible(49, 49, 0));
		assertFalse(viewport.isVisible(151, 151, 0));
	}

	@Test
	public void knowsWhetherAPointIsInsideTheViewport() {
		assertTrue(viewport.isVisible(51, 81, 0));
		assertTrue(viewport.isVisible(149, 119, 0));
	}

	@Test
	public void approximateToTheLongesViewportSize() {
		assertTrue(viewport.isVisible(45, 100, 0));
	}
}
