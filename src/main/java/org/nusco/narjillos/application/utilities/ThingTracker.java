package org.nusco.narjillos.application.utilities;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.FoodPiece;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;

public class ThingTracker {

	private final Viewport viewport;
	private final Locator locator;
	private Thing tracked;

	public ThingTracker(Viewport viewport, Locator locator) {
		this.viewport = viewport;
		this.locator = locator;
	}

	public synchronized void tick() {
		if (!isTracking())
			return;

		if (tracked.getLabel().equals("narjillo")) {
			Narjillo narjillo = (Narjillo) tracked;
			if (narjillo.isDead()) {
				Thing nextClosestNarjillo = locator.findNarjilloAt(narjillo.getPosition());
				if (nextClosestNarjillo == null) {
					stopTracking();
					return;
				} else
					startTracking(nextClosestNarjillo);
			}
		}
		
		if (tracked.getLabel().equals("egg")) {
			Narjillo hatched = ((Egg) tracked).getHatchedNarjillo();
			if (hatched != null)
				startTracking(hatched);
			return;
		}
		
		if (tracked.getLabel().equals("food_piece")) {
			Thing eater = ((FoodPiece) tracked).getEater();
			if (eater != null)
				startTracking(eater);
			return;
		}
		
		centerViewportOn(tracked);
	}

	public synchronized void startTracking(Vector position) {
		startTrackingThingAt(position);
		
		if (!isTracking()) {
			viewport.flyToTargetEC(position);
			viewport.flyToNextZoomCloseupLevel();
		}
	}

	public synchronized void stopTracking() {
		tracked = null;
	}

	public synchronized boolean isTracking() {
		return tracked != null;
	}

	private void startTrackingThingAt(Vector position) {
		Thing thing = locator.findThingAt(position);

		if (thing == null) {
			stopTracking();
			return;
		}

		startTracking(thing);
		viewport.flyToTargetEC(thing.getPosition());
		viewport.flyToMaxZoomCloseupLevel();
	}

	private void startTracking(Thing thing) {
		tracked = thing;
	}

	private void centerViewportOn(Thing thing) {
		if (thing.getLabel().equals("narjillo"))
			viewport.flyToTargetEC(((Narjillo) thing).getCenterOfMass());
		else
			viewport.flyToTargetEC(thing.getPosition());
	}
}
