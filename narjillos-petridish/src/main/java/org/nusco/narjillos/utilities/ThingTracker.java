package org.nusco.narjillos.utilities;

import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;

public class ThingTracker {

	private final Viewport viewport;
	private final Locator locator;
	private Thing tracked;

	public ThingTracker(Viewport viewport, Locator locator) {
		this.viewport = viewport;
		this.locator = locator;
	}

	public synchronized void track() {
		if (!isTracking())
			return;
		
		if (tracked.getLabel().equals("narjillo")) {
			Narjillo narjillo = (Narjillo) tracked;
			if (narjillo.isDead()) {
				Narjillo nextClosestNarjillo = locator.findNarjilloNear(narjillo.getPosition(), Double.MAX_VALUE);
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

	public synchronized void focusAt(Vector position) {
		if (isTracking()) {
			startTrackingAt(position);
			return;
		}
		
		Thing thing = locator.findThingNear(position);

		if (thing == null)
			stopTracking();

		viewport.flyToTargetEC(position);
	}

	public synchronized void startTrackingAt(Vector position) {
		Thing thing = locator.findThingNear(position);

		if (thing == null) {
			stopTracking();
			viewport.flyToTargetEC(position);
			viewport.flyToNextZoomCloseupLevel();
			return;
		}

		startTracking(thing);
		viewport.flyToMaxZoomCloseupLevel();
	}
	
	public synchronized boolean isTracking() {
		return tracked != null;
	}

	public synchronized void stopTracking() {
		tracked = null;
	}

	private void startTracking(Thing thing) {
		tracked = thing;
	}

	private void centerViewportOn(Thing thing) {
		if (thing.getLabel().equals("narjillo"))
			viewport.flyToTargetEC(((Narjillo) thing).calculateCenterOfMass());
		else
			viewport.flyToTargetEC(thing.getPosition());
	}
}
