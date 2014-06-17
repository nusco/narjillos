package org.nusco.swimmers.graphics;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.physics.Vector;

public class SwimmerView extends Parent {

	static final int OFFSET_X = 200;
	static final int OFFSET_Y = 400;

	private final Swimmer swimmer;

	public SwimmerView(Swimmer swimmer) {
		this.swimmer = swimmer;
	}

	public List<Node> getParts() {
		List<Node> result = new LinkedList<>();
		addWithChildren(result, swimmer.getHead());
		return result;
	}

	public Node getTarget() {
		return new VectorView(swimmer.getCurrentTarget()).toShape();
	}

	private void addWithChildren(List<Node> result, Organ organ) {
		Node shape = new OrganView(organ).toShape();
		if(shape != null)
			result.add(shape);
		for(Organ child : organ.getChildren())
			addWithChildren(result, child);
	}

	public List<Rectangle> getChangeVectors() {
		List<Rectangle> results = new LinkedList<>();
		for(Organ part : swimmer.getParts()) {
			Vector peek = part.peek;
			Rectangle line = new Rectangle(0, 0, peek.getLength(), 2);
			line.setFill(Color.BLUEVIOLET);
			line.getTransforms().add(new Translate(200, 400));
			line.getTransforms().add(new Translate(part.getStartPoint().getX(), part.getStartPoint().getY()));
			line.getTransforms().add(new Rotate(peek.getAngle()));
			results.add(line);
		}
		return results;
	}

	public void tick() {
		swimmer.tick();
	}

	public void setCurrentTarget(Vector target) {
		swimmer.setCurrentTarget(target);
	}
}
