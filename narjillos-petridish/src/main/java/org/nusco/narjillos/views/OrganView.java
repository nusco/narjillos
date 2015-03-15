package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.Fiber;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;
import org.nusco.narjillos.utilities.Viewport;

class OrganView implements ItemView {

	private static final double VERY_LOW_MAGNIFICATION = 0.003;
	private static final double LOW_MAGNIFICATION = 0.015;
	private static final double MEDIUM_MAGNIFICATION = 0.025;
	private static final double HIGH_MAGNIFICATION = 0.040;

	private static final double MOTION_BLUR_DISTANCE = 0.1;
	private static final int MOTION_BLUR_THRESHOLD = 10;
	private static final int MOTION_BLUR_INTENSITY = 2;

	private final static int OVERLAP = 7;

	private final Narjillo narjillo;
	private final Organ organ;
	private final Color color;
	private final Rectangle rectangle;

	private Segment previousOrganPosition;

	public OrganView(Organ bodyPart, Narjillo narjillo) {
		this.narjillo = narjillo;
		this.organ = bodyPart;
		color = toRGBColor(bodyPart.getFiber());
		rectangle = createRectangle();
		previousOrganPosition = organ.getPositionInSpace();
	}

	private Rectangle createRectangle() {
		Rectangle result = new Rectangle(0, 0, getLengthWithOverlapMargin(organ), organ.getThickness());

		double arc = (organ.getAdultLength() * organ.getAdultThickness()) % 15 + 15;
		result.setArcWidth(arc);
		result.setArcHeight(arc);

		return result;
	}

	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (organ.isAtrophic())
			return null;

		double alpha = getAlpha(zoomLevel);
		if (alpha == 0)
			return null; // perfectly transparent. don't draw.

		double length = getLengthWithOverlapMargin(organ);
		double thickness = organ.getThickness();
		double angle = organ.getAbsoluteAngle();

		rectangle.setWidth(length);
		rectangle.setHeight(thickness);

		rectangle.setFill(getColor(infraredOn, alpha));

		if (infraredOn && zoomLevel > HIGH_MAGNIFICATION) {
			rectangle.setStroke(new Color(1, 1, 1, alpha));
			rectangle.setStrokeWidth(3);
		} else {
			rectangle.setStrokeWidth(0);
		}

		rectangle.getTransforms().clear();

		// overlap slightly and shift to center based on thickness
		double widthCenter = thickness / 2;
		rectangle.getTransforms().add(new Translate(-OVERLAP, -widthCenter));
		rectangle.getTransforms().add(moveToStartPoint());

		// rotate in position
		rectangle.getTransforms().add(new Rotate(angle, OVERLAP, widthCenter));

		rectangle.setEffect(null);
		addMotionBlur(zoomLevel, effectsOn);
		// store position for motion blur in the next frame
		previousOrganPosition = organ.getPositionInSpace();

		return rectangle;
	}

	private void addMotionBlur(double zoomLevel, boolean effectsOn) {
		if (!effectsOn)
			return;
		
		if (zoomLevel < MOTION_BLUR_DISTANCE)
			return;

		Vector movement = calculateMovement();

		if (movement.equals(Vector.ZERO))
			return;

		double velocity = movement.getLength();

		if (velocity < MOTION_BLUR_THRESHOLD)
			return;

		try {
			rectangle.setEffect(new MotionBlur(movement.getAngle(), velocity / MOTION_BLUR_THRESHOLD * MOTION_BLUR_INTENSITY));
		} catch (ZeroVectorException e) {
		}
	}

	private Vector calculateMovement() {
		Segment currentOrganPosition = organ.getPositionInSpace();

		if (!areDistant(currentOrganPosition, previousOrganPosition))
			return Vector.ZERO;

		return currentOrganPosition.getDistanceFrom(previousOrganPosition);
	}

	private boolean areDistant(Segment position1, Segment position2) {
		// This stuff has no physical meaning, but it works as a rule of thumb
		// to optimize graphics calculations. It favours linear operations
		// without trigonometry, to keep things fast.

		if (areDistant(position1.getStartPoint(), position2.getStartPoint()))
			return true;

		if (areDistant(position1.getEndPoint(), position2.getEndPoint()))
			return false;

		return false;
	}

	private boolean areDistant(Vector point1, Vector point2) {
		return Math.abs(point2.x - point1.x) > MOTION_BLUR_THRESHOLD || Math.abs(point2.y - point1.y) > MOTION_BLUR_THRESHOLD;
	}

	private Translate moveToStartPoint() {
		Vector startPoint = organ.getStartPoint();
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
		return organ.getLength() < 100 || organ.getThickness() < 40;
	}

	private boolean isVerySmall() {
		return organ.getLength() < 50 || organ.getThickness() < 20;
	}

	private double getEnergyAlpha() {
		return getNarjillo().getEnergy().getMaximumValue() / getNarjillo().getEnergy().getValue();
	}
	
	protected double clipToRange(double result, double min, double max) {
		return Math.max(min, Math.min(max, result));
	}

	private Color toRGBColor(Fiber fiber) {
		return new Color(fiber.getPercentOfRed(), fiber.getPercentOfGreen(), fiber.getPercentOfBlue(), 1);
	}

	@Override
	public boolean isVisible(Viewport viewport) {
		double margin = Math.max(organ.getThickness() / 2, getLengthWithOverlapMargin(organ));
		return viewport.isVisible(organ.getStartPoint(), margin);
	}

	private double getLengthWithOverlapMargin(Organ organ) {
		return organ.getLength() + (OVERLAP * 2);
	}

	private Narjillo getNarjillo() {
		return narjillo;
	}
}