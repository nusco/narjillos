package org.nusco.narjillos.application.utilities;

import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.experiment.environment.Environment;

/**
 * A Viewport is a zoomable, rectangular view over an ecosystem.
 * <p>
 * It uses two systems of coordinates: SC are "screen coordinates" - the
 * position in the application window. EC are "ecosystem coordinates" - the
 * position in the ecosystem.
 */
public class Viewport {

	static final double MAX_INITIAL_SIZE_SC = 800;

	static final double ZOOM_MAX = 2;

	private static final double ZOOM_OVERZOOMING_LEVEL = 1;

	static final double[] ZOOM_CLOSEUP_LEVELS = new double[] { 0.15, 0.6 };

	private static final double ZOOM_VELOCITY = 1.03;

	private final double environmentSizeEC;

	private Vector sizeSC;

	private Vector centerEC;

	private double zoomLevel;

	private Vector targetCenterEC;

	private double targetZoomLevel;

	private volatile boolean userIsZooming = false;

	final double minZoomLevel;

	public Viewport(Environment environment) {
		this.environmentSizeEC = environment.getSize();
		setCenterEC(getEcosystemCenterEC());

		double size = Math.min(environment.getSize(), MAX_INITIAL_SIZE_SC);
		sizeSC = Vector.cartesian(size, size);
		minZoomLevel = Math.max(getSizeSC().x, getSizeSC().y) / environmentSizeEC;

		centerOnEcosystem();
		zoomLevel = minZoomLevel;
		zoomToNextLevel();
	}

	public Vector getSizeSC() {
		return sizeSC;
	}

	public void setSizeSC(Vector sizeSC) {
		this.sizeSC = sizeSC;
	}

	public Vector getPositionEC() {
		Vector offset = Vector.cartesian(toLengthEC(sizeSC.x), toLengthEC(sizeSC.y)).by(0.5);
		return centerEC.minus(offset);
	}

	public Vector getCenterEC() {
		return centerEC;
	}

	public void moveBy(Vector velocitySC) {
		targetCenterEC = centerEC.plus(Vector.cartesian(toLengthEC(velocitySC.x), toLengthEC(velocitySC.y)));
	}

	public void translateBy(Vector velocitySC) {
		centerEC = centerEC.plus(Vector.cartesian(toLengthEC(velocitySC.x), toLengthEC(velocitySC.y)));
		targetCenterEC = centerEC;
	}

	public double getZoomLevel() {
		return zoomLevel;
	}

	public void zoomIn() {
		userIsZooming = true;
		zoomTo(zoomLevel * ZOOM_VELOCITY);
	}

	public void zoomOut() {
		userIsZooming = true;
		zoomTo(zoomLevel / ZOOM_VELOCITY);
	}

	public void zoomTo(double zoomLevel) {
		this.zoomLevel = Math.min(Math.max(zoomLevel, minZoomLevel), ZOOM_MAX);
		this.targetZoomLevel = this.zoomLevel;
		if (isZoomedOutCompletely())
			targetCenterEC = getEcosystemCenterEC();
	}

	public void tick() {
		if (!userIsZooming) {
			correctOverzooming();
			zoomToTarget();
		}
		panToTarget();
		userIsZooming = false;
	}

	public boolean isVisible(Vector pointEC, double marginEC) {
		Vector distance = centerEC.minus(pointEC);
		double maxAxialDistancePC = Math.max(Math.abs(distance.x), Math.abs(distance.y));
		double maxVisibleAxialDistance = Math.max(toLengthEC(sizeSC.x), toLengthEC(sizeSC.y)) / 2;
		return (maxAxialDistancePC <= maxVisibleAxialDistance + marginEC);
	}

	public final Vector toEC(Vector pointSC) {
		return getPositionEC().plus(pointSC.by(1.0 / zoomLevel));
	}

	public void centerOn(Thing target) {
		targetCenterEC = target.getCenter();
	}

	public void centerAndZoomOn(Thing target) {
		centerOn(target);
		targetZoomLevel = Math.min(getMaxZoomLevel(), getZoomToFitLevel(target.getRadius()));
	}

	public void zoomToNextLevel() {
		targetZoomLevel = nextZoomCloseupLevel();
	}

	public void zoomToMaxLevel() {
		targetZoomLevel = getMaxZoomLevel();
	}

	public boolean isZoomedOutCompletely() {
		return zoomLevel <= minZoomLevel;
	}

	public boolean isZoomCloseToTarget() {
		return Math.abs(zoomLevel - targetZoomLevel) < 0.1;
	}

	void setCenterEC(Vector centerEC) {
		this.centerEC = centerEC;
		this.targetCenterEC = centerEC;
	}

	void setCenterSC(Vector centerSC) {
		centerEC = toEC(centerSC);
	}

	private Vector getEcosystemCenterEC() {
		return Vector.cartesian(environmentSizeEC / 2, environmentSizeEC / 2);
	}

	private void centerOnEcosystem() {
		centerEC = getEcosystemCenterEC();
	}

	private double getZoomToFitLevel(double radius) {
		double diameter = radius * 2;
		final double margin = 2;
		return getMinSizeSC() / (diameter * margin);
	}

	private double getMinSizeSC() {
		return Math.min(getSizeSC().x, getSizeSC().x);
	}

	private double toLengthEC(double lengthSC) {
		return lengthSC / zoomLevel;
	}

	private double getMaxZoomLevel() {
		return ZOOM_CLOSEUP_LEVELS[ZOOM_CLOSEUP_LEVELS.length - 1];
	}

	private void correctOverzooming() {
		if (zoomLevel > ZOOM_OVERZOOMING_LEVEL) {
			double highestCloseupLevel = getMaxZoomLevel();
			targetZoomLevel = highestCloseupLevel;
		}
	}

	private double nextZoomCloseupLevel() {
		for (int i = 0; i < ZOOM_CLOSEUP_LEVELS.length; i++)
			if (ZOOM_CLOSEUP_LEVELS[i] > targetZoomLevel + 0.01)
				return ZOOM_CLOSEUP_LEVELS[i];
		return getMaxZoomLevel();
	}

	private void panToTarget() {
		if (targetCenterEC.approximatelyEquals(centerEC))
			return;

		Vector distanceToTarget = targetCenterEC.minus(centerEC);
		centerEC = centerEC.plus(distanceToTarget.by(0.1));
	}

	private void zoomToTarget() {
		double differenceToTargetZoomLevel = targetZoomLevel - zoomLevel;
		if (Math.abs(differenceToTargetZoomLevel) < 0.001)
			return;

		final double zoomingSpeed = 0.015;
		this.zoomLevel = zoomLevel + differenceToTargetZoomLevel * zoomingSpeed;
	}
}
