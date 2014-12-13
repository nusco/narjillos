package org.nusco.narjillos.views;

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

import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.ecosystem.EcosystemEventListener;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.VisualDebugger;
import org.nusco.narjillos.utilities.Light;
import org.nusco.narjillos.utilities.MotionBlur;
import org.nusco.narjillos.utilities.PetriDishState;
import org.nusco.narjillos.utilities.Viewport;

public class EcosystemView {

	static final Color BACKGROUND_COLOR = Color.ANTIQUEWHITE;
	static final Paint INFRARED_BACKGROUND_COLOR = Color.DARKGRAY.darker();
	
	private final Viewport viewport;
	private final Map<Thing, ThingView> thingsToViews = new LinkedHashMap<>();
	private final PetriDishState petriDishState;
	private final SpecklesView specklesView;
	private final Shape emptySpace;
	private final Shape infraredEmptySpace;
	private final Shape darkness;

	public EcosystemView(Ecosystem ecosystem, Viewport viewport, PetriDishState state) {
		this.viewport = viewport;
		this.petriDishState = state;

		long size = ecosystem.getSize();
		
		emptySpace = new Rectangle(0, 0, size, size);
		emptySpace.setFill(EcosystemView.BACKGROUND_COLOR);
		
		infraredEmptySpace = new Rectangle(0, 0, size, size);
		infraredEmptySpace.setFill(EcosystemView.INFRARED_BACKGROUND_COLOR);
		
		darkness = new Rectangle(0, 0, size, size);

		specklesView = new SpecklesView(viewport, size);

		for (Thing thing : ecosystem.getThings(""))
			addThingView(thing);

		ecosystem.addEventListener(new EcosystemEventListener() {
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
		if (petriDishState.getLight() == Light.OFF)
			return darkness;

		boolean isInfrared = petriDishState.getLight() == Light.INFRARED;
		boolean hasMotionBlur = petriDishState.getMotionBlur() == MotionBlur.ON;

		Group result = new Group();

		Node backgroundFill = isInfrared ? infraredEmptySpace : emptySpace;
		darkenWithDistance(backgroundFill, viewport.getZoomLevel());
		result.getChildren().add(backgroundFill);

		Node speckles = specklesView.toNode(isInfrared);
		if (speckles != null) {
			darkenWithDistance(speckles, viewport.getZoomLevel());
			result.getChildren().add(speckles);
		}

		result.getChildren().add(getThingsGroup(isInfrared, hasMotionBlur));

		setZoomLevelEffects(result);
		
		return result;
	}

	private Group getThingsGroup(boolean infraredOn, boolean motionBlurOn) {
		Group things = new Group();
		things.getChildren().addAll(getNodesForThingsInOrder(infraredOn, motionBlurOn));

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

	private List<Node> getNodesForThingsInOrder(boolean infraredOn, boolean motionBlurOn) {
		List<Node> result = new LinkedList<>();
		addNodesFor("food_piece", result, infraredOn, motionBlurOn);
		addNodesFor("narjillo", result, infraredOn, motionBlurOn);
		addNodesFor("egg", result, infraredOn, motionBlurOn);
		return result;
	}

	private void addNodesFor(String thingLabel, List<Node> result, boolean infraredOn, boolean motionBlurOn) {
		for (ThingView view : getThingViews()) {
			if (view.getThing().getLabel().equals(thingLabel)) {
				Node node = view.toNode(viewport, infraredOn, motionBlurOn);
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
