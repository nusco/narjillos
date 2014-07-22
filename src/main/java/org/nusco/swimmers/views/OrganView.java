package org.nusco.swimmers.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.creature.Narjillo;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.utilities.ColorByte;

class OrganView extends ThingView {

	private final static int OVERLAP = 5;

	private final Organ organ;
	private final Color color;
	private final Rectangle rectangle;
	
	public OrganView(Organ organ, Narjillo narjillo) {
		super(narjillo);
		this.organ = organ;
		color = toRGBColor(organ.getColor());
		rectangle = createRectangle();
	}

	private Rectangle createRectangle() {
		Rectangle result = new Rectangle(0, 0, getLengthIncludingOverlap(), organ.getThickness());

		double arc = (organ.getLength() * organ.getThickness()) % 15 + 15;
		result.setArcWidth(arc);
		result.setArcHeight(arc);

		return result;
	}

	public Node toNode(double zoomLevel) {
		rectangle.setFill(getColor());
		
		rectangle.getTransforms().clear();
		
		// overlap slightly and shift to center based on thickness
		double widthCenter = organ.getThickness() / 2;
		rectangle.getTransforms().add(new Translate(-OVERLAP, -widthCenter));
		rectangle.getTransforms().add(moveToStartPoint());
		
		// rotate in position
		rectangle.getTransforms().add(new Rotate(organ.getAbsoluteAngle(), OVERLAP, widthCenter));
		
		return rectangle;
	}

	private Translate moveToStartPoint() {
		Vector startPoint = organ.getStartPoint();
		return new Translate(startPoint.x, startPoint.y);
	}

	private Color getColor() {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), getAlpha());
	}

	private double getAlpha() {
		return Math.min(0.7, getNarjillo().getEnergy() * 6 / Narjillo.MAX_ENERGY);
	}

	public static Color toRGBColor(ColorByte colorByte) {
		return new Color(colorByte.getRed(), colorByte.getGreen(), colorByte.getBlue(), 1);
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		double margin = Math.max(organ.getThickness() / 2, getLengthIncludingOverlap());
		return viewport.isVisible(getNarjillo().getPosition().plus(organ.getStartPoint()), margin);
	}

	private int getLengthIncludingOverlap() {
		return organ.getLength() + (OVERLAP * 2);
	}

	private Narjillo getNarjillo() {
		return (Narjillo)getThing();
	}
}