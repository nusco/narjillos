package org.nusco.swimmers.views;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class PondView extends ThingView {

	private static final int ZOOM_INCREMENT = 20;
	
	private int viewSize = 800;
	private final Pond pond;

	private final List<ThingView> thingViews;

	public PondView(Pond pond) {
		this.pond = pond;
		// TODO: this will have to get more dynamic once the set of Things
		// can change because swimmers eat, die, etc.
		thingViews = createThingViews(pond);
	}

	public int getViewSize() {
		return viewSize;
	}

	private double getScale() {
		return (double) getViewSize() / Pond.USEFUL_AREA_SIZE;
	}

	public Node toNode() {
		Group group = new Group();
		group.getChildren().addAll(getNodesForThings());
		group.getTransforms().add(new Scale(getScale(), getScale()));
		return group;
	}
	
	private List<Node> getNodesForThings() {
		List<Node> result = new LinkedList<>();
		for (ThingView view : thingViews)
			result.add(view.toNode());
		return result;
	}

	private List<ThingView> createThingViews(Pond pond) {
		List<ThingView> result = new LinkedList<>();
		for (Thing thing : pond.getThings())
			result.add(ThingView.createViewFor(thing));
		return result;
	}

	public void tick() {
		pond.tick();
	}

	public Pond getPond() {
		return pond;
	}

	public void zoomIn(Vector center) {
		viewSize += viewSize / 100 * ZOOM_INCREMENT;
	}

	public void zoomOut() {
		viewSize -= viewSize / 100 * ZOOM_INCREMENT;
	}

	public void show(Group root) {
		root.getChildren().clear();
		root.getChildren().add(toNode());
	}
}
