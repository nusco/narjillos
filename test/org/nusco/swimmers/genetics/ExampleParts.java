package org.nusco.swimmers.genetics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.body.Head;
import org.nusco.swimmers.body.VisibleOrgan;

public class ExampleParts {
	public final static Head HEAD = new Head(50, 4, 123);
	public final static VisibleOrgan CHILD_1 = HEAD.sproutVisibleOrgan(120, 4, 45, 123);
	public final static VisibleOrgan CHILD_2 = HEAD.sproutVisibleOrgan(120, 4, -45, 123);
	public final static VisibleOrgan CHILD_1_1 = CHILD_1.sproutVisibleOrgan(70, 6, 30, 123);
	public final static VisibleOrgan CHILD_1_2 = CHILD_1.sproutVisibleOrgan(70, 6, -30, 123);
	public final static VisibleOrgan CHILD_2_1 = CHILD_2.sproutVisibleOrgan(30, 6, 30, 123);
	public final static VisibleOrgan CHILD_2_2 = CHILD_2.sproutVisibleOrgan(30, 6, -30, 123);
	public final static VisibleOrgan CHILD_1_1_1 = CHILD_1_1.sproutVisibleOrgan(50, 4, 20, 123);
	public final static VisibleOrgan CHILD_1_1_2 = CHILD_1_1.sproutVisibleOrgan(50, 4, -20, 123);
	public final static VisibleOrgan CHILD_2_2_1 = CHILD_2_2.sproutVisibleOrgan(50, 4, 20, 123);
	public final static VisibleOrgan CHILD_2_2_2 = CHILD_2_2.sproutVisibleOrgan(50, 4, -20, 123);

	public static List<VisibleOrgan> asList() {
		List<VisibleOrgan> expected = new LinkedList<>();
		expected.add(HEAD);
		expected.add(CHILD_1);
		expected.add(CHILD_1_1);
		expected.add(CHILD_1_1_1);
		expected.add(CHILD_1_1_2);
		expected.add(CHILD_1_2);
		expected.add(CHILD_2);
		expected.add(CHILD_2_1);
		expected.add(CHILD_2_2);
		expected.add(CHILD_2_2_1);
		expected.add(CHILD_2_2_2);
		return expected;
	}
}
