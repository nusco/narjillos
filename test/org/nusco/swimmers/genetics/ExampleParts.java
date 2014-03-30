package org.nusco.swimmers.genetics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.body.Head;
import org.nusco.swimmers.body.VisibleOrgan;

public class ExampleParts {
	public final static VisibleOrgan HEAD = new Head(25, 2, 123);
	public final static VisibleOrgan CHILD_1 = HEAD.sproutVisibleOrgan(60, 2, 45, 123);
	public final static VisibleOrgan CHILD_2 = HEAD.sproutVisibleOrgan(60, 2, -45, 123);
	public final static VisibleOrgan CHILD_1_1 = CHILD_1.sproutVisibleOrgan(35, 3, 30, 123);
	public final static VisibleOrgan CHILD_1_2 = CHILD_1.sproutVisibleOrgan(35, 3, -30, 123);
	public final static VisibleOrgan CHILD_2_1 = CHILD_2.sproutVisibleOrgan(15, 3, 30, 123);
	public final static VisibleOrgan CHILD_2_2 = CHILD_2.sproutVisibleOrgan(15, 3, -30, 123);

	public static List<VisibleOrgan> asList() {
		List<VisibleOrgan> expected = new LinkedList<>();
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
