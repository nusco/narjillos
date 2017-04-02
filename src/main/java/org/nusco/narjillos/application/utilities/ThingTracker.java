package org.nusco.narjillos.application.utilities;

import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.FoodPellet;

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

		if (target.getLabel().equals(Narjillo.LABEL)) {
			Narjillo narjillo = (Narjillo) target;
			if (narjillo.isDead()) {
				if (isDemoMode())
					startTrackingRandomLivingThing();
				else {
					Thing nextClosestNarjillo = locator.findNarjilloAt(narjillo.getPosition());
					startTracking(nextClosestNarjillo);
				}
			}
		}

		if (target.getLabel().equals(Egg.LABEL) || target.getLabel().equals(FoodPellet.LABEL)) {
			startTracking(target.getInteractor());
		}

		viewport.centerOn(target);
	}

	public synchronized void stopTracking() {
		target = null;
		demoMode = false;
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
		return "freeroaming";
	}

	private synchronized boolean isFollowing() {
		return target != null;
	}

	private boolean hasBeenDemoTrackingFor(long seconds) {
		long secondsSinceLastDemoTracking = (System.currentTimeMillis() - lastDemoTrackingTime) / 1000;
		return secondsSinceLastDemoTracking > seconds;
	}

	public void startTrackingThingAt(Vector position) {
		startTracking(locator.findThingAt(position));
	}

	public void startTracking(Thing target) {
		if (target == null || target == Thing.NULL) {
			stopTracking();
			return;
		}

		this.target = target;
		viewport.centerAndZoomOn(target);
	}

	private void startTrackingRandomLivingThing() {
		Thing target = locator.findRandomLivingThing();
		startTracking(target);
		lastDemoTrackingTime = System.currentTimeMillis();
	}

	private boolean isDemoMode() {
		return demoMode;
	}
}
