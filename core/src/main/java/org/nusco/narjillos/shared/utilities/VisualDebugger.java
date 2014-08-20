package org.nusco.narjillos.shared.utilities;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.physics.Segment;

/**
 * A quick debugging class. It's just a global thing that holds segments.
 * If any code in the system loads it with segments, those segments get
 * visualized by the viewer classes. Whoever fills in the GlobalPeeker
 * also has the responsibility to clean it up, probably once per tick.
 * 
 * This class is made to be accessed by multiple threads, one of which is
 * the writer.
 */
public class VisualDebugger {

	private static final List<Segment> previousSegments = new LinkedList<>();
	private static final List<Segment> segments = new LinkedList<>();
	
	public synchronized static void clear() {
		// keep the full list of segments around in case
		// the reading thread peeks in during an update
		previousSegments.clear();
		previousSegments.addAll(segments);
		segments.clear();
	}
	
	public synchronized static void add(Segment s) {
		System.out.println("Global peeker -> added " + s);
		segments.add(s);
	}

	public synchronized static List<Segment> getSegments() {
		List<Segment> result = new LinkedList<>();
		result.addAll(previousSegments);
		return result;
	}

	public static final boolean DEBUG = true;
}
