package org.nusco.swimmers.graphics;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;

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

	public Node getMouth() {
		return new VectorView(swimmer.getCurrentTarget(), swimmer.getPosition()).toShape();
	}

	private void addWithChildren(List<Node> result, Organ organ) {
		Node shape = new OrganView(organ).toShape();
		if (shape != null)
			result.add(shape);
		for (Organ child : organ.getChildren())
			addWithChildren(result, child);
	}

	public void tick() {
		swimmer.tick();
	}

	public void setCurrentTarget(Vector target) {
		swimmer.setCurrentTarget(target);
	}
}
