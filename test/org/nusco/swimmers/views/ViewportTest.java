package org.nusco.swimmers.views;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.pond.Pond;

public class ViewportTest {

	@Test
	public void hasTheSameSizeAsThePondByDefault() {
		Viewport viewport = new Viewport(new Pond(100));

		assertEquals(100, viewport.getSizeX());
		assertEquals(100, viewport.getSizeY());
	}

	@Test
	public void hasAMaximumInitialSize() {
		Viewport viewport = new Viewport(new Pond(100000));

		assertEquals(Viewport.MAX_INITIAL_SIZE, viewport.getSizeX());
		assertEquals(Viewport.MAX_INITIAL_SIZE, viewport.getSizeY());
	}

	@Test
	public void canBeResized() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSize(1000, 900);
		
		assertEquals(1000, viewport.getSizeX());
		assertEquals(900, viewport.getSizeY());
	}

	@Test
	public void isPositionedAtTheCenterOfThePondByDefault() {
		Viewport viewport = new Viewport(new Pond(100));

		assertEquals(50, viewport.getCenterX());
		assertEquals(50, viewport.getCenterY());
	}

	@Test
	public void canBeCenteredOnADifferentPosition() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.centerOn(100, 200);
		
		assertEquals(100, viewport.getCenterX());
		assertEquals(200, viewport.getCenterY());
	}

	@Test
	public void hasAnUpperAngleInTheOriginByDefault() {
		Viewport viewport = new Viewport(new Pond(100));
		
		assertEquals(0, viewport.getUpperLeftCornerX());
		assertEquals(0, viewport.getUpperLeftCornerY());
	}

	@Test
	public void itsUpperAngleMovesWithItsCenter() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.centerOn(100, 200);
		viewport.setSize(40, 60);
		
		assertEquals(80, viewport.getUpperLeftCornerX());
		assertEquals(170, viewport.getUpperLeftCornerY());
	}

	@Test
	public void hasADefaultZoomLevelOfOne() {
		Viewport viewport = new Viewport(new Pond(100));

		assertEquals(1, viewport.getZoomLevel(), 0);
	}

	@Test
	public void zoomsToViewTheEntirePondAtTheBeginning() {
		final long pondSize = Viewport.MAX_INITIAL_SIZE * 10;
		Viewport viewport = new Viewport(new Pond(pondSize));

		assertEquals(0.1, viewport.getZoomLevel(), 0);
	}

	@Test
	public void resizingItDoesNotChangeTheZoomLevel() {
		final long pondSize = Viewport.MAX_INITIAL_SIZE * 10;
		Viewport viewport = new Viewport(new Pond(pondSize));

		viewport.setSize(100, 10000);
		assertEquals(0.1, viewport.getZoomLevel(), 0);
	}

	@Test
	public void zoomingItDoesNotChangeItsCenter() {
		Viewport viewport = new Viewport(new Pond(100));

		viewport.zoomIn();

		assertEquals(50, viewport.getCenterX());
		assertEquals(50, viewport.getCenterY());

		viewport.zoomOut();

		assertEquals(50, viewport.getCenterX());
		assertEquals(50, viewport.getCenterY());
	}

	@Test
	public void resizingItDoesNotChangeItsCenter() {
		Viewport viewport = new Viewport(new Pond(100));

		viewport.setSize(20, 1000);

		assertEquals(50, viewport.getCenterX());
		assertEquals(50, viewport.getCenterY());
	}

	@Test
	public void canZoomIn() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSize(50, 50);
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
		viewport.setSize(50, 50);
		viewport.zoomToFit();
		assertEquals(0.5, viewport.getZoomLevel(), 0);
		viewport.setSize(20, 20);

		viewport.zoomOut();
		
		assertEquals(0.4854, viewport.getZoomLevel(), 0.0001);

		viewport.zoomOut();
		
		assertEquals(0.4712, viewport.getZoomLevel(), 0.0001);
	}

	@Test
	public void canZoomInToFitEntirePond() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSize(50, 50);

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
		viewport.setSize(50, 50);
		for (int i = 0; i < 50; i++)
			viewport.zoomIn();

		for (int i = 0; i < 50; i++)
			viewport.tick();
		
		assertEquals(1, viewport.getZoomLevel(), 0.0001);
	}

	@Test
	public void cannotZoomOutToSeeMoreThanTheEntirePond() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.setSize(50, 50);
		for (int i = 0; i < 100; i++)
			viewport.zoomOut();
		
		assertEquals(0.5, viewport.getZoomLevel(), 0);
	}

	@Test
	public void itsVisibleAreaDependsOnScaleAndSize() {
		Viewport viewport = new Viewport(new Pond(100));
		viewport.zoomIn();
		viewport.setSize(100, 50);

		assertEquals(100 / Viewport.ZOOM_FACTOR, viewport.getVisibleAreaX(), 0);
		assertEquals(50 / Viewport.ZOOM_FACTOR, viewport.getVisibleAreaY(), 0);
	}
}
