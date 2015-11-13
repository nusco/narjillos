package org.nusco.narjillos.core.geometry;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BoundingBox {

	public final double left;
	public final double right;
	public final double bottom;
	public final double top;

	public BoundingBox(Set<SegmentShape> shapes) {
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

		List<Vector> points = collectPoints(shapes);
		for (Vector point : points) {
			minX = Math.min(minX, point.x);
			maxX = Math.max(maxX, point.x);
			minY = Math.min(minY, point.y);
			maxY = Math.max(maxY, point.y);
		}
		
		left = minX;
		right = maxX;
		bottom = minY;
		top = maxY;
	}

	private List<Vector> collectPoints(Set<SegmentShape> shapes) {
		List<Vector> result = new LinkedList<>();

		if (shapes.isEmpty()) {
			result.add(Vector.ZERO);
			return result;
		}
		
		// TODO: use map()
		for (SegmentShape segmentShape : shapes) {
			Segment segment = segmentShape.toSegment();
			result.add(segment.getStartPoint());
			result.add(segment.getEndPoint());
		}
		return result;
	}

	public boolean overlaps(BoundingBox other) {
		return top > other.bottom && bottom < other.top && right > other.left && left < other.right;
	}
}
