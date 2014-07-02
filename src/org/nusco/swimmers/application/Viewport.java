package org.nusco.swimmers.application;

import org.nusco.swimmers.shared.physics.Vector;

public class Viewport {

	public static final int SIZE_X = 800;
	public static final int SIZE_Y = 600;

	private int sizeX = SIZE_X;
	private int sizeY = SIZE_Y;
	private Vector position = Vector.ZERO;
	
	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSize(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public Vector getPosition() {
		return position;
	}

	public void moveTo(Vector position) {
		this.position  = position;
	}
}
