package org.nusco.narjillos.views;

import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.views.utilities.Viewport;

public abstract class RoundObjectView extends ThingView {

	private final int radius;

	public RoundObjectView(Thing thing, int radius) {
		super(thing);
		this.radius = radius;
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		return viewport.isVisible(getThing().getPosition(), getRadius());
	}

	protected final double getRadius() {
		return radius;
	}
}