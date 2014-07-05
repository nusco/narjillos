package org.nusco.swimmers.views;

import org.nusco.swimmers.pond.Pond;

public class Viewport {

	public static final long MAX_INITIAL_SIZE = 800;
	static final double ZOOM_FACTOR = 1.03;
	static final double MAX_ZOOM = 1.6;

	private long sizeX;
	private long sizeY;
	private long centerX;
	private long centerY;
	private double zoomLevel = 1;

	private final double pondSize;
	
	public Viewport(Pond pond) {
		this.pondSize = pond.getSize();
		sizeX = Math.min(pond.getSize(), MAX_INITIAL_SIZE);
		sizeY = Math.min(pond.getSize(), MAX_INITIAL_SIZE);
		centerX = (long)(pond.getSize() / 2.0);
		centerY = (long)(pond.getSize() / 2.0);
		zoomToFit();
	}

	public long getSizeX() {
		return sizeX;
	}

	public long getSizeY() {
		return sizeY;
	}

	public void setSize(long sizeX, long sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public void centerOn(long x, long y) {
		this.centerX  = x;
		this.centerY  = y;
	}

	long getCenterX() {
		return centerX;
	}

	long getCenterY() {
		return centerY;
	}

	public long getUpperLeftCornerX() {
		return centerX - (sizeX / 2);
	}

	public long getUpperLeftCornerY() {
		return centerY - (sizeY / 2);
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

	public double getVisibleAreaX() {
		return getSizeX() / getZoomLevel();
	}

	public double getVisibleAreaY() {
		return getSizeY() / getZoomLevel();
	}

	private double wholePondScale() {
		return Math.max(getSizeX(), getSizeY()) / pondSize;
	}

	private void setZoomLevel(double zoomLevel) {
		this.zoomLevel = Math.min(Math.max(zoomLevel, wholePondScale()), MAX_ZOOM);
	}

	public boolean isVisible(double x, double y, double margin) {
		double maxRadius = maxVisibleRadius(margin);
		double distanceX = getCenterX() - x;
		double distanceY = getCenterY() - y;
		return (Math.abs(distanceX) < maxRadius &&
				Math.abs(distanceY) < maxRadius);
	}

	private double maxVisibleRadius(double margin) {
		return (Math.max(getSizeX(), getSizeY()) / 2 / getZoomLevel()) + margin * getZoomLevel();
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
