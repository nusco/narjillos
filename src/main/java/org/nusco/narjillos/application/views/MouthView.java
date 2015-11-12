package org.nusco.narjillos.application.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.geometry.FastMath;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.geometry.ZeroVectorAngleException;
import org.nusco.narjillos.creature.Narjillo;

class MouthView implements ItemView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.1;
	private static final int NUMBER_OF_LINES = 3;
	private static final int LINE_LENGTH = 30;
	private static final int MAX_LINE_ANGLE = 25;

	private final Narjillo narjillo;
	private final Group group = new Group();
	private final Line[] lines = new Line[NUMBER_OF_LINES];
	
	public MouthView(Narjillo narjillo) {
		this.narjillo = narjillo;
		
		for (int i = 0; i < lines.length; i++) {
			lines[i] = createLine();
			group.getChildren().add(lines[i]);
		}
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;

		Color color = getColor(zoomLevel, infraredOn);
		for (int i = 0; i < lines.length; i++) {
			lines[i].setStroke(color);
			rotateLine(i);
		}
		
		Vector position = getNarjillo().getPosition();
		group.getTransforms().clear();
		group.getTransforms().add(new Translate(position.x, position.y));
		
		return group;
	}

	@Override
	public boolean isVisible(Viewport viewport) {
		return viewport.isVisible(getNarjillo().getPosition(), LINE_LENGTH);
	}
	
	private void rotateLine(int index) {
		double lineLag = (360 / (lines.length + 1) * index) % 360;
		double lineAngle = FastMath.sin(getNarjillo().getBrainWaveAngle() - lineLag) * MAX_LINE_ANGLE;
		lines[index].getTransforms().clear();
		lines[index].getTransforms().add(new Rotate(getMouthAngle() + lineAngle));
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
	
	private double getMouthAngle() {
		try {
			return getNarjillo().getMouth().getDirection().getAngle();
		} catch (ZeroVectorAngleException e) {
			return 0;
		}
	}

	private Line createLine() {
		Line result = new Line(0, 0, LINE_LENGTH, 2);
		result.setStrokeWidth(2);
		return result;
	}

	private Narjillo getNarjillo() {
		return narjillo;
	}
}
