package org.nusco.narjillos.application.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.physics.Segment;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.physics.ZeroVectorException;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Fiber;

public class OrganView implements ItemView {

	private static final double MAX_OVERLAP = 7;
	private static final double MIN_CIRCLE_RADIUS = 4;

	private static final double VERY_LOW_MAGNIFICATION = 0.003;
	private static final double LOW_MAGNIFICATION = 0.015;
	private static final double MEDIUM_MAGNIFICATION = 0.025;
	private static final double HIGH_MAGNIFICATION = 0.040;
	private static final double VERY_HIGH_MAGNIFICATION = 0.14;

	private static final double MOTION_BLUR_DISTANCE = 0.1;
	private static final int MOTION_BLUR_THRESHOLD = 10;
	private static final int MOTION_BLUR_INTENSITY = 2;

	private final Narjillo narjillo;
	private final ConnectedOrgan organ;
	private final Color baseColor;

	private final Rectangle segment;
	private final boolean hasJoint;
	private final Circle joint;
	Group group = new Group();

	private Segment previousOrganPosition;

	public OrganView(ConnectedOrgan organ, Narjillo narjillo) {
		this.organ = organ;
		this.narjillo = narjillo;

		baseColor = toRGBColor(this.organ.getFiber());

		segment = createSegment();
		
		hasJoint = shouldHaveAJoint();
		if (hasJoint)
			joint = new Circle(MIN_CIRCLE_RADIUS);
		else
			joint = null;
		
		previousOrganPosition = this.organ.getPositionInSpace();
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (organ.isAtrophic())
			return null;

		double alpha = getAlpha(zoomLevel);
		if (alpha == 0)
			return null; // perfectly transparent. don't draw.

		Shape shape = getShape(zoomLevel, effectsOn);

		addFill(shape, infraredOn, alpha);
		addStroke(shape, infraredOn, alpha, zoomLevel);
		addMotionBlurEffect(shape, zoomLevel, effectsOn);

		previousOrganPosition = organ.getPositionInSpace();

		return shape;
	}

	@Override
	public boolean isVisible(Viewport viewport) {
		double margin = Math.max(segment.getWidth() / 2 + getOverlap(), segment.getHeight() / 2);
		if (joint != null)
			margin = margin + joint.getRadius();  // worst case
		return viewport.isVisible(organ.getCenterOfMass(), margin);
	}

	private Rectangle createSegment() {
		Rectangle result = new Rectangle(0, 0, organ.getLength() + getOverlap() * 2, organ.getThickness());
		
		double arc = (organ.getAdultLength() * organ.getAdultThickness()) % 15 + 15;
		result.setArcWidth(arc);
		result.setArcHeight(arc);
		
		return result;
	}

	private double getOverlap() {
		return Math.min(organ.getThickness() / 2, MAX_OVERLAP);
	}

	private boolean shouldHaveAJoint() {
		if (organ.isAtrophic())
			return false;
		
		if (organ.isLeaf())
			return false;
		
		boolean isJointPotentiallyVisible = getJointRadius(organ.getAdultThickness()) > Math.min(organ.getAdultThickness(), getOverlap());
		if (!isJointPotentiallyVisible)
			return false;

		return true;
	}

	private Shape getShape(double zoomLevel, boolean effectsOn) {

		segment.setWidth(organ.getLength() + getOverlap() * 2);
		segment.setHeight(organ.getThickness());
		
		segment.getTransforms().clear();
		// overlap slightly and shift to center based on thickness
		double widthCenter = organ.getThickness() / 2;
		segment.getTransforms().add(moveToStartPoint());
		segment.getTransforms().add(new Translate(-getOverlap(), -widthCenter));
		segment.getTransforms().add(new Rotate(organ.getAbsoluteAngle(), getOverlap(), widthCenter));

		boolean isHighDetail = hasJoint && zoomLevel >= VERY_HIGH_MAGNIFICATION && effectsOn;
		
		if (!isHighDetail)
			return segment;
		
		joint.setRadius(getJointRadius(organ.getThickness()));
		
		joint.getTransforms().clear();
		joint.getTransforms().add(moveToStartPoint());
		joint.getTransforms().add(new Translate(organ.getLength(), 0));
		joint.getTransforms().add(new Rotate(organ.getAbsoluteAngle(), -organ.getLength(), 0));

		return Path.union(segment, joint);
	}

	private double getJointRadius(double organThickness) {
		return Math.max(organThickness / 8, MIN_CIRCLE_RADIUS);
	}

	private void addFill(Shape organShape, boolean infraredOn, double alpha) {
		if (infraredOn)
			organShape.setFill(new Color(1, 0, 0, alpha));
		else
			organShape.setFill(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha));
	}

	private void addStroke(Shape organShape, boolean infraredOn, double alpha, double zoomLevel) {
		if (infraredOn && zoomLevel > HIGH_MAGNIFICATION) {
			organShape.setStroke(new Color(1, 1, 1, alpha));
			organShape.setStrokeWidth(3);
		} else {
			organShape.setStrokeWidth(0);
		}
	}

	private void addMotionBlurEffect(Shape organShape, double zoomLevel, boolean effectsOn) {
		organShape.setEffect(null);

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
			organShape.setEffect(new MotionBlur(movement.getAngle(), velocity / MOTION_BLUR_THRESHOLD * MOTION_BLUR_INTENSITY));
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
		return organ.getLength() + getOverlap() * 2 < 100 || organ.getThickness() < 40;
	}

	private boolean isVerySmall() {
		return organ.getLength() + getOverlap() * 2 < 50 || organ.getThickness() < 20;
	}

	private double getEnergyAlpha() {
		double alpha = getNarjillo().getEnergy().getValue() / (getNarjillo().getEnergy().getMaximumValue() / 10);
		return Math.min(1, alpha);
	}

	private Color toRGBColor(Fiber fiber) {
		return new Color(fiber.getPercentOfRed(), fiber.getPercentOfGreen(), fiber.getPercentOfBlue(), 1);
	}

	private Narjillo getNarjillo() {
		return narjillo;
	}
}