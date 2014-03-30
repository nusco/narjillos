package org.nusco.swimmers.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.body.Swimmer;

public class SwimmerTest {

	@Test
	public void returnsTheHeadOfTheSwimmer() {
		Swimmer swimmer = new Swimmer(ExampleParts.HEAD);
		assertEquals(ExampleParts.HEAD, swimmer.getHead());
	}

	@Test
	public void returnsTheBodyPartsOfTheSwimmer() {
		Swimmer swimmer = new Swimmer(ExampleParts.HEAD);
		assertEquals(ExampleParts.asList(), swimmer.getParts());
	}
}
