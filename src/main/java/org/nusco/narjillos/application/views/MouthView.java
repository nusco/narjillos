package org.nusco.narjillos.application.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.physics.ZeroVectorException;
import org.nusco.narjillos.creature.Narjillo;

class MouthView implements ItemView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.1;

	private static final int LENGTH = 50;
	private final Narjillo narjillo;
	private final Group group = new Group();
	private final Line line1 = createLine();
	private final Line line2 = createLine();

	public MouthView(Narjillo narjillo) {
		this.narjillo = narjillo;
		group.getChildren().add(line1);
		group.getChildren().add(line2);
	}

	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;
		
		Color color = getColor(zoomLevel, infraredOn);
		line1.setStroke(color);
		line2.setStroke(color);

		rotate(line1, 10);
		rotate(line2, -10);
		
		Vector position = getNarjillo().getPosition();
		group.getTransforms().clear();
		group.getTransforms().add(new Translate(position.x, position.y));
		
		return group;
	}

	@Override
	public boolean isVisible(Viewport viewport) {
		return viewport.isVisible(getNarjillo().getPosition(), LENGTH);
	}

	private Color getColor(double zoomLevel, boolean infraredOn) {
		if (infraredOn)
			return Color.WHITE;
		return new Color(0, 0.6, 0, getOpacity(zoomLevel));
	}

	private double getOpacity(double zoomLevel) {
		double opacityBasedOnZoom = (zoomLevel - MINIMUM_ZOOM_LEVEL) * 20;
		double opacity = Math.min(opacityBasedOnZoom, getOpacityBasedOnAge());
		return Math.max(0, Math.min(1, opacity));
	}

	private double getOpacityBasedOnAge() {
		final double AGE_OF_FULL_OPACITY = 100;

		long age = getNarjillo().getAge();
		if (age > AGE_OF_FULL_OPACITY)
			return 1;
		
		return age / AGE_OF_FULL_OPACITY;
	}
	
	private void rotate(Line line, int angle) {
		line.getTransforms().clear();
		line.getTransforms().add(new Rotate(getMouthAngle() + angle));
	}

	private double getMouthAngle() {
		try {
			return getNarjillo().getMouth().getDirection().getAngle();
		} catch (ZeroVectorException e) {
			return 0;
		}
	}

	private Line createLine() {
		Line result = new Line(0, 0, LENGTH, 2);
		result.setStrokeWidth(2);
		return result;
	}

	private Narjillo getNarjillo() {
		return narjillo;
	}
}
