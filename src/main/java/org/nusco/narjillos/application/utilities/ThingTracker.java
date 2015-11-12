package org.nusco.narjillos.application.utilities;

import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.FoodPellet;
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
	private long lastDemoTrackingTime;

	public ThingTracker(Viewport viewport, Locator locator) {
		this.viewport = viewport;
		this.locator = locator;
	}

	public synchronized void tick() {
		if (!isFollowing())
			return;

		if (isDemoMode()) {
			if (hasBeenDemoTrackingFor(DEMO_MODE_FOCUS_TIME_IN_SECONDS + DEMO_MODE_ZOOM_TIME_IN_SECONDS))
				startTrackingRandomLivingThing();
			else if (hasBeenDemoTrackingFor(DEMO_MODE_FOCUS_TIME_IN_SECONDS))
				viewport.zoomOut();
		}
		
		if (target.getLabel().equals("narjillo")) {
			Narjillo narjillo = (Narjillo) target;
			if (narjillo.isDead()) {
				if (isDemoMode())
					startTrackingRandomLivingThing();
				else {
					Thing nextClosestNarjillo = locator.findNarjilloAt(narjillo.getPosition());
					if (nextClosestNarjillo == null) {
						stopTracking();
						return;
					} else
						startTracking(nextClosestNarjillo);
				}
			}
		}
		
		if (target.getLabel().equals("egg")) {
			Narjillo hatched = ((Egg) target).getHatchedNarjillo();
			if (hatched != null)
				startTracking(hatched);
			return;
		}
		
		if (target.getLabel().equals("food_pellet")) {
			Thing eater = ((FoodPellet) target).getEater();
			if (eater != null)
				startTracking(eater);
			return;
		}
		
		viewport.centerOn(target);
	}

	public synchronized void stopTracking() {
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
		viewport.zoomToMaxLevel();
		startTrackingRandomLivingThing();
	}

	public String getStatus() {
		if (isDemoMode())
			return "demo";
		if (isFollowing())
			return "following";
		return "freeroam";
	}

	private boolean hasBeenDemoTrackingFor(long seconds) {
		long secondsSinceLastDemoTracking = (System.currentTimeMillis() - lastDemoTrackingTime) / 1000;
		return secondsSinceLastDemoTracking > seconds;
	}

	public void startTrackingThingAt(Vector position) {
		Thing target = locator.findThingAt(position);

		if (target == null) {
			stopTracking();
			return;
		}

		startTracking(target);
	}

	public void startTracking(Thing target) {
		this.target = target;
		viewport.centerAndZoomOn(target);
	}

	private void startTrackingRandomLivingThing() {
		Thing target = locator.findRandomLivingThing();
		if (target == null) {
			stopTracking();
			return;
		}
		startTracking(target);
		lastDemoTrackingTime = System.currentTimeMillis();
	}

	private boolean isDemoMode() {
		return demoMode;
	}
}
