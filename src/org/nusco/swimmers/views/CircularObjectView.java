package org.nusco.swimmers.views;

import org.nusco.swimmers.shared.things.Thing;

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