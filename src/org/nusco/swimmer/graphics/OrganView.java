package org.nusco.swimmer.graphics;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.swimmer.body.Organ;
import org.nusco.swimmer.physics.Vector;

class OrganView {
	private final static boolean SHOW_INVISIBLE_ORGANS = true;
	
	private final Organ organ;

	public OrganView(Organ organ) {
		this.organ = organ;
	}

	public Node toShape() {
		if(!organ.isVisible())
			if(SHOW_INVISIBLE_ORGANS)
				return toCircle();
			else
				return null;
		return toRectangle();
	}

	private Rectangle toRectangle() {
		final int OVERLAP = 5;
		Rectangle result = new Rectangle(0, 0, organ.getLength() + (OVERLAP * 2), organ.getThickness());
		
		int arc = (organ.getLength() * organ.getThickness()) % 15 + 15;
		result.setArcWidth(arc);
		result.setArcHeight(arc);
		
		result.setFill(ColorEncoder.toColor(organ.getRGB()));
		if(organ.getThickness() > 5)
			result.setStroke(Color.BLACK);

		// shift towards the center of the screen
		result.getTransforms().add(new Translate(200, 400));
		
		// overlap slightly and shift to center based on thickness
		int widthCenter = organ.getThickness() / 2;
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
}