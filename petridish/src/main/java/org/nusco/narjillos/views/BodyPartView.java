package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;
import org.nusco.narjillos.views.utilities.Viewport;

class BodyPartView extends ThingView {

	private final static double VERY_LOW_MAGNIFICATION = 0.003;
	private final static double LOW_MAGNIFICATION = 0.015;
	private final static double MEDIUM_MAGNIFICATION = 0.025;
	private final static double HIGH_MAGNIFICATION = 0.040;

	private final static int OVERLAP = 7;

	private final BodyPart bodyPart;
	private final Color color;
	private final Rectangle rectangle;

	public BodyPartView(BodyPart bodyPart, Narjillo narjillo) {
		super(narjillo);
		this.bodyPart = bodyPart;
		color = toRGBColor(bodyPart.getColor());
		rectangle = createRectangle();
	}

	private Rectangle createRectangle() {
		return new Rectangle(0, 0, getLengthIncludingOverlap(), bodyPart.getThickness());
	}

	public Node toNode(double zoomLevel, boolean infraredOn) {
		if (bodyPart.isAtrophic())
			return null; // TODO: fix atrophy

		double alpha = getAlpha(zoomLevel);
		if (alpha == 0)
			return null; // perfectly transparent. don't draw.

		rectangle.setWidth(getLengthIncludingOverlap());
		rectangle.setHeight(bodyPart.getThickness());

		double arc = (bodyPart.getLength() * bodyPart.getThickness()) % 15 + 15;
		rectangle.setArcWidth(arc);
		rectangle.setArcHeight(arc);

		rectangle.setFill(getColor(infraredOn, alpha));

		if (infraredOn && zoomLevel > HIGH_MAGNIFICATION) {
			rectangle.setStroke(new Color(1, 1, 1, alpha));
			rectangle.setStrokeWidth(3);
		} else {
			rectangle.setStrokeWidth(0);
		}

		rectangle.getTransforms().clear();

		// overlap slightly and shift to center based on thickness
		double widthCenter = bodyPart.getThickness() / 2;
		rectangle.getTransforms().add(new Translate(-OVERLAP, -widthCenter));
		rectangle.getTransforms().add(moveToStartPoint());

		// rotate in position
		rectangle.getTransforms().add(new Rotate(bodyPart.getAbsoluteAngle(), OVERLAP, widthCenter));

		return rectangle;
	}

	private Translate moveToStartPoint() {
		Vector startPoint = bodyPart.getStartPoint();
		return new Translate(startPoint.x, startPoint.y);
	}

	private Color getColor(boolean infraredOn, double alpha) {
		if (infraredOn)
			return new Color(1, 0, 0, alpha);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	private double getAlpha(double zoomLevel) {
		double distanceAlpha = getDistanceAlpha(zoomLevel);
		double energyAlpha = getEnergyAlpha();
		return Math.min(energyAlpha, distanceAlpha);
	}

	private double getDistanceAlpha(double zoomLevel) {
		if (zoomLevel <= VERY_LOW_MAGNIFICATION)
			return 0; // nothing visible

		if (zoomLevel > VERY_LOW_MAGNIFICATION && zoomLevel <= LOW_MAGNIFICATION) {
			if (isSmall())
				return 0;
			return (zoomLevel - VERY_LOW_MAGNIFICATION) / (LOW_MAGNIFICATION - VERY_LOW_MAGNIFICATION);
		}
		
		if (zoomLevel > LOW_MAGNIFICATION && zoomLevel <= MEDIUM_MAGNIFICATION) {
			if (isVerySmall())
				return 0;
			if (isSmall())
				return (zoomLevel - LOW_MAGNIFICATION) / (MEDIUM_MAGNIFICATION - LOW_MAGNIFICATION);
		}
		
		if (zoomLevel > MEDIUM_MAGNIFICATION && zoomLevel <= HIGH_MAGNIFICATION) {
			if (isVerySmall())
				return (zoomLevel - MEDIUM_MAGNIFICATION) / (HIGH_MAGNIFICATION - MEDIUM_MAGNIFICATION);
		}

		return 1;
	}

	private boolean isSmall() {
		return bodyPart.getLength() < 100 || bodyPart.getThickness() < 40;
	}

	private boolean isVerySmall() {
		return bodyPart.getLength() < 50 || bodyPart.getThickness() < 20;
	}

	private double getEnergyAlpha() {
		double currentEnergyPercent = getNarjillo().getEnergyPercent();
		return clipToRange(currentEnergyPercent * 10, 0, 1);
	}

	public static Color toRGBColor(ColorByte colorByte) {
		return new Color(colorByte.getRed(), colorByte.getGreen(), colorByte.getBlue(), 1);
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		double margin = Math.max(bodyPart.getThickness() / 2, getLengthIncludingOverlap());
		return viewport.isVisible(bodyPart.getStartPoint(), margin);
	}

	private double getLengthIncludingOverlap() {
		return bodyPart.getLength() + (OVERLAP * 2);
	}

	private Narjillo getNarjillo() {
		return (Narjillo) getThing();
	}
}