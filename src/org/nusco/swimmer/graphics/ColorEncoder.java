package org.nusco.swimmer.graphics;

import javafx.scene.paint.Color;

class ColorEncoder {

	public static Color toColor(int rgb) {
		byte rgbByte = (byte)rgb;
		double MAX_VALUE_IN_3_BITS = 7.0;
		double red = (rgbByte & 0b00000111) / MAX_VALUE_IN_3_BITS;
		double green = ((rgbByte & 0b00111000) >> 3) / MAX_VALUE_IN_3_BITS;
		double blue = ((rgbByte & 0b11000000) >> 5) / MAX_VALUE_IN_3_BITS;
		final double alpha = 0.6;
		return new Color(red, green, blue, alpha);
	}
}
