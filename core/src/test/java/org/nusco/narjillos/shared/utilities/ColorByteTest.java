package org.nusco.narjillos.shared.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ColorByteTest {

	@Test
	public void soresABiteSizedNumber() {
		ColorByte color = new ColorByte(255);
		assertEquals(255, color.toByteSizedInt());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void failsWhenGivenANumberThatDoesntFitOneByte() {
		new ColorByte(256);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void failsWhenGivenANegativeNumber() {
		new ColorByte(-1);
	}

	@Test
	public void readsRedFromTheLowest3BitsMultipliedBy7() {
		ColorByte color = new ColorByte(0b00000111);
		assertEquals(1, color.getRed(), 0.0);
		assertEquals(0, color.getGreen(), 0.0);
		assertEquals(0, color.getBlue(), 0.0);
	}

	@Test
	public void readsGreenFromTheCentral3Bits() {
		ColorByte color = new ColorByte(0b00111000);
		assertEquals(0, color.getRed(), 0.0);
		assertEquals(1, color.getGreen(), 0.0);
		assertEquals(0, color.getBlue(), 0.0);
	}

	@Test
	public void readsBlueFromTheTopmost2Bits() {
		ColorByte color = new ColorByte(0b11000000);
		assertEquals(0, color.getRed(), 0.0);
		assertEquals(0, color.getGreen(), 0.0);
		// blue has its least significant bit always set to 0,
		// so this is the max possible blue value
		assertEquals(0.85, color.getBlue(), 0.01);
	}

	@Test
	public void mixesColorsSubtractivelyIfTheirLSBIsTheSame() {
		ColorByte color1 = new ColorByte(0b11001100);
		ColorByte color2 = new ColorByte(0b01100110);
		
		ColorByte mix = color1.mix(color2);

		assertEquals(new ColorByte(0b01000100), mix);
	}

	@Test
	public void mixesColorsAdditivelyIfTheirLSBIsDifferent() {
		ColorByte color1 = new ColorByte(0b11001100);
		ColorByte color2 = new ColorByte(0b01100111);
		
		ColorByte mix = color1.mix(color2);

		assertEquals(new ColorByte(0b11101111), mix);
	}

	@Test
	public void mixingIsSymmetric() {
		ColorByte color1 = new ColorByte(0b11001100);
		ColorByte color2 = new ColorByte(0b01100110);
		
		assertEquals(color1.mix(color2), color2.mix(color1));
	}
}
