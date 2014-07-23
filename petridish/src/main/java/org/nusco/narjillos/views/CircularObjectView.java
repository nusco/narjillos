package org.nusco.narjillos.views;

import org.nusco.narjillos.shared.things.Thing;

public abstract class CircularObjectView extends ThingView {

	public CircularObjectView(Thing thing) {
		super(thing);
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		return viewport.isVisible(getThing().getPosition(), getRadius());
	}

	protected abstract double getRadius();
}