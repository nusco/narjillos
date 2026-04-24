package org.nusco.narjillos.core.utilities;

import java.text.DecimalFormat;

/**
 * Formats numbers for pretty printing.
 * <p>
 * For example, 2000 becomes 2K, 1200000 becomes 1.2M, and so on.
 */
public class NumberFormatter {

	private final static DecimalFormat FORMAT = new DecimalFormat("###.##");

	public static String format(long number) {
		if (number < 1_000)
			return String.valueOf(number);

		if (number <= 999_999) {
			double thousands = Math.floor(number / 10) / 100.0;
			return FORMAT.format(thousands) + "K";
		}

		if (number <= 999_999_999L) {
			double millions = Math.floor(number / 10_000.0) / 100.0;
			return FORMAT.format(millions) + "M";
		}

		double billions = Math.floor(number / 10_000_000.0) / 100.0;
		return FORMAT.format(billions) + "G";
	}

	public static String format(double number) {
		// two decimals
		return Double.toString(((long) (number * 100)) / 100.0);
	}
}
