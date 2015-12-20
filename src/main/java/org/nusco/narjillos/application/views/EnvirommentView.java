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
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import org.nusco.narjillos.application.utilities.AppState;
import org.nusco.narjillos.application.utilities.Effects;
import org.nusco.narjillos.application.utilities.Light;
import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.VisualDebugger;
import org.nusco.narjillos.experiment.environment.Environment;
import org.nusco.narjillos.experiment.environment.EnvironmentEventListener;

public class EnvirommentView {
	
	private final Viewport viewport;
	private final Map<Thing, ThingView> thingsToViews = new LinkedHashMap<>();
	private final AppState viewState;
	private final BackgroundView backgroundView;

	public EnvirommentView(Environment environment, Viewport viewport, AppState state) {
		this.viewport = viewport;
		this.viewState = state;

		long size = environment.getSize();
		
		backgroundView = new BackgroundView(viewport, size);

		for (Thing thing : environment.getThings(""))
			addThingView(thing);

		environment.addEventListener(new EnvironmentEventListener() {
			@Override
			public synchronized void added(Thing thing) {
				addThingView(thing);
			}

			@Override
			public synchronized void removed(Thing thing) {
				removeThingView(thing);
			}
		});
	}

	public Node toNode() {
		boolean isLightOn = viewState.getLight() != Light.OFF;
		boolean isInfrared = viewState.getLight() == Light.INFRARED;
		boolean areEffectsOn = viewState.getEffects() == Effects.ON;

		Group result = new Group();

		Node speckles = backgroundView.toNode(viewState.getLight());
		result.getChildren().add(speckles);

		if (!isLightOn)
			return result;
		
		result.getChildren().add(getThingsGroup(isInfrared, areEffectsOn));

		if (areEffectsOn)
			setZoomLevelEffects(result);
		
		return result;
	}

	private Group getThingsGroup(boolean infraredOn, boolean effectsOn) {
		Group result = new Group();
		result.getChildren().addAll(getNodesForThingsInOrder(infraredOn, effectsOn));

		if (VisualDebugger.DEBUG)
			result.getChildren().add(getVisualDebuggingSegments());

		ViewportTransformer.applyTransforms(result, viewport);

		return result;
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
		addNodesFor("food_pellet", result, infraredOn, effectsOn);
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
}
