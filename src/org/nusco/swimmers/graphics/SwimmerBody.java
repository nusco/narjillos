package org.nusco.swimmers.graphics;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.body.Head;
import org.nusco.swimmers.body.Organ;
import org.nusco.swimmers.body.Swimmer;
import org.nusco.swimmers.body.Vector;

public class SwimmerBody extends Parent {

	private Head head;

	public SwimmerBody(Swimmer swimmer) {
		this.head = (Head)swimmer.getHead();
	}

	public List<Node> getParts() {
		List<Node> result = new LinkedList<>();
		addWithChildren(result, head);
		return result;
	}

	private void addWithChildren(List<Node> result, Organ organ) {
		if(organ.isVisible())
			result.add(toRectangle(organ));
		else
			result.add(toBubble(organ));
		for(Organ child : organ.getChildren())
			addWithChildren(result, child);
	}

	private Rectangle toRectangle(Organ organ) {
		final int OVERLAP = 5;
		Rectangle result = new Rectangle(0, 0, organ.getLength() + (OVERLAP * 2), organ.getThickness());
		
		result.setArcWidth(15);
		result.setArcHeight(15);
		
		result.setFill(toColor(organ.getRGB()));
		result.setStroke(Color.BLACK);

		// shift towards the center of the screen
		result.getTransforms().add(new Translate(200, 400));
		
		// overlap slightly and shift to center based on thickness
		int widthCenter = organ.getThickness() / 2;
		result.getTransforms().add(new Translate(-OVERLAP, -widthCenter));

		// move to start point in world
		Vector startPoint = organ.getStartPoint();
		result.getTransforms().add(new Translate(startPoint.getX(), startPoint.getY()));

		// rotate in position
		result.getTransforms().add(new Rotate(organ.getAngle(), OVERLAP, widthCenter));

		return result;
	}

	private Color toColor(int rgb) {
		byte rgbByte = (byte)rgb;
		double red = (rgbByte & 0b00000111) / 7.0;
		double green = ((rgbByte & 0b00111000) >> 3) / 7.0;
		double blue = (((rgbByte & 0b11000000) >> 6) + 4) / 7.0;
		final double alpha = 0.6;
		return new Color(red, green, blue, alpha);
	}

	private Node toBubble(Organ organ) {
		Circle result = new Circle(3);
		
		Color baseColor = Color.BEIGE;
		result.setFill(new Color(baseColor.getBlue(), baseColor.getRed(), baseColor.getGreen(), 0.4));
		result.setStroke(Color.BLACK);

		// shift towards the center of the screen
		result.getTransforms().add(new Translate(200, 400));

		// move to start point in world
		Vector startPoint = organ.getStartPoint();
		result.getTransforms().add(new Translate(startPoint.getX(), startPoint.getY()));

		return result;
	}

	public void tick() {
		head.tick();
	}
}
