package org.nusco.swimmers.views;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.things.Thing;

public class PondView extends ThingView {

	private final Pond pond;
	private final Viewport viewport;
	private final List<ThingView> thingViews;
	private final Node background;

	public PondView(Pond pond) {
		this.pond = pond;
		viewport = new Viewport(pond);
		// TODO: this will have to get more dynamic once the set of Things
		// can change because swimmers eat, die, etc.
		thingViews = createThingViews(pond);
		background = createBackground(pond);
	}

	public Viewport getViewport() {
		return viewport;
	}

	public Node toNode() {
		Group group = new Group();
		group.getChildren().add(getBackground());
		group.getChildren().addAll(getNodesForThings());

		group.getTransforms().add(new Translate(viewport.getSizeX() / 2, viewport.getSizeY() / 2));

		double zoomLevel = viewport.getZoomLevel();
		group.getTransforms().add(new Scale(zoomLevel, zoomLevel));

		double pondSizeAtThisZoomLevel = pond.getSize() * zoomLevel;
		double translation = (-pondSizeAtThisZoomLevel) / 2 / zoomLevel;
		group.getTransforms().add(new Translate(translation, translation));

		setZoomBlurEffect(group);

		return group;
	}

	private void setZoomBlurEffect(Group group) {
		if(viewport.getZoomLevel() <= 1)
			return;
		int blurAmount = (int)(15 * (viewport.getZoomLevel() - 1));
		group.setEffect(new BoxBlur(blurAmount, blurAmount, 3));
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
		viewport.tick();
	}

	public Pond getPond() {
		return pond;
	}

	public void show(Group root) {
		root.getChildren().add(toNode());
	}
}
