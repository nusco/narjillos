package org.nusco.swimmers.views;

import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;

/**
 * A Viewport is a limited-sized view over a pond, possibly zoomed.
 * 
 * It converts between two systems of coordinates:
 * 
 * SC are "screen coordinates" - the position in the application window.
 * PC are "pond coordinates" - the position in the pond.
 */
public class Viewport {

	static final long MAX_INITIAL_SIZE_SC = 800;
	static final double MAX_ZOOM = 1.6;
	private static final double ZOOM_VELOCITY = 1.03;
	
	private final double pondSizePC;
	private Vector centerPC;
	private Vector sizeSC;
	private double zoomLevel = 1;

	public Viewport(Pond pond) {
		this.pondSizePC = pond.getSize();
		this.centerPC = Vector.cartesian(pondSizePC /2, pondSizePC / 2);

		double size = Math.min(pond.getSize(), MAX_INITIAL_SIZE_SC);
		sizeSC = Vector.cartesian(size, size);

		zoomToFit();
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

	public void setCenterPC(Vector centerPC) {
		this.centerPC = centerPC;
	}

	public void moveBy(Vector velocitySC) {
		centerPC = centerPC.plus(Vector.cartesian(toLengthPC(velocitySC.x), toLengthPC(velocitySC.y)));
	}

	public double getZoomLevel() {
		return zoomLevel;
	}

	public void zoomIn() {
		setZoomLevel(zoomLevel * ZOOM_VELOCITY);
	}

	public void zoomOut() {
		setZoomLevel(zoomLevel / ZOOM_VELOCITY);
	}

	public final void zoomToFit() {
		setZoomLevel(wholePondScale());
	}

	private double wholePondScale() {
		return Math.max(getSizeSC().x, getSizeSC().y) / pondSizePC;
	}

	private void setZoomLevel(double zoomLevel) {
		this.zoomLevel = Math.min(Math.max(zoomLevel, wholePondScale()), MAX_ZOOM);
	}

	public void tick() {
		correctOverzooming();
	}

	private void correctOverzooming() {
		if (zoomLevel < 1)
			return;
		
		double excessZoom = zoomLevel - 1;
		if (excessZoom < 0.001) {
			setZoomLevel(1);
			return;
		}
		
		double attenuation = Math.max(0.01, excessZoom / 10);
		setZoomLevel(zoomLevel - attenuation);
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
		return getPositionPC().plus(pointSC.by(1 / zoomLevel));
	}
}
