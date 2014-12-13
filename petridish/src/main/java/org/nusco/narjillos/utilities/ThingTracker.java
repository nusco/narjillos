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
		
		if (tracked.getLabel().equals("narjillo") && ((Narjillo) tracked).isDead()) {
			stopTracking();
			return;
		}
		
		if (tracked.getLabel().equals("egg")) {
			Narjillo hatchedNarjillo = ((Egg) tracked).getHatchedNarjillo();
			if (hatchedNarjillo != null)
				startTracking(hatchedNarjillo);
			return;
		}
		
		if (tracked.getLabel().equals("food_piece") && ((FoodPiece) tracked).getEnergy().isDepleted()) {
			stopTracking();
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
		else
			startTracking(thing);

		viewport.flyToTargetEC(position);
	}

	public synchronized void startTrackingAt(Vector position) {
		Thing thing = locator.findThingNear(position);

		if (thing == null) {
			stopTracking();
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
