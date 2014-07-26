package org.nusco.narjillos.views;

import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * A Viewport is a zoomable, rectangular view over a pond.
 * 
 * It uses two systems of coordinates: SC are "screen coordinates" - the
 * position in the application window. PC are "pond coordinates" - the
 * position in the pond.
 */
public class Viewport {

	static final double MAX_INITIAL_SIZE_SC = 800;
	static final double MAX_ZOOM = 2;
	private static final double ZOOM_VELOCITY = 1.03;
	private static final double ZOOM_CLOSEUP = 0.45;

	private final double pondSizePC;
	private Vector sizeSC;
	private Vector centerPC;
	private double zoomLevel;

	private Vector targetCenterPC;
	private double targetZoomLevel;
	private final double minZoomLevel;

	public Viewport(Pond pond) {
		this.pondSizePC = pond.getSize();
		setCenterPC(getPondCenterPC());

		double size = Math.min(pond.getSize(), MAX_INITIAL_SIZE_SC);
		sizeSC = Vector.cartesian(size, size);
		minZoomLevel = Math.max(getSizeSC().x, getSizeSC().y) / pondSizePC;
		
		centerOnPond();
		zoomToFit();
	}

	private Vector getPondCenterPC() {
		return Vector.cartesian(pondSizePC / 2, pondSizePC / 2);
	}

	public Vector getSizeSC() {
		return sizeSC;
	}

	public void setSizeSC(Vector sizeSC) {
		this.sizeSC = sizeSC;
	}

	public Vector getPositionPC() {
		Vector offset = Vector.cartesian(toLengthPC(sizeSC.x), toLengthPC(sizeSC.y)).by(0.5);
		return centerPC.minus(offset);
	}

	public Vector getCenterPC() {
		return centerPC;
	}

	public void setCenterSC(Vector centerSC) {
		centerPC = toPC(centerSC);
	}

	final void setCenterPC(Vector centerPC) {
		this.centerPC = centerPC;
		this.targetCenterPC = centerPC;
	}

	public void moveBy(Vector velocitySC) {
		targetCenterPC = centerPC.plus(Vector.cartesian(toLengthPC(velocitySC.x), toLengthPC(velocitySC.y)));
	}

	public double getZoomLevel() {
		return zoomLevel;
	}

	public void zoomIn() {
		setZoomLevel(zoomLevel * ZOOM_VELOCITY);
	}

	public void zoomOut() {
		if (zoomLevel - minZoomLevel < 0.001) 
			targetCenterPC = getPondCenterPC();

		setZoomLevel(zoomLevel / ZOOM_VELOCITY);
	}

	public final void zoomToFit() {
		targetZoomLevel = minZoomLevel;
		zoomLevel = targetZoomLevel / 10;
	}

	private void centerOnPond() {
		centerPC = getPondCenterPC();
	}

	final void setZoomLevel(double zoomLevel) {
		this.zoomLevel = Math.min(Math.max(zoomLevel, minZoomLevel), MAX_ZOOM);
		this.targetZoomLevel = zoomLevel;
	}

	public void tick() {
		correctOverzooming();
		flyToTarget();
	}

	private void correctOverzooming() {
		if (zoomLevel > 1 && targetZoomLevel > 1)
			targetZoomLevel = 1;
	}

	public boolean isVisible(Vector pointPC, double marginPC) {
		Vector distance = centerPC.minus(pointPC);
		double maxAxialDistancePC = Math.max(Math.abs(distance.x), Math.abs(distance.y));
		double maxVisibleAxialDistance = Math.max(toLengthPC(sizeSC.x), toLengthPC(sizeSC.y)) / 2;
		return (maxAxialDistancePC <= maxVisibleAxialDistance + marginPC);
	}

	private double toLengthPC(double lengthSC) {
		return lengthSC / zoomLevel;
	}

	private final Vector toPC(Vector pointSC) {
		return getPositionPC().plus(pointSC.by(1.0 / zoomLevel));
	}

	public void flyToTargetSC(Vector targetSC) {
		targetCenterPC = toPC(targetSC);
	}

	public void flyToCloseUp() {
		targetZoomLevel = ZOOM_CLOSEUP;
	}

	private void flyToTarget() {
		panToTarget();
		zoomToTarget();
	}

	private void panToTarget() {
		if (targetCenterPC.almostEquals(centerPC))
			return;

		Vector distanceToTarget = targetCenterPC.minus(centerPC);
		centerPC = centerPC.plus(distanceToTarget.by(0.1));
	}

	private void zoomToTarget() {
		double differenceToTargetZoomLevel = targetZoomLevel - zoomLevel;
		if (Math.abs(differenceToTargetZoomLevel) < 0.001)
			return;

		this.zoomLevel = zoomLevel + differenceToTargetZoomLevel * 0.015;
	}
}
