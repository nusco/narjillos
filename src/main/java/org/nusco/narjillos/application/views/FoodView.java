package org.nusco.narjillos.application.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.things.FoodPellet;
import org.nusco.narjillos.core.utilities.Configuration;

class FoodView extends ThingView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.035;

	private final RoundObjectView roundObjectView;

	public FoodView(FoodPellet food) {
		super(food);

		roundObjectView = new RoundObjectView(Configuration.FOOD_RADIUS) {

			@Override
			public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
				if (zoomLevel < MINIMUM_ZOOM_LEVEL)
					return null;

				getShape().setFill(getColor(infraredOn));

				if (infraredOn) {
					getShape().setStroke(Color.WHITE);
					getShape().setStrokeWidth(3);
				} else {
					getShape().setStrokeWidth(0);
				}

				if (effectsOn)
					getShape().setEffect(getEffects(zoomLevel, infraredOn));

				return getShape();
			}

			private Color getColor(boolean infraredOn) {
				if (infraredOn)
					return Color.RED;
				return Color.BROWN;
			}
		};
		roundObjectView.moveTo(food.getPosition());
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		return roundObjectView.toNode(zoomLevel, infraredOn, effectsOn);
	}

	@Override
	public boolean isVisible(Viewport viewport) {
		return roundObjectView.isVisible(viewport);
	}
}