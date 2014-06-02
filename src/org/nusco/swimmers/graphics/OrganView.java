package org.nusco.swimmers.graphics;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.physics.Vector;

class OrganView {
	// for quick debugging
	private final static boolean SHOW_INVISIBLE_ORGANS = false;
	
	private final Organ organ;

	public OrganView(Organ organ) {
		this.organ = organ;
	}

	public Node toShape() {
		if(organ.getLength() == 0)
			if(SHOW_INVISIBLE_ORGANS)
				return toCircle();
			else
				return null;
		return toRectangle();
	}

	private Rectangle toRectangle() {
		final int OVERLAP = 5;
		Rectangle result = new Rectangle(0, 0, organ.getLength() + (OVERLAP * 2), organ.getThickness());
		
		double arc = (organ.getLength() * organ.getThickness()) % 15 + 15;
		result.setArcWidth(arc);
		result.setArcHeight(arc);
		
		result.setFill(toColor(organ.getRGB()));
		if(organ.getThickness() > 5)
			result.setStroke(Color.BLACK);

		// shift towards the center of the screen
		result.getTransforms().add(new Translate(200, 400));
		
		// overlap slightly and shift to center based on thickness
		double widthCenter = organ.getThickness() / 2;
		result.getTransforms().add(new Translate(-OVERLAP, -widthCenter));

		result.getTransforms().add(moveToStartPoint());

		// rotate in position
		result.getTransforms().add(new Rotate(organ.getAngle(), OVERLAP, widthCenter));

		return result;
	}

	private Node toCircle() {
		Circle result = new Circle(3);
		
		Color baseColor = Color.BEIGE;
		result.setFill(new Color(baseColor.getBlue(), baseColor.getRed(), baseColor.getGreen(), 0.4));
		result.setStroke(Color.BLACK);

		// shift towards the center of the screen
		result.getTransforms().add(new Translate(200, 400));

		result.getTransforms().add(moveToStartPoint());

		return result;
	}

	private  Translate moveToStartPoint() {
		Vector startPoint = organ.getStartPoint();
		return new Translate(startPoint.getX(), startPoint.getY());
	}
	
	public static Color toColor(int rgb) {
		byte rgbByte = (byte)rgb;
		double MAX_VALUE_IN_3_BITS = 7.0;
		double red = (rgbByte & 0b00000111) / MAX_VALUE_IN_3_BITS;
		double green = ((rgbByte & 0b00111000) >> 3) / MAX_VALUE_IN_3_BITS;
		double blue = ((rgbByte & 0b11000000) >> 5) / MAX_VALUE_IN_3_BITS;
		final double alpha = 0.6;
		return new Color(red, green, blue, alpha);
	}
}