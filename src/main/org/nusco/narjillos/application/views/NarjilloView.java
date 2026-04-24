package org.nusco.narjillos.application.views;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;

import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.utilities.VisualDebugger;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.ConnectedOrgan;

class NarjilloView extends ThingView {

	private final Group group = new Group();

	private final List<OrganView> organs;

	private final MouthView mouth;

	private final ItemView eyes;

	private final CenterOfMassView centerOfMass;

	public NarjilloView(Narjillo narjillo) {
		super(narjillo);
		organs = createOrganViews();
		mouth = new MouthView(narjillo);
		eyes = new EyesView(narjillo);
		centerOfMass = new CenterOfMassView(narjillo);
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		group.getChildren().clear();

		group.getChildren().addAll(getOrganNodes(zoomLevel, infraredOn, effectsOn));

		Node mouthNode = mouth.toNode(zoomLevel, infraredOn, effectsOn);
		if (mouthNode != null)
			group.getChildren().add(mouthNode);

		Node eyeNode = eyes.toNode(zoomLevel, infraredOn, effectsOn);
		if (eyeNode != null)
			group.getChildren().add(eyeNode);

		if (VisualDebugger.DEBUG)
			group.getChildren().add(centerOfMass.toNode(zoomLevel, infraredOn, effectsOn));

		if (effectsOn && !group.getChildren().isEmpty())
			group.setEffect(getEffects(zoomLevel, infraredOn));

		return group;
	}

	private List<Node> getOrganNodes(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		List<Node> result = new LinkedList<>();
		for (OrganView view : organs) {
			Node node = view.toNode(zoomLevel, infraredOn, effectsOn);
			if (node != null)
				result.add(node);
		}
		return result;
	}

	private List<OrganView> createOrganViews() {
		List<OrganView> result = new LinkedList<>();
		for (ConnectedOrgan bodyPart : getNarjillo().getOrgans())
			result.add(new OrganView(bodyPart, getNarjillo()));
		return result;
	}

	private Narjillo getNarjillo() {
		return (Narjillo) getThing();
	}

	@Override
	public boolean isVisible(Viewport viewport) {
		for (OrganView organView : organs)
			if (organView.isVisible(viewport))
				return true;
		OrganView organView = organs.get(0);
		organView.isVisible(viewport);
		// ignore the mouth and eyes, too small to make a visible difference
		return false;
	}
}
