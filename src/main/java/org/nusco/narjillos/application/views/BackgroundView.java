package org.nusco.narjillos.application.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.application.utilities.Light;
import org.nusco.narjillos.application.utilities.Viewport;

/**
 * The background. It can be different colors depending on light conditions and
 * zoom level.
 * <p>
 * This background also puts speckles in the petri dish's liquid, to give a
 * sense of the current speed and direction, especially when tracking things.
 */
class BackgroundView {

	private static final double NORMAL_SPECKLE_RADIUS = 2;

	private static final double INFRARED_SPECKLE_RADIUS = 2.5;

	private static final Color BACKGROUND_COLOR = Color.ANTIQUEWHITE;

	private static final Color INFRARED_BACKGROUND_COLOR = Color.DARKGRAY.darker();

	private static final int TILE_SIZE = 800;

	private static final double MINIMUM_ZOOM_LEVEL_TO_SEE_SPECS = 0.20;

	private static final Color SPECKLE_COLOR = Color.ANTIQUEWHITE.darker();

	private static final int SPECKLES_PER_TILE = 7;

	private final Viewport viewport;

	private final Shape darkBackground;

	private final Shape background;

	private final Shape infraredBackground;

	private final Shape emptyBackground;

	private final Shape infraredEmptyBackground;

	public BackgroundView(Viewport viewport, long ecosystemSize) {
		this.viewport = viewport;

		darkBackground = new Rectangle(0, 0, ecosystemSize, ecosystemSize);
		darkBackground.setFill(Color.BLACK);

		emptyBackground = new Rectangle(0, 0, ecosystemSize, ecosystemSize);
		emptyBackground.setFill(BACKGROUND_COLOR);

		infraredEmptyBackground = new Rectangle(0, 0, ecosystemSize, ecosystemSize);
		infraredEmptyBackground.setFill(INFRARED_BACKGROUND_COLOR);

		Group backgroundGroup = createBackground(TILE_SIZE, BACKGROUND_COLOR);
		Group infraredBackgroundGroup = createBackground(TILE_SIZE, INFRARED_BACKGROUND_COLOR);
		for (int i = 0; i < SPECKLES_PER_TILE; i++) {
			int x = getRandomCoordinate(TILE_SIZE);
			int y = getRandomCoordinate(TILE_SIZE);
			backgroundGroup.getChildren().add(createSpeckle(x, y, NORMAL_SPECKLE_RADIUS));
			infraredBackgroundGroup.getChildren().add(createSpeckle(x, y, INFRARED_SPECKLE_RADIUS));
		}
		background = new Rectangle(0, 0, ecosystemSize, ecosystemSize);
		background.setFill(new ImagePattern(createSpecklesTexture(backgroundGroup), 0, 0, TILE_SIZE, TILE_SIZE, false));

		infraredBackground = new Rectangle(0, 0, ecosystemSize, ecosystemSize);
		infraredBackground.setFill(new ImagePattern(createSpecklesTexture(infraredBackgroundGroup), 0, 0, TILE_SIZE, TILE_SIZE, false));
	}

	public Node toNode(Light light) {
		Node result = getBackground(light);
		ViewportTransformer.applyTransforms(result, viewport);
		return result;
	}

	private Group createBackground(final int tileSize, Color color) {
		Group backgroundGroup = new Group();
		Shape emptySpace = new Rectangle(0, 0, tileSize, tileSize);
		emptySpace.setFill(color);
		backgroundGroup.getChildren().add(emptySpace);
		return backgroundGroup;
	}

	private WritableImage createSpecklesTexture(Group group) {
		WritableImage result = new WritableImage(TILE_SIZE, TILE_SIZE);
		Scene offScreenScene = new Scene(group, TILE_SIZE, TILE_SIZE);
		offScreenScene.snapshot(result);
		return result;
	}

	private Node getBackground(Light light) {
		if (light == Light.OFF)
			return darkBackground;

		Node result;
		if (viewport.getZoomLevel() <= MINIMUM_ZOOM_LEVEL_TO_SEE_SPECS)
			result = (light == Light.INFRARED) ? infraredEmptyBackground : emptyBackground;
		else
			result = (light == Light.INFRARED) ? infraredBackground : background;

		darkenWithDistance(result, viewport.getZoomLevel());
		return result;
	}

	private void darkenWithDistance(Node node, double zoomLevel) {
		double brightnessAdjust = -zoomLevel / 5;
		node.setEffect(new ColorAdjust(0, 0, brightnessAdjust, 0));
	}

	private Shape createSpeckle(int x, int y, double radius) {
		Shape result = new Circle(radius);
		result.setFill(SPECKLE_COLOR);
		result.getTransforms().add(new Translate(x, y));
		return result;
	}

	private int getRandomCoordinate(int maxSize) {
		double maxSpeckleRadius = Math.max(NORMAL_SPECKLE_RADIUS, INFRARED_SPECKLE_RADIUS);
		// Stay away from edges to avoid half-speckles
		return (int) (maxSpeckleRadius + Math.random() * (maxSize - (maxSpeckleRadius * 2)));
	}
}
