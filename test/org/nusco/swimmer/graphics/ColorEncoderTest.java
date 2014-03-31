package org.nusco.swimmer.graphics;

import static org.junit.Assert.*;
import javafx.scene.paint.Color;

import org.junit.Test;

public class ColorEncoderTest {

	@Test
	public void testReadsRedFromTheLowest3BitsMultipliedBy7() {
		Color color = ColorEncoder.toColor(0b00000111);
		assertEquals(1, color.getRed(), 0.0);
		assertEquals(0, color.getGreen(), 0.0);
		assertEquals(0, color.getBlue(), 0.0);
	}

	@Test
	public void testReadsGreenFromTheCentral3BitsMultipliedBy7() {
		Color color = ColorEncoder.toColor(0b00111000);
		assertEquals(0, color.getRed(), 0.0);
		assertEquals(1, color.getGreen(), 0.0);
		assertEquals(0, color.getBlue(), 0.0);
	}

	@Test
	public void testReadsGreenFromTheTopmost2BitsMultipliedBy7() {
		Color color = ColorEncoder.toColor(0b11000000);
		assertEquals(0, color.getRed(), 0.0);
		assertEquals(0, color.getGreen(), 0.0);
		assertEquals(0.8, color.getBlue(), 0.1);
	}

	@Test
	public void testAddsTransparency() {
		Color color = ColorEncoder.toColor(0);
		assertEquals(0.6, color.getOpacity(), 0.01);
	}

}
