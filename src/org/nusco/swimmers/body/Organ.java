package org.nusco.swimmers.body;

import java.util.List;

public interface Organ {

	public abstract double getAngle();

	public abstract double getRelativeAngle();

	public abstract Vector getEndPoint();

	public abstract int getRGB();

	public abstract Organ getAsParent();

	public abstract Organ getParent();

	public abstract List<Organ> getChildren();

	public abstract VisibleOrgan sproutVisibleOrgan(int length, int thickness, int initialRelativeAngle, int rgb);
	
	public abstract Organ sproutInvisibleOrgan();

	public abstract boolean isVisible();

	public abstract String toString();

	public abstract int getLength();

	public abstract int getThickness();

	public abstract Vector getStartPoint();
}