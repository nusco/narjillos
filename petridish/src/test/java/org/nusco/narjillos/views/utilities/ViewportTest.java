package org.nusco.narjillos.views.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;

public class ViewportTest {

	@Test
	public void hasTheSameSizeAsTheEcosystemByDefault() {
		Viewport viewport = new Viewport(new Ecosystem(100));

		assertMoreOrLessEquals(Vector.cartesian(100, 100), viewport.getSizeSC());
	}

	@Test
	public void hasAMaximumInitialSize() {
		Viewport viewport = new Viewport(new Ecosystem(100000));

		assertMoreOrLessEquals(Vector.cartesian(Viewport.MAX_INITIAL_SIZE_SC, Viewport.MAX_INITIAL_SIZE_SC), viewport.getSizeSC());
	}

	@Test
	public void canBeResized() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		viewport.setSizeSC(Vector.cartesian(1000, 900));
		
		assertMoreOrLessEquals(Vector.cartesian(1000, 900), viewport.getSizeSC());
	}

	@Test
	public void isCenteredOnTheCenterOfTheEcosystemByDefault() {
		Viewport viewport = new Viewport(new Ecosystem(100));

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void canBeCenteredOnADifferentPosition() {
		Viewport viewport = new Viewport(new Ecosystem((long) 100));
		stabilize(viewport);
		
		viewport.setCenterEC(Vector.cartesian(100, 200));
		stabilize(viewport);
		
		assertMoreOrLessEquals(Vector.cartesian(100, 200), viewport.getCenterEC());
	}

	@Test
	public void hasItsUpperCornerInTheOriginByDefault() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		stabilize(viewport);
		
		assertMoreOrLessEquals(Vector.ZERO, viewport.getPositionEC());
	}

	@Test
	public void canBeRecentered() {
		Viewport viewport = new Viewport(new Ecosystem(800));
		viewport.setSizeSC(Vector.cartesian(100, 400));
		stabilize(viewport);
		
		assertMoreOrLessEquals(Vector.cartesian(350, 199), viewport.getPositionEC());
		assertMoreOrLessEquals(Vector.cartesian(400, 400), viewport.getCenterEC());
		assertEquals(1, viewport.getZoomLevel(), 0.01);

		viewport.setCenterSC(Vector.cartesian(300, 500));
		stabilize(viewport);
		
		assertMoreOrLessEquals(Vector.cartesian(400, 400), viewport.getCenterEC());
		assertMoreOrLessEquals(Vector.cartesian(350, 200), viewport.getPositionEC());
	}

	@Test
	public void hasADefaultZoomLevelOfOne() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		stabilize(viewport);

		assertEquals(1, viewport.getZoomLevel(), 0.01);
	}

	@Test
	public void zoomsToViewTheEntireEcosystemAtTheBeginning() {
		final long ecosystemSize = (long)(Viewport.MAX_INITIAL_SIZE_SC * 10);
		Viewport viewport = new Viewport(new Ecosystem(ecosystemSize));
		stabilize(viewport);
		
		assertEquals(0.1, viewport.getZoomLevel(), 0.01);
	}

	@Test
	public void resizingItDoesNotChangeTheZoomLevel() {
		final long ecosystemSize = (long)(Viewport.MAX_INITIAL_SIZE_SC * 10);
		Viewport viewport = new Viewport(new Ecosystem(ecosystemSize));
		stabilize(viewport);
		
		viewport.setSizeSC(Vector.cartesian(100, 10000));
		assertEquals(0.1, viewport.getZoomLevel(), 0.01);
	}

	@Test
	public void zoomingItDoesNotChangeItsCenter() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.zoomIn();

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.zoomOut();

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void resizingItDoesNotChangeItsCenter() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.setSizeSC(Vector.cartesian(20, 1000));

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void resizingChangesItsPosition() {
		Viewport viewport = new Viewport(new Ecosystem(300));
		viewport.setSizeSC(Vector.cartesian(50, 60));
		viewport.setCenterEC(Vector.cartesian(100, 200));
		stabilize(viewport);
		assertMoreOrLessEquals(Vector.cartesian(75, 170), viewport.getPositionEC());
		
		viewport.setSizeSC(Vector.cartesian(40, 90));

		assertMoreOrLessEquals(Vector.cartesian(100, 200), viewport.getCenterEC());
		assertMoreOrLessEquals(Vector.cartesian(80, 155), viewport.getPositionEC());
	}

	@Test
	public void canZoomIn() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		stabilize(viewport);
		
		viewport.setSizeSC(Vector.cartesian(50, 50));
		viewport.setZoomLevel(0.5);

		viewport.zoomIn();
		
		assertEquals(0.515, viewport.getZoomLevel(), 0.01);

		viewport.zoomIn();
		
		assertEquals(0.5304, viewport.getZoomLevel(), 0.01);
	}

	@Test
	public void canZoomOut() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		stabilize(viewport);
		
		viewport.setZoomLevel(0.2);
		for (int i = 0; i < 500; i++)
			viewport.tick();
		
		assertEquals(0.2, viewport.getZoomLevel(), 0.01);

		viewport.zoomOut();
		stabilize(viewport);
		
		assertEquals(0.197, viewport.getZoomLevel(), 0.01);

		viewport.zoomOut();
		stabilize(viewport);
		
		assertEquals(0.191, viewport.getZoomLevel(), 0.01);
	}

	@Test
	public void pansTowardsCenterWhenAtMaxZoomLevel() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		stabilize(viewport);
		
		viewport.setCenterSC(Vector.cartesian(60, 60));
		viewport.setZoomLevel(1);
		for (int i = 0; i < 100; i++)
			viewport.tick();
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.zoomOut();
		for (int i = 0; i < 100; i++)
			viewport.tick();
		
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void zoomsOverTheMaxRegressToAStableState() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		viewport.setSizeSC(Vector.cartesian(50, 50));
		viewport.setZoomLevel(Viewport.MAX_ZOOM + 0.2);
		stabilize(viewport);
		
		assertTrue(viewport.getZoomLevel() < Viewport.MAX_ZOOM);
	}

	@Test
	public void cannotZoomOutOverALimit() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		for (int i = 0; i < 300; i++)
			viewport.zoomOut();
		
		assertEquals(0.4, viewport.getZoomLevel(), 0);
	}

	private void assertMoreOrLessEquals(Vector expected, Vector actual) {
		if (!moreOrLessEquals(expected, actual))
			throw new RuntimeException("Expected " + expected + ", got " + actual);
	}

	private boolean moreOrLessEquals(Vector expected, Vector actual) {
		final double delta = 1;
		return Math.abs(expected.x - actual.x) <= delta && Math.abs(expected.y - actual.y) <= delta;
	}

	private void stabilize(Viewport viewport) {
		for (int i = 0; i < 300; i++)
			viewport.tick();
	}
}
