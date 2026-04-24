package org.nusco.narjillos.core.geometry;

import java.util.Set;

public class BoundingBox {

	public final double left;

	public final double right;

	public final double bottom;

	public final double top;

	public BoundingBox(Segment segment) {
		this(segment.getStartPoint().x, segment.getEndPoint().x, segment.getStartPoint().y, segment.getEndPoint().y);
	}

	public BoundingBox(double left, double right, double bottom, double top) {
		this.left = Math.min(left, right);
		this.right = Math.max(left, right);
		this.bottom = Math.min(bottom, top);
		this.top = Math.max(bottom, top);
	}

	public static BoundingBox punctiform(Vector v) {
		return new BoundingBox(v.x, v.x, v.y, v.y);
	}

	public boolean overlaps(BoundingBox other) {
		return !(top <= other.bottom || bottom >= other.top || right <= other.left || left >= other.right);
	}

	public boolean isContainedIn(BoundingBox other) {
		return left >= other.left && right <= other.right && bottom >= other.bottom && top <= other.top;
	}

	@Override
	public int hashCode() {
		return (int) (bottom + top);
	}

    @Override
	public boolean equals(Object obj) {
		BoundingBox other = (BoundingBox) obj;
		if (Double.doubleToLongBits(bottom) != Double.doubleToLongBits(other.bottom))
			return false;
		if (Double.doubleToLongBits(left) != Double.doubleToLongBits(other.left))
			return false;
		if (Double.doubleToLongBits(right) != Double.doubleToLongBits(other.right))
			return false;
		return Double.doubleToLongBits(top) == Double.doubleToLongBits(other.top);
	}

	public static BoundingBox union(Set<BoundingBox> boundingBoxes) {
		if (boundingBoxes.isEmpty())
			throw new RuntimeException("Empty bounding box union");

		double left = Double.POSITIVE_INFINITY;
		double right = Double.NEGATIVE_INFINITY;
		double bottom = Double.POSITIVE_INFINITY;
		double top = Double.NEGATIVE_INFINITY;

		for (BoundingBox boundingBox : boundingBoxes) {
			left = Math.min(left, boundingBox.left);
			right = Math.max(right, boundingBox.right);
			bottom = Math.min(bottom, boundingBox.bottom);
			top = Math.max(top, boundingBox.top);
		}

		return new BoundingBox(left, right, bottom, top);
	}
}
