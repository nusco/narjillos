package org.nusco.narjillos.application.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.experiment.environment.Ecosystem;

public class ViewportVisibilityTest {

	private Viewport viewport;

	@BeforeEach
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

		assertThat(viewport.getCenterEC().approximatelyEquals(Vector.cartesian(100, 100))).isTrue();
	}

	@Test
	public void knowsWhetherAPointIsOutsideTheViewport() {
		assertThat(viewport.isVisible(Vector.cartesian(49, 49), 0)).isFalse();
		assertThat(viewport.isVisible(Vector.cartesian(151, 151), 0)).isFalse();
		assertThat(viewport.isVisible(Vector.cartesian(100, 10), 0)).isFalse();
		assertThat(viewport.isVisible(Vector.cartesian(10, 100), 0)).isFalse();
	}

	@Test
	public void knowsWhetherAPointIsInsideTheViewport() {
		assertThat(viewport.isVisible(Vector.cartesian(51, 81), 0)).isTrue();
		assertThat(viewport.isVisible(Vector.cartesian(149, 119), 0)).isTrue();
	}

	@Test
	public void takesTheLongestAxisDistanceAsAReferenceToDetermineVisibility() {
		assertThat(viewport.isVisible(Vector.cartesian(51, 51), 0)).isTrue();
		assertThat(viewport.isVisible(Vector.cartesian(149, 149), 0)).isTrue();
	}

	@Test
	public void hasATolerance() {
		assertThat(viewport.isVisible(Vector.cartesian(49, 49), 0)).isFalse();
		assertThat(viewport.isVisible(Vector.cartesian(49, 49), 3)).isTrue();

		assertThat(viewport.isVisible(Vector.cartesian(1, 1), 0)).isFalse();
		assertThat(viewport.isVisible(Vector.cartesian(1, 1), 100)).isTrue();
	}

	@Test
	public void approximateToTheLongestViewportSize() {
		assertThat(viewport.isVisible(Vector.cartesian(100, 70), 0)).isTrue();
	}

	@Test
	public void takesZoomIntoAccountToDecideOnVisibility() {
		assertThat(viewport.isVisible(Vector.cartesian(49, 79), 0)).isFalse();
		assertThat(viewport.isVisible(Vector.cartesian(151, 121), 0)).isFalse();

		viewport.zoomTo(1.2);
		stabilizeViewport();

		assertThat(viewport.isVisible(Vector.cartesian(49, 79), 0)).isTrue();
		assertThat(viewport.isVisible(Vector.cartesian(151, 121), 0)).isTrue();
	}

	@Test
	public void takesPositionIntoAccountToDecideOnVisibility() {
		assertThat(viewport.isVisible(Vector.cartesian(49, 79), 0)).isFalse();
		assertThat(viewport.isVisible(Vector.cartesian(149, 119), 0)).isTrue();

		viewport.moveBy(Vector.cartesian(-10, -10));
		stabilizeViewport();

		assertThat(viewport.isVisible(Vector.cartesian(49, 79), 0)).isTrue();
		assertThat(viewport.isVisible(Vector.cartesian(150, 120), 0)).isFalse();
	}

	private void stabilizeViewport() {
		for (int i = 0; i < 40; i++)
			viewport.tick();
	}
}
