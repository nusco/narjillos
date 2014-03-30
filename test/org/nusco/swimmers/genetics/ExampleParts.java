package org.nusco.swimmers.genetics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.body.HeadPart;
import org.nusco.swimmers.body.Part;

public class ExampleParts {
	public final static Part HEAD = new HeadPart(50, 10);
	public final static Part CHILD_1 = HEAD.sproutChild(120, 12, 45);
	public final static Part CHILD_2 = HEAD.sproutChild(120, 12, -45);
	public final static Part CHILD_1_1 = CHILD_1.sproutChild(70, 15, 30);
	public final static Part CHILD_1_2 = CHILD_1.sproutChild(70, 15, -30);
	public final static Part CHILD_2_1 = CHILD_2.sproutChild(30, 15, 30);
	public final static Part CHILD_2_2 = CHILD_2.sproutChild(30, 15, -30);

	public static List<Part> asList() {
		List<Part> expected = new LinkedList<>();
		expected.add(HEAD);
		expected.add(CHILD_1);
		expected.add(CHILD_2);
		expected.add(CHILD_1_1);
		expected.add(CHILD_1_2);
		expected.add(CHILD_2_1);
		expected.add(CHILD_2_2);
		return expected;
	}
}
