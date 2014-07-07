package org.nusco.swimmers.views;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;

public class ViewportTest {

	@Test
	public void hasTheSameSizeAsThePondByDefault() {
		Viewport viewport = new Viewport(new Pond(100));

		assertAlmostEquals(Vector.cartesian(100, 100), viewport.getSizeSC());
	}

	@Test
	public void hasAMaximumInitialSize() {
		Viewport viewport = new Viewport(new Pond(100000));

		assertAlmostEquals(Vector.cartesian(Viewport.MAX_INITIAL_SIZE_SC, Viewport.MAX_INITIAL_SIZE_SC), viewport.getSizeSC());
	}

	@Test
	public void canBeResized() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSizeSC(Vector.cartesian(1000, 900));
		
		assertAlmostEquals(Vector.cartesian(1000, 900), viewport.getSizeSC());
	}

	@Test
	public void isCenteredOnTheCenterOfThePondByDefault() {
		Viewport viewport = new Viewport(new Pond(100));

		assertAlmostEquals(Vector.cartesian(50, 50), viewport.getCenterPC());
	}

	@Test
	public void canBeCenteredOnADifferentPosition() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setCenterSC(Vector.cartesian(100, 200));
		
		assertAlmostEquals(Vector.cartesian(100, 200), viewport.getCenterPC());
	}

	@Test
	public void hasAnUpperAngleInTheOriginByDefault() {
		Viewport viewport = new Viewport(new Pond(100));
		
		assertAlmostEquals(Vector.ZERO, viewport.getPositionPC());
	}

	@Test
	public void canBeRecentered() {
		Viewport viewport = new Viewport(new Pond(800));
		viewport.setSizeSC(Vector.cartesian(100, 400));
		assertAlmostEquals(Vector.cartesian(350, 200), viewport.getPositionPC());
		assertAlmostEquals(Vector.cartesian(400, 400), viewport.getCenterPC());
		assertEquals(1, viewport.getZoomLevel(), 0);

		viewport.setCenterSC(Vector.cartesian(300, 500));

		assertAlmostEquals(Vector.cartesian(650, 700), viewport.getCenterPC());
		assertAlmostEquals(Vector.cartesian(600, 500), viewport.getPositionPC());
	}

	@Test
	public void hasADefaultZoomLevelOfOne() {
		Viewport viewport = new Viewport(new Pond(100));

		assertEquals(1, viewport.getZoomLevel(), 0);
	}

	@Test
	public void zoomsToViewTheEntirePondAtTheBeginning() {
		final long pondSize = Viewport.MAX_INITIAL_SIZE_SC * 10;
		Viewport viewport = new Viewport(new Pond(pondSize));

		assertEquals(0.1, viewport.getZoomLevel(), 0.001);
	}

	@Test
	public void resizingItDoesNotChangeTheZoomLevel() {
		final long pondSize = Viewport.MAX_INITIAL_SIZE_SC * 10;
		Viewport viewport = new Viewport(new Pond(pondSize));

		viewport.setSizeSC(Vector.cartesian(100, 10000));
		assertEquals(0.1, viewport.getZoomLevel(), 0.001);
	}

	@Test
	public void zoomingItDoesNotChangeItsCenter() {
		Viewport viewport = new Viewport(new Pond(100));
		assertAlmostEquals(Vector.cartesian(50, 50), viewport.getCenterPC());

		viewport.zoomIn();

		assertAlmostEquals(Vector.cartesian(50, 50), viewport.getCenterPC());

		viewport.zoomOut();

		assertAlmostEquals(Vector.cartesian(50, 50), viewport.getCenterPC());
	}

	@Test
	public void resizingItDoesNotChangeItsCenter() {
		Viewport viewport = new Viewport(new Pond(100));
		assertAlmostEquals(Vector.cartesian(50, 50), viewport.getCenterPC());

		viewport.setSizeSC(Vector.cartesian(20, 1000));

		assertAlmostEquals(Vector.cartesian(50, 50), viewport.getCenterPC());
	}

	@Test
	public void resizingChangesItsPosition() {
		Viewport viewport = new Viewport(new Pond(300));
		viewport.setSizeSC(Vector.cartesian(50, 60));
		viewport.setCenterPC(Vector.cartesian(100, 200));
		assertAlmostEquals(Vector.cartesian(75, 170), viewport.getPositionPC());
		
		viewport.setSizeSC(Vector.cartesian(40, 90));

		assertAlmostEquals(Vector.cartesian(100, 200), viewport.getCenterPC());
		assertAlmostEquals(Vector.cartesian(80, 155), viewport.getPositionPC());
	}

	@Test
	public void canZoomIn() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSizeSC(Vector.cartesian(50, 50));
		viewport.zoomToFit();
		assertEquals(0.5, viewport.getZoomLevel(), 0);

		viewport.zoomIn();
		
		assertEquals(0.515, viewport.getZoomLevel(), 0.0001);

		viewport.zoomIn();
		
		assertEquals(0.5304, viewport.getZoomLevel(), 0.0001);
	}

	@Test
	public void canZoomOut() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSizeSC(Vector.cartesian(50, 50));
		viewport.zoomToFit();
		assertEquals(0.5, viewport.getZoomLevel(), 0);
		viewport.setSizeSC(Vector.cartesian(20, 20));

		viewport.zoomOut();
		
		assertEquals(0.4854, viewport.getZoomLevel(), 0.0001);

		viewport.zoomOut();
		
		assertEquals(0.4712, viewport.getZoomLevel(), 0.0001);
	}

	@Test
	public void canZoomInToFitEntirePond() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSizeSC(Vector.cartesian(50, 50));

		viewport.zoomToFit();
		
		assertEquals(0.5, viewport.getZoomLevel(), 0);
	}
	
	@Test
	public void cannotZoomInOverAMaximum() {
		Viewport viewport = new Viewport(new Pond(100));
		for (int i = 0; i < 50; i++)
			viewport.zoomIn();
		
		assertEquals(Viewport.MAX_ZOOM, viewport.getZoomLevel(), 0);
	}

	@Test
	public void zoomsOverOneRegressToOne() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSizeSC(Vector.cartesian(50, 50));
		for (int i = 0; i < 50; i++)
			viewport.zoomIn();

		for (int i = 0; i < 60; i++)
			viewport.tick();
		
		assertEquals(1, viewport.getZoomLevel(), 0.01);
	}

	@Test
	public void cannotZoomOutToSeeMoreThanTheEntirePond() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSizeSC(Vector.cartesian(50, 50));
		for (int i = 0; i < 100; i++)
			viewport.zoomOut();
		
		assertEquals(0.5, viewport.getZoomLevel(), 0);
	}

	private void assertAlmostEquals(Vector expected, Vector actual) {
		if (!actual.almostEquals(expected))
			throw new RuntimeException("Expected " + expected + ", got " + actual);
	}
}
