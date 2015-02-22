package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.shared.physics.Angle;
import org.nusco.narjillos.shared.physics.FastMath;
import org.nusco.narjillos.shared.utilities.Configuration;
import org.nusco.narjillos.utilities.Viewport;

class EggView extends ThingView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.02;
	private static final double RADIUS_VARIATION = 1.5;
	private static final double BLOBBING_SPEED = 3;
	
	private final Ellipse shape;
	
	private double waveAngle = Math.random() * 360;
	
	public EggView(Egg egg) {
		super(egg);
		shape = new Ellipse(Configuration.EGG_RADIUS, Configuration.EGG_RADIUS);
	}

	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;

		waveAngle = Angle.normalize(waveAngle + BLOBBING_SPEED);
		shape.setRadiusX(Math.min(getEgg().getAge(), Configuration.EGG_RADIUS + RADIUS_VARIATION * FastMath.sin(waveAngle)));
		shape.setRadiusY(Math.min(getEgg().getAge(), Configuration.EGG_RADIUS + RADIUS_VARIATION * FastMath.cos(waveAngle)));

		shape.setFill(getFillColor(infraredOn));

		if (effectsOn)
			shape.setEffect(getEffects(zoomLevel, infraredOn));

		shape.getTransforms().clear();
		Translate translation = new Translate(getThing().getPosition().x, getThing().getPosition().y);
		shape.getTransforms().add(translation);
		
		return shape;
	}

	private Color getFillColor(boolean infraredOn) {
		Color color = (infraredOn ? Color.RED : Color.BURLYWOOD);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), getAlpha());
	}

	private double getAlpha() {
		return 1 - getEgg().getDecay();
	}

	private Egg getEgg() {
		return (Egg) getThing();
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		return viewport.isVisible(getThing().getPosition(), Configuration.EGG_RADIUS + RADIUS_VARIATION);
	}
}