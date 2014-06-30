package org.nusco.swimmers.graphics;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class PondView extends ThingView {

	private static final int ZOOM_INCREMENT = 100;
	private int viewSize = 800;
	private final Pond pond;

	public PondView(Pond pond) {
		this.pond = pond;
	}

	public int getViewSize() {
		return viewSize;
	}

	private double getScale() {
		return (double) getViewSize() / Pond.USEFUL_AREA_SIZE;
	}

	public void add(Swimmer swimmer, long x, long y) {
		pond.add(swimmer, x, y);
	}

	public Node toNode() {
		Group result = new Group();
		result.getChildren().addAll(getNodesForThings());
		result.getTransforms().add(new Scale(getScale(), getScale()));
		return result;
	}

	private List<Node> getNodesForThings() {
		List<Node> result = new LinkedList<>();
		for (Thing thing : pond.getThings()) {
			ThingView view = ThingView.createViewFor(thing);
			result.add(view.toNode());
		}
		return result;
	}

	public void tick() {
		pond.tick();
	}

	public Pond getPond() {
		return pond;
	}

	public void zoomIn(Vector center) {
		viewSize += ZOOM_INCREMENT;
	}

	public void zoomOut() {
		viewSize -= ZOOM_INCREMENT;
	}

	public void show(Group root) {
		root.getChildren().clear();
		root.getChildren().add(toNode());
	}
}
