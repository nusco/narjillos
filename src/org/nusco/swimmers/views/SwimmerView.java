package org.nusco.swimmers.views;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.shared.physics.Vector;

class SwimmerView extends ThingView {

	private final Swimmer swimmer;

	private final Group group = new Group();
	private final List<OrganView> organViews;
	private final MouthView mouthView;

	public SwimmerView(Swimmer swimmer) {
		this.swimmer = swimmer;
		organViews = createOrganViews();
		mouthView = new MouthView(swimmer);
	}

	@Override
	public Node toNode() {
		group.getChildren().clear();
		group.getChildren().addAll(getOrganNodes());
		group.getChildren().add(mouthView.toNode());
		Vector position = swimmer.getPosition();
		group.getTransforms().clear();
		group.getTransforms().add(new Translate(position.getX(), position.getY()));
		return group;
	}

	private List<Node> getOrganNodes() {
		List<Node> result = new LinkedList<>();
		for (ThingView view : organViews) {
			Node node = view.toNode();
			if (node != null)
				result.add(node);
		}
		return result;
	}

	private List<OrganView> createOrganViews() {
		List<OrganView> result = new LinkedList<>();
		addWithChildren(swimmer.getHead(), result);
		return result;
	}

	private void addWithChildren(Organ organ, List<OrganView> result) {
		result.add(new OrganView(organ));
		for (Organ child : organ.getChildren())
			addWithChildren(child, result);
	}

	// TODO: this method should disappear once swimmers learn
	// to find their own target
	public void setCurrentTarget(Vector target) {
		swimmer.setCurrentTarget(target);
	}
}
