package org.nusco.swimmers.graphics;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.shared.physics.Vector;

public class SwimmerView extends ThingView {

	private final Swimmer swimmer;

	public SwimmerView(Swimmer swimmer) {
		this.swimmer = swimmer;
	}

	@Override
	public Node toNode() {
		Group result = new Group();
		result.getChildren().addAll(getOrganNodes());

		result.getChildren().add(new MouthView(swimmer.getCurrentTarget()).toNode());
		
		Vector position = swimmer.getPosition();
		result.getTransforms().add(new Translate(position.getX(), position.getY()));

		return result;
	}

	private List<Node> getOrganNodes() {
		List<Node> nodes = new LinkedList<>();
		addWithChildren(nodes, swimmer.getHead());
		return nodes;
	}

	private void addWithChildren(List<Node> result, Organ organ) {
		Node shape = new OrganView(organ).toNode();
		if (shape != null)
			result.add(shape);
		for (Organ child : organ.getChildren())
			addWithChildren(result, child);
	}

	public void tick() {
		swimmer.tick();
	}

	// TODO: this method should disappear once swimmers learn to find their own target
	public void setCurrentTarget(Vector target) {
		swimmer.setCurrentTarget(target);
	}
}
