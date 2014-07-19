package org.nusco.swimmers.views;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.BodyPart;
import org.nusco.swimmers.shared.physics.Vector;

class SwimmerView extends ThingView {
	
	private final Group group = new Group();
	private final List<OrganView> organViews;
	private final MouthView mouthView;
	private final EyeView eyeView;

	public SwimmerView(Swimmer swimmer) {
		super(swimmer);
		organViews = createOrganViews();
		mouthView = new MouthView(swimmer);
		eyeView = new EyeView(swimmer);
	}

	@Override
	public Node toNode() {
		group.getChildren().clear();
		group.getChildren().add(mouthView.toNode());
		group.getChildren().addAll(getOrganNodes());
		group.getChildren().add(eyeView.toNode());
		Vector position = getSwimmer().getPosition();
		group.getTransforms().clear();
		group.getTransforms().add(new Translate(position.x, position.y));
		group.setEffect(PondView.SHADOW);
		return group;
	}

	private List<Node> getOrganNodes() {
		List<Node> result = new LinkedList<>();
		for (OrganView view : organViews) {
			Node node = view.toNode();
			if (node != null)
				result.add(node);
		}
		return result;
	}

	private List<OrganView> createOrganViews() {
		List<OrganView> result = new LinkedList<>();
		addWithChildren(getSwimmer().getHead(), result);
		return result;
	}

	private void addWithChildren(BodyPart organ, List<OrganView> result) {
		result.add(new OrganView(organ, getSwimmer()));
		for (BodyPart child : organ.getChildren())
			addWithChildren(child, result);
	}

	private Swimmer getSwimmer() {
		return (Swimmer)getThing();
	}
}
