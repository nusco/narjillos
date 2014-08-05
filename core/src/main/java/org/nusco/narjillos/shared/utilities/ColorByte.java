package org.nusco.narjillos.shared.utilities;

/**
 * Stores a wide range of colors in a single byte-sized integer.
 */
public class ColorByte {

	private static final double MAX_THREE_BITS_VALUE = 0b111;

	private final int colorByte;

	public ColorByte(int zeroTo255) {
		// better be defensive here, considering how easy it
		// is to get Java unsigned bytes wrong
		if (zeroTo255 < 0 || zeroTo255 > 255)
			throw new IllegalArgumentException("Invalid ColorByte: " + zeroTo255);
		colorByte = zeroTo255;
	}

	public ColorByte mix(ColorByte other) {
		// arbitrary rule:
		// two colors with the same least-significant byte mix additively,
		// generating a lighter color;
		// colors with different least-significant bytes mix with an AND,
		// generating a darker color.
		if (getLSB() == other.getLSB())
			return new ColorByte(colorByte & other.colorByte);

		return new ColorByte(colorByte | other.colorByte);
	}

	private int getLSB() {
		return colorByte & 0b1;
	}

	public int toByteSizedInt() {
		return colorByte;
	}

	public double getRed() {
		return ((colorByte & 0b00000111) >> 0) / MAX_THREE_BITS_VALUE;
	}

	public double getGreen() {
		return ((colorByte & 0b00111000) >> 3) / MAX_THREE_BITS_VALUE;
	}

	public double getBlue() {
		// blue's lowest bit is always 0 to fit three colors in 8 bits
		return ((colorByte & 0b11000000) >> 5) / MAX_THREE_BITS_VALUE;
	}

	@Override
	public boolean equals(Object obj) {
		return colorByte == ((ColorByte) obj).colorByte;
	}
	
	@Override
	public int hashCode() {
		return colorByte;
	}
}
