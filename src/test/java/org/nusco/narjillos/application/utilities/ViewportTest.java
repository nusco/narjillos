package org.nusco.narjillos.application.utilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.experiment.environment.Ecosystem;

public class ViewportTest {

	@Test
	public void hasTheSameSizeAsTheEcosystemByDefault() {
		var viewport = new Viewport(new Ecosystem(100, false));

		assertMoreOrLessEquals(Vector.cartesian(100, 100), viewport.getSizeSC());
	}

	@Test
	public void hasAMaximumInitialSize() {
		var viewport = new Viewport(new Ecosystem(100000, false));

		assertMoreOrLessEquals(Vector.cartesian(Viewport.MAX_INITIAL_SIZE_SC, Viewport.MAX_INITIAL_SIZE_SC), viewport.getSizeSC());
	}

	@Test
	public void canBeResized() {
		var viewport = new Viewport(new Ecosystem(100, false));
		viewport.setSizeSC(Vector.cartesian(1000, 900));

		assertMoreOrLessEquals(Vector.cartesian(1000, 900), viewport.getSizeSC());
	}

	@Test
	public void isCenteredOnTheCenterOfTheEcosystemByDefault() {
		var viewport = new Viewport(new Ecosystem(100, false));

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void canBeCenteredOnADifferentPosition() {
		var viewport = new Viewport(new Ecosystem(100, false));
		stabilize(viewport);

		viewport.setCenterEC(Vector.cartesian(100, 200));
		stabilize(viewport);

		assertMoreOrLessEquals(Vector.cartesian(100, 200), viewport.getCenterEC());
	}

	@Test
	public void hasItsUpperCornerInTheOriginByDefault() {
		var viewport = new Viewport(new Ecosystem(100, false));
		viewport.zoomTo(1);
		stabilize(viewport);

		assertMoreOrLessEquals(Vector.ZERO, viewport.getPositionEC());
	}

	@Test
	public void canBeRecentered() {
		var viewport = new Viewport(new Ecosystem(800, false));
		viewport.setSizeSC(Vector.cartesian(100, 400));
		viewport.zoomTo(1);
		stabilize(viewport);

		assertMoreOrLessEquals(Vector.cartesian(350, 199), viewport.getPositionEC());
		assertMoreOrLessEquals(Vector.cartesian(400, 400), viewport.getCenterEC());
		assertThat(viewport.getZoomLevel()).isEqualTo(1, within(0.01));

		viewport.setCenterSC(Vector.cartesian(300, 500));
		stabilize(viewport);

		assertMoreOrLessEquals(Vector.cartesian(400, 400), viewport.getCenterEC());
		assertMoreOrLessEquals(Vector.cartesian(350, 200), viewport.getPositionEC());
	}

	@Test
	public void zoomsFromALongDistanceAtTheBeginning() {
		final long ecosystemSize = (long) (Viewport.MAX_INITIAL_SIZE_SC * 10);
		var viewport = new Viewport(new Ecosystem(ecosystemSize, false));

		assertThat(viewport.getZoomLevel()).isEqualTo(viewport.minZoomLevel, within(0.01));
	}

	@Test
	public void zoomsToTheMinimumCloseupLevelAtTheBeginning() {
		var viewport = new Viewport(new Ecosystem(100, false));
		stabilize(viewport);

		assertThat(viewport.getZoomLevel()).isEqualTo(Viewport.ZOOM_CLOSEUP_LEVELS[0], within(0.01));
	}

	@Test
	public void resizingItDoesNotChangeTheZoomLevel() {
		final long ecosystemSize = (long) (Viewport.MAX_INITIAL_SIZE_SC * 10);
		var viewport = new Viewport(new Ecosystem(ecosystemSize, false));
		viewport.zoomTo(0.1);
		stabilize(viewport);

		viewport.setSizeSC(Vector.cartesian(100, 10000));
		assertThat(viewport.getZoomLevel()).isEqualTo(0.1, within(0.01));
	}

	@Test
	public void zoomingItDoesNotChangeItsCenter() {
		var viewport = new Viewport(new Ecosystem(100, false));
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.zoomIn();

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.zoomOut();

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void resizingItDoesNotChangeItsCenter() {
		var viewport = new Viewport(new Ecosystem(100, false));
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.setSizeSC(Vector.cartesian(20, 1000));

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void resizingChangesItsPosition() {
		var viewport = new Viewport(new Ecosystem(300, false));
		viewport.setSizeSC(Vector.cartesian(50, 60));
		viewport.setCenterEC(Vector.cartesian(100, 200));
		viewport.zoomTo(1.2);
		stabilize(viewport);

		assertMoreOrLessEquals(Vector.cartesian(58, 150), viewport.getPositionEC());

		viewport.setSizeSC(Vector.cartesian(40, 90));

		assertMoreOrLessEquals(Vector.cartesian(100, 200), viewport.getCenterEC());
		assertMoreOrLessEquals(Vector.cartesian(67, 125), viewport.getPositionEC());
	}

	@Test
	public void canZoomIn() {
		var viewport = new Viewport(new Ecosystem(100, false));
		stabilize(viewport);

		viewport.setSizeSC(Vector.cartesian(50, 50));
		viewport.zoomTo(1);

		viewport.zoomIn();

		assertThat(viewport.getZoomLevel()).isEqualTo(1.03, within(0.01));

		viewport.zoomIn();

		assertThat(viewport.getZoomLevel()).isEqualTo(1.061, within(0.01));
	}

	@Test
	public void canZoomOut() {
		var viewport = new Viewport(new Ecosystem(10000, false));
		stabilize(viewport);

		viewport.zoomTo(0.2);
		for (int i = 0; i < 500; i++)
			viewport.tick();

		assertThat(viewport.getZoomLevel()).isEqualTo(0.2, within(0.01));

		viewport.zoomOut();
		stabilize(viewport);

		assertThat(viewport.getZoomLevel()).isEqualTo(0.197, within(0.01));

		viewport.zoomOut();
		stabilize(viewport);

		assertThat(viewport.getZoomLevel()).isEqualTo(0.191, within(0.01));
	}

	@Test
	public void pansTowardsCenterWhenAtMaxZoomLevel() {
		var viewport = new Viewport(new Ecosystem(100, false));
		stabilize(viewport);

		viewport.setCenterSC(Vector.cartesian(60, 60));
		viewport.zoomTo(1);
		stabilize(viewport);

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.zoomOut();
		stabilize(viewport);

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void zoomsOverTheMaxRegressToAStableState() {
		var viewport = new Viewport(new Ecosystem(100, false));
		viewport.setSizeSC(Vector.cartesian(50, 50));
		viewport.zoomTo(Viewport.ZOOM_MAX + 0.2);
		stabilize(viewport);

		assertThat(viewport.getZoomLevel()).isLessThan(Viewport.ZOOM_MAX);
	}

	@Test
	public void cannotZoomOutOverALimit() {
		var viewport = new Viewport(new Ecosystem(100, false));
		for (int i = 0; i < 300; i++)
			viewport.zoomOut();

		assertThat(viewport.getZoomLevel()).isEqualTo(1.0, within(0.0));
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
