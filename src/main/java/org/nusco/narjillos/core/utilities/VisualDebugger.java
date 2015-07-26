package org.nusco.narjillos.core.utilities;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.physics.Segment;

/**
 * A quick debugging class.
 * 
 * This is just a global object that holds geometric Segments. If any code in
 * the system loads it with Segments, those segments will be visualized by the
 * viewer classes.
 * 
 * This class is made to be accessed by multiple threads: one will write the
 * Segments, the other(s) will read them.
 */
public class VisualDebugger {

	// set this to true before compilation to activate the debugger
	public static final boolean DEBUG = false;

	private static final List<Segment> previousSegments = new LinkedList<>();
	private static final List<Segment> segments = new LinkedList<>();

	public synchronized static List<Segment> getSegments() {
		// The reading thread might peek in during an update,
		// so we always maintain a backup of the previous update
		List<Segment> result = new LinkedList<>();
		result.addAll(previousSegments);
		return result;
	}

	public synchronized static void add(Segment s) {
		segments.add(s);
	}

	public synchronized static void clear() {
		previousSegments.clear();
		previousSegments.addAll(segments);
		segments.clear();
	}
}
