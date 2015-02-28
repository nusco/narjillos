package org.nusco.narjillos.views;

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

import org.nusco.narjillos.utilities.Viewport;

public class SpecklesView {

	private static final double NORMAL_SPECKLE_RADIUS = 2;
	private static final double INFRARED_SPECKLE_RADIUS = 2.5;
	private static final Color SPECKLE_COLOR = EcosystemView.BACKGROUND_COLOR.darker();
	
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
		infraredBackground.setFill(new ImagePattern(infraredBackgroundTexture, 0, 0, viewport.getSizeSC().x, viewport.getSizeSC().y, false));
	}

	public Node toNode(boolean infraredOn) {
		Node result = infraredOn ? infraredBackground : background;
		
		if (viewport.getZoomLevel() <= 0.25)
			return null;

		result.getTransforms().clear();
		result.getTransforms().add(new Translate(-viewport.getPositionEC().x, -viewport.getPositionEC().y));
		result.getTransforms().add(new Scale(viewport.getZoomLevel(), viewport.getZoomLevel(), viewport.getPositionEC().x, viewport.getPositionEC().y));
		
		return result;
	}

	private void createBackgroundTextures() {
		final int textureSize = 600;

		// TODO: remove this duplication
		
		Group backgroundGroup = new Group();
		Scene offScreenBackgroundScene = new Scene(backgroundGroup, textureSize, textureSize);
		Shape emptySpace = new Rectangle(0, 0, textureSize, textureSize);
		emptySpace.setFill(EcosystemView.BACKGROUND_COLOR);
		backgroundGroup.getChildren().add(emptySpace);

		Group infraredBackgroundGroup = new Group();
		Scene offScreenInfraredBackgroundScene = new Scene(infraredBackgroundGroup, textureSize, textureSize);
		Shape infraredEmptySpace = new Rectangle(0, 0, textureSize, textureSize);
		infraredEmptySpace.setFill(EcosystemView.INFRARED_BACKGROUND_COLOR);
		infraredBackgroundGroup.getChildren().add(infraredEmptySpace);
		
		for (int i = 0; i < 5; i++) {
			int x = getRandomCoordinate(textureSize);
			int y = getRandomCoordinate(textureSize);
			backgroundGroup.getChildren().add(createSpeckle(x, y, NORMAL_SPECKLE_RADIUS));
			infraredBackgroundGroup.getChildren().add(createSpeckle(x, y, INFRARED_SPECKLE_RADIUS));
		}

		backgroundTexture = new WritableImage(textureSize, textureSize);
		offScreenBackgroundScene.snapshot(backgroundTexture);

		infraredBackgroundTexture = new WritableImage(textureSize, textureSize);
		offScreenInfraredBackgroundScene.snapshot(infraredBackgroundTexture);
	}

	private Shape createSpeckle(int x, int y, double radius) {
		Shape result = new Circle(radius);
		result.setFill(SPECKLE_COLOR);
		result.getTransforms().add(new Translate(x, y));
		return result;
	}

	private int getRandomCoordinate(int maxSize) {
		double maxSpeckleRadius = Math.max(NORMAL_SPECKLE_RADIUS, INFRARED_SPECKLE_RADIUS);
		// Don't get too close to the edges - we don't want half-speckles
		return (int) (maxSpeckleRadius + Math.random() * (maxSize - (maxSpeckleRadius * 2)));
	}
}
