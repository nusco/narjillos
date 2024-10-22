package org.nusco.narjillos.application.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.application.utilities.Viewport;

/**
 * The speckles in the petri dish's liquid. They provide a sense of a tracked
 * narjillo's movement, speed and direction.
 */
class SpecklesView {

	private static final double NORMAL_SPECKLE_RADIUS = 2;
	private static final double INFRARED_SPECKLE_RADIUS = 2.5;
	private static final Color SPECKLE_COLOR = EnvironmentView.BACKGROUND_COLOR.darker();

	private final Viewport viewport;
	private final Shape background;
	private final Shape infraredBackground;
	private WritableImage backgroundTexture;
	private WritableImage infraredBackgroundTexture;

	public SpecklesView(Viewport viewport, long ecosystemSize) {
		this.viewport = viewport;

		createBackgroundTextures();

		background = new Rectangle(0, 0, ecosystemSize, ecosystemSize);
		background.setFill(new ImagePattern(backgroundTexture, 0, 0, viewport.getSizeSC().x, viewport.getSizeSC().y, false));

		infraredBackground = new Rectangle(0, 0, ecosystemSize, ecosystemSize);
		infraredBackground
				.setFill(new ImagePattern(infraredBackgroundTexture, 0, 0, viewport.getSizeSC().x, viewport.getSizeSC().y, false));
	}

	public Node toNode(boolean infraredOn) {
		Node result = infraredOn ? infraredBackground : background;

		double minimumZoomLevelToSeeSpeckles = 0.20;
		if (viewport.getZoomLevel() <= minimumZoomLevelToSeeSpeckles)
			return null;

		result.getTransforms().clear();
		result.getTransforms().add(new Translate(-viewport.getPositionEC().x, -viewport.getPositionEC().y));
		result.getTransforms().add(
				new Scale(viewport.getZoomLevel(), viewport.getZoomLevel(), viewport.getPositionEC().x, viewport.getPositionEC().y));

		return result;
	}

	private void createBackgroundTextures() {
		backgroundTexture = createBackgroundTextures(EnvironmentView.BACKGROUND_COLOR, NORMAL_SPECKLE_RADIUS);
		infraredBackgroundTexture = createBackgroundTextures(EnvironmentView.INFRARED_BACKGROUND_COLOR, INFRARED_SPECKLE_RADIUS);
	}

	private WritableImage createBackgroundTextures(Color backgroundColor, double speckleRadius) {
		final int tileSize = 600;

        Group backgroundGroup = new Group();
        Shape emptySpace = new Rectangle(0, 0, tileSize, tileSize);
		emptySpace.setFill(backgroundColor);
		backgroundGroup.getChildren().add(emptySpace);

		for (int i = 0; i < 5; i++) {
			int x = getRandomCoordinate(tileSize);
			int y = getRandomCoordinate(tileSize);
			backgroundGroup.getChildren().add(createSpeckle(x, y, speckleRadius));
		}

		Scene offScreenScene = new Scene(backgroundGroup, tileSize, tileSize);
		WritableImage texture = new WritableImage(tileSize, tileSize);
		offScreenScene.snapshot(texture);
		return texture;
	}

	private Shape createSpeckle(int x, int y, double radius) {
		Shape result = new Circle(radius);
		result.setFill(SPECKLE_COLOR);
		result.getTransforms().add(new Translate(x, y));
		return result;
	}

	private int getRandomCoordinate(int maxSize) {
		double maxSpeckleRadius = Math.max(NORMAL_SPECKLE_RADIUS, INFRARED_SPECKLE_RADIUS);
		// Don't get too close to the edges of the tile - we don't want half-speckles
		return (int) (maxSpeckleRadius + Math.random() * (maxSize - (maxSpeckleRadius * 2)));
	}
}
