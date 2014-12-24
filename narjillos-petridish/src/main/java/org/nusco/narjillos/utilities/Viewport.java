package org.nusco.narjillos.utilities;

import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * A Viewport is a zoomable, rectangular view over an ecosystem.
 * 
 * It uses two systems of coordinates: SC are "screen coordinates" - the
 * position in the application window. EC are "ecosystem coordinates" - the
 * position in the ecosystem.
 */
public class Viewport {

	static final double MAX_INITIAL_SIZE_SC = 800;
	static final double MAX_ZOOM = 2;
	private static final double ZOOM_VELOCITY = 1.03;
	static final double[] ZOOM_CLOSEUP_LEVELS = new double[] { 0.15, 0.6 };

	private final double ecosystemSizeEC;
	private Vector sizeSC;
	private Vector centerEC;
	private double zoomLevel;

	private Vector targetCenterEC;
	private double targetZoomLevel;
	private final double fitAllZoomLevel;
	private volatile boolean userIsZooming = false;
	final double minZoomLevel;
	
	public Viewport(Ecosystem ecosystem) {
		this.ecosystemSizeEC = ecosystem.getSize();
		setCenterEC(getEcosystemCenterEC());

		double size = Math.min(ecosystem.getSize(), MAX_INITIAL_SIZE_SC);
		sizeSC = Vector.cartesian(size, size);
		fitAllZoomLevel = Math.max(getSizeSC().x, getSizeSC().y) / ecosystemSizeEC;
		minZoomLevel = fitAllZoomLevel / 2.5;
		
		centerOnEcosystem();
		zoomLevel = minZoomLevel;
		flyToNextZoomCloseupLevel();
	}

	private Vector getEcosystemCenterEC() {
		return Vector.cartesian(ecosystemSizeEC / 2, ecosystemSizeEC / 2);
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

	public void setCenterSC(Vector centerSC) {
		centerEC = toEC(centerSC);
	}

	final void setCenterEC(Vector centerEC) {
		this.centerEC = centerEC;
		this.targetCenterEC = centerEC;
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

	private void centerOnEcosystem() {
		centerEC = getEcosystemCenterEC();
	}


	public final void zoomTo(double zoomLevel) {
		this.zoomLevel = Math.min(Math.max(zoomLevel, minZoomLevel), MAX_ZOOM);
		this.targetZoomLevel = zoomLevel;
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

	private void correctOverzooming() {
		if (zoomLevel > 1) {
			double highestCloseupLevel = getMaxZoomLevel();
			targetZoomLevel = highestCloseupLevel;
		}
	}

	public boolean isVisible(Vector pointEC, double marginEC) {
		Vector distance = centerEC.minus(pointEC);
		double maxAxialDistancePC = Math.max(Math.abs(distance.x), Math.abs(distance.y));
		double maxVisibleAxialDistance = Math.max(toLengthEC(sizeSC.x), toLengthEC(sizeSC.y)) / 2;
		return (maxAxialDistancePC <= maxVisibleAxialDistance + marginEC);
	}

	private double toLengthEC(double lengthSC) {
		return lengthSC / zoomLevel;
	}

	public final Vector toEC(Vector pointSC) {
		return getPositionEC().plus(pointSC.by(1.0 / zoomLevel));
	}

	public void flyToTargetSC(Vector targetSC) {
		targetCenterEC = toEC(targetSC);
	}

	public void flyToTargetEC(Vector targetEC) {
		targetCenterEC = targetEC;
	}

	public void flyToNextZoomCloseupLevel() {
		targetZoomLevel = nextZoomCloseupLevel();
	}

	public void flyToMaxZoomCloseupLevel() {
		targetZoomLevel = getMaxZoomLevel();
	}

	public boolean isZoomedOutCompletely() {
		return Math.abs(zoomLevel - minZoomLevel) < 0.001;
	}

	public boolean isZoomCloseToTarget() {
		return Math.abs(zoomLevel - targetZoomLevel) < 0.1;
	}

	private double getMaxZoomLevel() {
		return ZOOM_CLOSEUP_LEVELS[ZOOM_CLOSEUP_LEVELS.length - 1];
	}

	private double nextZoomCloseupLevel() {
		for (int i = 0; i < ZOOM_CLOSEUP_LEVELS.length; i++)
			if (ZOOM_CLOSEUP_LEVELS[i] > targetZoomLevel + 0.01)
				return ZOOM_CLOSEUP_LEVELS[i];
		return getMaxZoomLevel();
	}

	private void panToTarget() {
		if (targetCenterEC.almostEquals(centerEC))
			return;

		Vector distanceToTarget = targetCenterEC.minus(centerEC);
		centerEC = centerEC.plus(distanceToTarget.by(0.1));
	}

	private void zoomToTarget() {
		double differenceToTargetZoomLevel = targetZoomLevel - zoomLevel;
		if (Math.abs(differenceToTargetZoomLevel) < 0.001)
			return;

		this.zoomLevel = zoomLevel + differenceToTargetZoomLevel * 0.015;
	}
}
