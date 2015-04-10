package org.nusco.narjillos.application.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.application.utilities.AppState;
import org.nusco.narjillos.application.utilities.Effects;
import org.nusco.narjillos.application.utilities.Light;
import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.physics.Segment;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.VisualDebugger;
import org.nusco.narjillos.ecosystem.Culture;
import org.nusco.narjillos.ecosystem.EnvironmentEventListener;

public class EnvirommentView {

	static final Color BACKGROUND_COLOR = Color.ANTIQUEWHITE;
	static final Paint INFRARED_BACKGROUND_COLOR = Color.DARKGRAY.darker();
	
	private final Viewport viewport;
	private final Map<Thing, ThingView> thingsToViews = new LinkedHashMap<>();
	private final AppState viewState;
	private final SpecklesView specklesView;
	private final Shape emptySpace;
	private final Shape infraredEmptySpace;
	private final Shape darkness;

	public EnvirommentView(Culture environment, Viewport viewport, AppState state) {
		this.viewport = viewport;
		this.viewState = state;

		long size = environment.getSize();
		
		emptySpace = new Rectangle(0, 0, size, size);
		emptySpace.setFill(EnvirommentView.BACKGROUND_COLOR);
		
		infraredEmptySpace = new Rectangle(0, 0, size, size);
		infraredEmptySpace.setFill(EnvirommentView.INFRARED_BACKGROUND_COLOR);
		
		darkness = new Rectangle(0, 0, size, size);

		specklesView = new SpecklesView(viewport, size);

		for (Thing thing : environment.getThings(""))
			addThingView(thing);

		environment.addEventListener(new EnvironmentEventListener() {
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

	public Node toNode() {
		if (viewState.getLight() == Light.OFF)
			return darkness;

		boolean isInfrared = viewState.getLight() == Light.INFRARED;
		boolean effectsOn = viewState.getEffects() == Effects.ON;

		Group result = new Group();

		Node backgroundFill = isInfrared ? infraredEmptySpace : emptySpace;
		darkenWithDistance(backgroundFill, viewport.getZoomLevel());
		result.getChildren().add(backgroundFill);

		Node speckles = specklesView.toNode(isInfrared);
		if (speckles != null) {
			darkenWithDistance(speckles, viewport.getZoomLevel());
			result.getChildren().add(speckles);
		}

		result.getChildren().add(getThingsGroup(isInfrared, effectsOn));

		if (effectsOn)
			setZoomLevelEffects(result);
		
		return result;
	}

	private Group getThingsGroup(boolean infraredOn, boolean effectsOn) {
		Group things = new Group();
		things.getChildren().addAll(getNodesForThingsInOrder(infraredOn, effectsOn));

		if (VisualDebugger.DEBUG)
			things.getChildren().add(getVisualDebuggingSegments());

		things.getTransforms().add(new Translate(-viewport.getPositionEC().x, -viewport.getPositionEC().y));
		things.getTransforms().add(
				new Scale(viewport.getZoomLevel(), viewport.getZoomLevel(), viewport.getPositionEC().x, viewport.getPositionEC().y));

		return things;
	}

	private Group getVisualDebuggingSegments() {
		Group result = new Group();
		List<Segment> segments = VisualDebugger.getSegments();

		if (segments.isEmpty())
			return result;

		for (Segment segment : segments) {
			Line line = new Line(segment.getStartPoint().x, segment.getStartPoint().y, segment.getEndPoint().x, segment.getEndPoint().y);
			line.setStrokeWidth(2);
			line.setStroke(Color.RED);
			result.getChildren().add(line);
		}

		return result;
	}

	private void setZoomLevelEffects(Group group) {
		double zoomLevel = viewport.getZoomLevel();

		final int EXTREME_MAGNIFICATION = 1;
		if (zoomLevel <= EXTREME_MAGNIFICATION)
			return;

		group.setEffect(getBlurEffect(zoomLevel));
	}

	private List<Node> getNodesForThingsInOrder(boolean infraredOn, boolean effectsOn) {
		List<Node> result = new LinkedList<>();
		addNodesFor("food_piece", result, infraredOn, effectsOn);
		addNodesFor("narjillo", result, infraredOn, effectsOn);
		addNodesFor("egg", result, infraredOn, effectsOn);
		return result;
	}

	private void addNodesFor(String thingLabel, List<Node> result, boolean infraredOn, boolean effectsOn) {
		for (ThingView view : getThingViews()) {
			if (view.getThing().getLabel().equals(thingLabel)) {
				Node node = view.toNode(viewport, infraredOn, effectsOn);
				if (node != null)
					result.add(node);
			}
		}
	}

	private Effect getBlurEffect(double zoomLevel) {
		int blurAmount = Math.min((int) (15 * (zoomLevel - 0.7)), 10);
		return new BoxBlur(blurAmount, blurAmount, 1);
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

	void darkenWithDistance(Node node, double zoomLevel) {
		double brightnessAdjust = -zoomLevel / 5;
		node.setEffect(new ColorAdjust(0, 0, brightnessAdjust, 0));
	}
}
