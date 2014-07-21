package org.nusco.swimmers.views;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.creature.Narjillo;
import org.nusco.swimmers.creature.body.BodyPart;
import org.nusco.swimmers.shared.physics.Vector;

class NarjilloView extends ThingView {
	
	private final Group group = new Group();
	private final List<OrganView> organViews;
	private final MouthView mouthView;
	private final CircularObjectView eyeView;

	public NarjilloView(Narjillo swimmer) {
		super(swimmer);
		organViews = createOrganViews();
		mouthView = new MouthView(swimmer);
		eyeView = new EyeView(swimmer);
	}

	@Override
	public Node toNode() {
		group.getChildren().clear();
		
		Node mouthNode = mouthView.toNode();
		if (mouthNode != null)
			group.getChildren().add(mouthNode);

		group.getChildren().addAll(getOrganNodes());

		Node eyeNode = eyeView.toNode();
		if (eyeNode != null)
			group.getChildren().add(eyeNode);

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

	private Narjillo getSwimmer() {
		return (Narjillo)getThing();
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		for (OrganView organView : organViews)
			if (organView.isVisible(viewport))
				return true;
		return mouthView.isVisible(viewport) || eyeView.isVisible(viewport);
	}
}
