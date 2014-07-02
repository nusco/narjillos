package org.nusco.swimmers.views;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class PondView extends ThingView {

	private static final int DEFAULT_SIZE = 800;
	
	private int viewSize = DEFAULT_SIZE;
	private final Pond pond;

	private final List<ThingView> thingViews;
	private final Node background;

	public PondView(Pond pond) {
		this.pond = pond;
		// TODO: this will have to get more dynamic once the set of Things
		// can change because swimmers eat, die, etc.
		thingViews = createThingViews(pond);
		background = createBackground(pond);
	}
	
	public int getViewSize() {
		return viewSize;
	}

	private double getScale() {
		return (double) getViewSize() / pond.getSize();
	}

	public Node toNode() {
		Group group = new Group();
		group.getChildren().add(getBackground());
		group.getChildren().addAll(getNodesForThings());
		group.getTransforms().add(new Scale(getScale(), getScale()));
		// TODO: at close distance
		//group.setEffect(new BoxBlur(4, 4, 1));
		return group;
	}

	private Node getBackground() {
		return background;
	}
	
	private List<Node> getNodesForThings() {
		List<Node> result = new LinkedList<>();
		for (ThingView view : thingViews)
			result.add(view.toNode());
		return result;
	}

	private Node createBackground(Pond pond) {
		Rectangle result = new Rectangle(0, 0, pond.getSize(), pond.getSize());
		result.setFill(Color.ANTIQUEWHITE);
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

	public void zoomIn(Vector center, double percent) {
		viewSize += viewSize / 100 * percent;
	}

	public void zoomOut(double percent) {
		viewSize -= viewSize / 100 * percent;
	}

	public void zoomToDefault() {
		viewSize = DEFAULT_SIZE;
	}

	public void show(Group root) {
		root.getChildren().add(toNode());
	}
}
