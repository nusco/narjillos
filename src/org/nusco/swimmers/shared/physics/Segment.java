package org.nusco.swimmers.shared.physics;


public class Segment {

	public final Vector startPoint;
	public final Vector vector;

	public Segment(Vector startPoint, Vector vector) {
		this.startPoint = startPoint;
		this.vector = vector;
	}
}
