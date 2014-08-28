package org.nusco.narjillos.views;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.shared.utilities.VisualDebugger;
import org.nusco.narjillos.views.utilities.Viewport;

class NarjilloView extends ThingView {
	
	private final Group group = new Group();
	private final List<BodyPartView> organViews;
	private final MouthView mouthView;
	private final RoundObjectView eyeView;
	private final CenterOfMassView centerOfMassView;

	public NarjilloView(Narjillo narjillo) {
		super(narjillo);
		organViews = createOrganViews();
		mouthView = new MouthView(narjillo);
		eyeView = new EyeView(narjillo);
		centerOfMassView = new CenterOfMassView(narjillo);
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn) {
		group.getChildren().clear();

		group.getChildren().addAll(getOrganNodes(zoomLevel, infraredOn));
		
		Node mouthNode = mouthView.toNode(zoomLevel, infraredOn);
		if (mouthNode != null)
			group.getChildren().add(mouthNode);

		Node eyeNode = eyeView.toNode(zoomLevel, infraredOn);
		if (eyeNode != null)
			group.getChildren().add(eyeNode);

		if (VisualDebugger.DEBUG)
			group.getChildren().add(centerOfMassView.toNode(zoomLevel, infraredOn));
		
		group.setEffect(getEffects(zoomLevel, infraredOn));
		return group;
	}

	private List<Node> getOrganNodes(double zoomLevel, boolean infraredOn) {
		List<Node> result = new LinkedList<>();
		for (BodyPartView view : organViews) {
			Node node = view.toNode(zoomLevel, infraredOn);
			if (node != null)
				result.add(node);
		}
		return result;
	}

	private List<BodyPartView> createOrganViews() {
		List<BodyPartView> result = new LinkedList<>();
		for (BodyPart bodyPart : getNarjillo().getBodyParts())
			result.add(new BodyPartView(bodyPart, getNarjillo()));
		return result;
	}

	private Narjillo getNarjillo() {
		return (Narjillo)getThing();
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		for (BodyPartView organView : organViews)
			if (organView.isVisible(viewport))
				return true;
		BodyPartView organView = organViews.get(0);
		organView.isVisible(viewport);
		// ignore the mouth and eye, too small to make a visible difference
		return false;
	}
}
