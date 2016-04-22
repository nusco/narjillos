package org.nusco.narjillos.application.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.geometry.ZeroVectorAngleException;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.Fiber;

class EyesView implements ItemView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.15;

	private final Narjillo narjillo;
	private final Circle eye1;
	private final Circle eye2;
	private final Circle pupil1;
	private final Circle pupil2;
	private final Group group = new Group();

	private final double eyeCenteringTranslation;
	private final double pupilTranslation;

	private final double eyeRed;
	private final double eyeGreen;
	private final double eyeBlue;

	public EyesView(Narjillo narjillo) {
		this.narjillo = narjillo;

		Fiber fiber = narjillo.getBody().getHead().getFiber();
		this.eyeRed = fiber.getPercentOfRed();
		this.eyeGreen = fiber.getPercentOfGreen();
		this.eyeBlue = fiber.getPercentOfBlue();
		
		// "Random qualities": we want something that looks random across narjillos,
		// but stays the same for the same narjillo even after saving and reloading
		double someRandomQuality = narjillo.getBody().getAdultMass();
		double someOtherRandomQuality = narjillo.getBody().getEnergyToChildren();
		
		this.eye1 = new Circle(someRandomQuality % 5 + 7);
		this.eye2 = new Circle(someOtherRandomQuality % 5 + 7);
		this.pupil1 = new Circle(Math.min(eye1.getRadius() - 2, someRandomQuality % 6 + 1));
		this.pupil2 = new Circle(Math.min(eye1.getRadius() - 2, someOtherRandomQuality % 6 + 1));

		eyeCenteringTranslation = eye1.getRadius() - eye2.getRadius();
		pupilTranslation = Math.min(eye2.getRadius() - pupil2.getRadius(), eye1.getRadius() - pupil1.getRadius());

		this.eye1.getTransforms().add(new Translate(eyeCenteringTranslation - eye1.getRadius() + 1, 0));
		this.eye2.getTransforms().add(new Translate(eyeCenteringTranslation + eye2.getRadius() - 1, 0));
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL || !effectsOn)
			return null;
		
		group.getChildren().clear();
		
		Color eyeColor = toEyeColor(zoomLevel, infraredOn);
		eye1.setFill(eyeColor);
		eye2.setFill(eyeColor);
		
		group.getChildren().add(eye1);
		group.getChildren().add(eye2);

		double eyesDirection = narjillo.getBody().getHead().getAbsoluteAngle() + 90;

		Color pupilColor = toPupilColor(zoomLevel);
		pupil1.setFill(pupilColor);
		pupil2.setFill(pupilColor);

		double pupilDirection;
		try {
			pupilDirection = narjillo.getMouth().getDirection().getAngle() - eyesDirection - 90;
		} catch (ZeroVectorAngleException e) {
			pupilDirection = 0;
		}
		
		pupil1.getTransforms().clear();
		pupil1.getTransforms().add(new Translate(eyeCenteringTranslation - eye1.getRadius() + 1, pupilTranslation));
		pupil1.getTransforms().add(new Rotate(pupilDirection, 0, -pupilTranslation));

		pupil2.getTransforms().clear();
		pupil2.getTransforms().add(new Translate(eyeCenteringTranslation + eye2.getRadius() - 1, pupilTranslation));
		pupil2.getTransforms().add(new Rotate(pupilDirection, 0, -pupilTranslation));

		group.getChildren().add(pupil1);
		group.getChildren().add(pupil2);
		
		group.getTransforms().clear();
		Vector position = narjillo.getPosition();
		group.getTransforms().add(new Translate(position.x, position.y));
		group.getTransforms().add(new Rotate(eyesDirection));
		
		return group;
	}

	@Override
	public boolean isVisible(Viewport viewport) {
		double margin = Math.max(eye1.getRadius() *2, eye2.getRadius() * 2);
		return viewport.isVisible(narjillo.getPosition(), margin);
	}

	private Color toEyeColor(double zoomLevel, boolean infraredOn) {
		if (infraredOn)
			return Color.WHITE;
		double red = narjillo.isInPain() ? 1 : eyeRed;
		double green = narjillo.isInPain() ? 0 : eyeGreen;
		double blue = narjillo.isInPain() ? 0 : eyeBlue;
		return new Color(red, green, blue, getZoomAlpha(zoomLevel));
	}

	private Color toPupilColor(double zoomLevel) {
		return new Color(0, 0, 0, Math.min(getZoomAlpha(zoomLevel), getEnergyAlpha(zoomLevel)));
	}

	private double getEnergyAlpha(double zoomLevel) {
		double maxEnergy = narjillo.getEnergy().getMaximumValue();
		if (maxEnergy <= 0)
			return 0;
		return narjillo.getEnergy().getValue() * 3 / maxEnergy;
	}

	private double getZoomAlpha(double zoomLevel) {
		return clipToRange((zoomLevel - MINIMUM_ZOOM_LEVEL) * 4, 0, 1);
	}

	private double clipToRange(double result, double min, double max) {
		return Math.max(min, Math.min(max, result));
	}
}
