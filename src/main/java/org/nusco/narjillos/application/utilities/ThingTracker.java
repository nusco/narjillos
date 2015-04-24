package org.nusco.narjillos.application.utilities;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.FoodPiece;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;

public class ThingTracker {

	private static final long DEMO_MODE_FOCUS_TIME_IN_SECONDS = 12;
	private static final long DEMO_MODE_ZOOM_TIME_IN_SECONDS = 3;

	private final Viewport viewport;
	private final Locator locator;
	private Thing target;
	private boolean demoMode;
	private long lastRefocusingTimeInDemoMode;

	public ThingTracker(Viewport viewport, Locator locator) {
		this.viewport = viewport;
		this.locator = locator;
	}

	public synchronized void tick() {
		if (!isFollowing())
			return;

		if (isDemoMode()) {
			if (hasBeenFocusingFor(DEMO_MODE_FOCUS_TIME_IN_SECONDS + DEMO_MODE_ZOOM_TIME_IN_SECONDS))
				refocusOnRandomLivingThing();
			else if (hasBeenFocusingFor(DEMO_MODE_FOCUS_TIME_IN_SECONDS))
				viewport.zoomOut();
		}
		
		if (target.getLabel().equals("narjillo")) {
			Narjillo narjillo = (Narjillo) target;
			if (narjillo.isDead()) {
				if (isDemoMode())
					refocusOnRandomLivingThing();
				else {
					Thing nextClosestNarjillo = locator.findNarjilloAt(narjillo.getPosition());
					if (nextClosestNarjillo == null) {
						stopFollowing();
						return;
					} else
						startFollowing(nextClosestNarjillo);
				}
			}
		}
		
		if (target.getLabel().equals("egg")) {
			Narjillo hatched = ((Egg) target).getHatchedNarjillo();
			if (hatched != null)
				startFollowing(hatched);
			return;
		}
		
		if (target.getLabel().equals("food_piece")) {
			Thing eater = ((FoodPiece) target).getEater();
			if (eater != null)
				startFollowing(eater);
			return;
		}
		
		centerViewportOn(target);
	}

	public synchronized void startFollowing(Vector position) {
		startFollowingThingAt(position);
		
		if (!isFollowing()) {
			viewport.flyToTargetEC(position);
			viewport.flyToNextZoomCloseupLevel();
		}
	}

	public synchronized void stopFollowing() {
		target = null;
		demoMode = false;
	}

	public synchronized boolean isFollowing() {
		return target != null;
	}

	public void toggleDemoMode() {
		demoMode = !demoMode;
		if (!isDemoMode())
			return;
		viewport.flyToMaxZoomCloseupLevel();
		refocusOnRandomLivingThing();
	}

	public String getStatus() {
		if (isDemoMode())
			return "demo";
		if (isFollowing())
			return "following";
		return "freeroam";
	}

	private boolean hasBeenFocusingFor(long seconds) {
		long secondsSinceLastRefocus = (System.currentTimeMillis() - lastRefocusingTimeInDemoMode) / 1000;
		return secondsSinceLastRefocus > seconds;
	}

	private void refocusOnRandomLivingThing() {
		Thing target = locator.findRandomLivingThing();
		if (target == null) {
			stopFollowing();
			return;
		}
		startFollowing(target);
		lastRefocusingTimeInDemoMode = System.currentTimeMillis();
	}

	private void startFollowingThingAt(Vector position) {
		Thing target = locator.findThingAt(position);

		if (target == null) {
			stopFollowing();
			return;
		}

		startFollowing(target);
	}

	private void startFollowing(Thing target) {
		this.target = target;
		viewport.flyToTargetEC(target.getPosition());
		viewport.flyToMaxZoomCloseupLevel();
	}

	private void centerViewportOn(Thing thing) {
		if (thing.getLabel().equals("narjillo"))
			viewport.flyToTargetEC(((Narjillo) thing).getCenterOfMass());
		else
			viewport.flyToTargetEC(thing.getPosition());
	}

	private boolean isDemoMode() {
		return demoMode;
	}
}
