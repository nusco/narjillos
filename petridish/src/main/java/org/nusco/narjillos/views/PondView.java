package org.nusco.narjillos.views;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.pond.PondEventListener;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;

public class PondView {

	private final Pond pond;
	private final Viewport viewport;
	private final Node background;
	private final Map<Thing, ThingView> thingsToViews = new HashMap<>();

	public PondView(Pond pond) {
		this.pond = pond;
		viewport = new Viewport(pond);
		background = createBackground(pond);

		for (Thing thing : pond.getThings())
			addThingView(thing);

		pond.addEventListener(new PondEventListener() {
			@Override
			public void thingAdded(Thing thing) {
				addThingView(thing);
			}

			@Override
			public void thingRemoved(Thing thing) {
				removeThingView(thing);
			}
		});
	}

	public Viewport getViewport() {
		return viewport;
	}

	public Node toNode() {
		Group result = new Group();
		result.getChildren().add(getBackground());
		result.getChildren().add(getThingsGroup());
		result.getChildren().add(createForeground(pond));
		return result;
	}

	private Group getThingsGroup() {
		Group things = new Group();
		things.getChildren().addAll(getNodesForThings());

		things.getTransforms().add(new Translate(-viewport.getPositionPC().x, -viewport.getPositionPC().y));
		things.getTransforms().add(new Scale(viewport.getZoomLevel(), viewport.getZoomLevel(),
											viewport.getPositionPC().x, viewport.getPositionPC().y));

		setZoomLevelEffects(things);
		return things;
	}

	private void setZoomLevelEffects(Group group) {
		double zoomLevel = viewport.getZoomLevel();
		
		if (zoomLevel <= 1)
			return;
		
		group.setEffect(getBlurEffect(zoomLevel));
	}

	private Node getBackground() {
		return background;
	}
	
	private List<Node> getNodesForThings() {
		List<Node> result = new LinkedList<>();
		for (ThingView view : getThingViews()) {
			Node node = view.toNode(viewport);
			if (node != null)
				result.add(node);
		}
		return result;
	}

	private Node createBackground(Pond pond) {
		Rectangle result = new Rectangle(0, 0, pond.getSize(), pond.getSize());
		result.setFill(Color.ANTIQUEWHITE);
		return result;
	}

	private Node createForeground(Pond pond) {
		Vector sizeSC = viewport.getSizeSC();
		double minScreenSize = Math.min(sizeSC.x, sizeSC.y);
		double maxScreenSize = Math.max(sizeSC.x, sizeSC.y);
		Rectangle black = new Rectangle(-10, -10, maxScreenSize + 20, maxScreenSize + 20);
		Circle hole = new Circle(sizeSC.x / 2, sizeSC.y / 2, minScreenSize / 2.03);
		Shape microscope = Shape.subtract(black, hole);
		microscope.setEffect(new BoxBlur(5, 5, 1));
		return microscope;
	}

	private BoxBlur getBlurEffect(double zoomLevel) {
		int blurAmount = (int)(15 * (zoomLevel - 1));
		return new BoxBlur(blurAmount, blurAmount, 3);
	}

	private synchronized Collection<ThingView> getThingViews() {
		return new HashSet<ThingView>(thingsToViews.values());
	}

	private synchronized ThingView addThingView(Thing thing) {
		return thingsToViews.put(thing, ThingView.createViewFor(thing));
	}

	private synchronized void removeThingView(Thing thing) {
		thingsToViews.remove(thing);
	}

	public void tick() {
		viewport.tick();
	}

	public Pond getPond() {
		return pond;
	}
}
