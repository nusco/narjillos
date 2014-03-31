package org.nusco.swimmer.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.genetics.OrganBuilder;

public class OrganBuilderTest {

	@Test
	public void extractsLength() {
		OrganBuilder builder = new OrganBuilder(new int[] {10, 0, 0, 0});
		assertEquals(10 * OrganBuilder.PART_LENGTH_MULTIPLIER, builder.getLength());
	}

	@Test
	public void extractsThickness() {
		OrganBuilder builder = new OrganBuilder(new int[] {0, 12, 0, 0});
		assertEquals((int)(12 * OrganBuilder.PART_THICKNESS_MULTIPLIER), builder.getThickness());
	}

	@Test
	public void extractsRgb() {
		OrganBuilder builder = new OrganBuilder(new int[] {0, 0, 0, 10});
		assertEquals(10, builder.getRGB());
	}
}
