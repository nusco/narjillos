package org.nusco.swimmers.views;

import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;

public class Viewport {

	public static final long MAX_INITIAL_SIZE = 800;
	static final double ZOOM_FACTOR = 1.03;
	static final double MAX_ZOOM = 1.6;

	private Vector size;
	private Vector center;
	private double zoomLevel = 1;

	private final double pondSize;
	
	public Viewport(Pond pond) {
		this.pondSize = pond.getSize();
		double sizeValue = Math.min(pond.getSize(), MAX_INITIAL_SIZE);
		size = Vector.cartesian(sizeValue, sizeValue);
		long centerValue = (long)(pond.getSize() / 2.0);
		center = Vector.cartesian(centerValue, centerValue);
		zoomToFit();
	}

	public Vector getSize() {
		return size;
	}

	public void setSize(Vector size) {
		this.size = size;
	}

	public void moveBy(Vector velocity) {
		centerOn(Vector.cartesian(getCenter().x + velocity.x / zoomLevel, getCenter().y + velocity.y / zoomLevel));
	}

	public void centerOn(Vector coordinates) {
		this.center  = coordinates;
	}

	public Vector toPondCoordinates(Vector viewportCoordinates) {
		double x = getPosition().x + viewportCoordinates.x * zoomLevel;
		double y = getPosition().y + viewportCoordinates.y * zoomLevel;
		return Vector.cartesian(x, y);
	}

	public Vector getCenter() {
		return center;
	}

	public Vector getPosition() {
		double x = getCenter().x - (getSize().x / 2);
		double y = getCenter().y - (getSize().y / 2);
		return Vector.cartesian(x, y);
	}

	public double getZoomLevel() {
		return zoomLevel;
	}

	public void zoomIn() {
		setZoomLevel(getZoomLevel() * ZOOM_FACTOR);
	}

	public void zoomOut() {
		setZoomLevel(getZoomLevel() / ZOOM_FACTOR);
	}

	public final void zoomToFit() {
		setZoomLevel(wholePondScale());
	}

	public Vector getVisibleArea() {
		double x = getSize().x / getZoomLevel();
		double y = getSize().y / getZoomLevel();
		return Vector.cartesian(x, y);
	}

	private double wholePondScale() {
		return Math.max(getSize().x, getSize().y) / pondSize;
	}

	private void setZoomLevel(double zoomLevel) {
		this.zoomLevel = Math.min(Math.max(zoomLevel, wholePondScale()), MAX_ZOOM);
	}

	public boolean isVisible(Vector point, double margin) {
		double maxRadius = maxVisibleRadius(margin);
		double distanceX = getCenter().x - point.x;
		double distanceY = getCenter().y - point.y;
		return (Math.abs(distanceX) < maxRadius &&
				Math.abs(distanceY) < maxRadius);
	}

	private double maxVisibleRadius(double margin) {
		return (Math.max(getSize().x, getSize().y) / 2 / getZoomLevel()) + margin * getZoomLevel();
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
		setZoomLevel(getZoomLevel() - attenuation);
	}
}
