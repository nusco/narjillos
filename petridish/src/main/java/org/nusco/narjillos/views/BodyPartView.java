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

class BodyPartView extends ThingView {

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
		Rectangle result = new Rectangle(0, 0, getLengthIncludingOverlap(), bodyPart.getThickness());

		double arc = (bodyPart.getLength() * bodyPart.getThickness()) % 15 + 15;
		result.setArcWidth(arc);
		result.setArcHeight(arc);

		return result;
	}

	public Node toNode(double zoomLevel, boolean infraredOn) {
		if (bodyPart.getLength() == 0)
			return null; // atrophy
		
		rectangle.setFill(getColor(infraredOn));
		
		if (infraredOn) {
			rectangle.setStroke(Color.WHITE);
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

	private Color getColor(boolean infraredOn) {
		if (infraredOn)
			return Color.RED;
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), getAlpha());
	}

	private double getAlpha() {
		double result = getNarjillo().getEnergy() * 10 / getNarjillo().getMaxEnergy();
		return clipToRange(result, 0, 0.7);
	}

	public static Color toRGBColor(ColorByte colorByte) {
		return new Color(colorByte.getRed(), colorByte.getGreen(), colorByte.getBlue(), 1);
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		double margin = Math.max(bodyPart.getThickness() / 2, getLengthIncludingOverlap());
		return viewport.isVisible(bodyPart.getStartPoint(), margin);
	}

	private int getLengthIncludingOverlap() {
		return bodyPart.getLength() + (OVERLAP * 2);
	}

	private Narjillo getNarjillo() {
		return (Narjillo)getThing();
	}
}