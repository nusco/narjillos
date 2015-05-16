package org.nusco.narjillos.application.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.creature.Narjillo;

class EyesView implements ItemView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.18;

	private final Narjillo narjillo;
	private final Circle eye1;
	private final Circle eye2;
	private final Circle pupil1;
	private final Circle pupil2;
	private final Group group = new Group();

	public EyesView(Narjillo narjillo) {
		this.narjillo = narjillo;

		// "Random qualities": we want something that looks random across narjillos,
		// but stays the same for the same narjillo even after saving and reloading
		double someRandomQuality = narjillo.getBody().getAdultMass();
		double someOtherRandomQuality = narjillo.getBody().getEnergyToChildren();
		
		this.eye1 = new Circle(someRandomQuality % 6 + 4);
		this.eye2 = new Circle(someOtherRandomQuality % 6 + 4);
		this.pupil1 = new Circle(Math.min(eye1.getRadius() - 2, someRandomQuality % 3 + 1));
		this.pupil2 = new Circle(Math.min(eye1.getRadius() - 2, someOtherRandomQuality % 3 + 1));

		this.eye1.getTransforms().add(new Translate(-eye1.getRadius() + 1, 0));
		this.eye2.getTransforms().add(new Translate(eye2.getRadius() - 1, 0));
		this.pupil1.getTransforms().add(new Translate(-eye1.getRadius() + 1, 0));
		this.pupil2.getTransforms().add(new Translate(eye2.getRadius() - 1, 0));
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL || !effectsOn)
			return null;
		
		group.getChildren().clear();
		
		Color eyeColor = toEyeColor(zoomLevel);
		eye1.setFill(eyeColor);
		eye2.setFill(eyeColor);
		
		group.getChildren().add(eye1);
		group.getChildren().add(eye2);

		if (!infraredOn) {
			Color pupilColor = toPupilColor(zoomLevel);
			pupil1.setFill(pupilColor);
			pupil2.setFill(pupilColor);
			
			group.getChildren().add(pupil1);
			group.getChildren().add(pupil2);
		}
		
		group.getTransforms().clear();
		Vector position = narjillo.getPosition();
		group.getTransforms().add(new Translate(position.x, position.y));
		group.getTransforms().add(new Rotate(narjillo.getBody().getHead().getAbsoluteAngle() + 90));
		
		return group;
	}

	@Override
	public boolean isVisible(Viewport viewport) {
		double margin = Math.max(eye1.getRadius() *2, eye2.getRadius() * 2);
		return viewport.isVisible(narjillo.getPosition(), margin);
	}

	private Color toEyeColor(double zoomLevel) {
		return new Color(1.0, 1.0, 1.0, getZoomOpacity(zoomLevel));
	}

	private Color toPupilColor(double zoomLevel) {
		return new Color(0, 0, 0, Math.min(getZoomOpacity(zoomLevel), getEnergyOpacity(zoomLevel)));
	}

	private double getEnergyOpacity(double zoomLevel) {
		double maxEnergy = narjillo.getEnergy().getMaximumValue();
		if (maxEnergy <= 0)
			return 0;
		return narjillo.getEnergy().getValue() * 3 / maxEnergy;
	}

	private double getZoomOpacity(double zoomLevel) {
		return clipToRange((zoomLevel - MINIMUM_ZOOM_LEVEL) * 4, 0, 1);
	}

	private double clipToRange(double result, double min, double max) {
		return Math.max(min, Math.min(max, result));
	}
}
