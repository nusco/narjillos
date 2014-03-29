package org.nusco.swimmers.graphics;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.body.HeadPart;
import org.nusco.swimmers.body.Part;
import org.nusco.swimmers.body.Vector;

public class SwimmerBody extends Parent {

	private HeadPart head;

	public SwimmerBody(HeadPart headPart) {
		this.head = headPart;
	}

	public List<Node> getParts() {
		List<Node> result = new LinkedList<>();
		addWithChildren(result, head);
		return result;
	}

	private void addWithChildren(List<Node> result, Part part) {
		result.add(toRectangle(part));
		for(Part child : part.getChildren())
			addWithChildren(result, child);
	}

	private Rectangle toRectangle(Part part) {
		final int OVERLAP = 5;
		Rectangle rect = new Rectangle(0, 0, part.getLength() + (OVERLAP * 2), part.getThickness());
		
		rect.setArcWidth(15);
		rect.setArcHeight(15);
		rect.setFill(Color.AQUAMARINE);
		rect.setStroke(Color.BLACK);

		// shift towards the center of the screen
		rect.getTransforms().add(new Translate(200, 400));
		
		// overlap slightly and shift to center based on thickness
		int widthCenter = part.getThickness() / 2;
		rect.getTransforms().add(new Translate(-OVERLAP, -widthCenter));

		// move to start point in world
		Vector startPoint = part.getStartPoint();
		rect.getTransforms().add(new Translate(startPoint.getX(), startPoint.getY()));

		// rotate in position
		rect.getTransforms().add(new Rotate(part.getAngle(), OVERLAP, widthCenter));

		return rect;
	}
}
