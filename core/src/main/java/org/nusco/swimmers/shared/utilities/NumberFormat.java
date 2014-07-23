package org.nusco.swimmers.shared.utilities;

import java.text.DecimalFormat;

public class NumberFormat {
	
	private final static DecimalFormat THOUSANDS_FORMAT = new DecimalFormat("##.#K");
	private final static DecimalFormat MILLIONS_FORMAT = new DecimalFormat("#.##M");

	public static String format(long number) {
		if (number < 1_000)
			return String.valueOf(number);

		if (number < 999_999) {
			double thousands = (double) number / 1000;
			return THOUSANDS_FORMAT.format(thousands);
		}

		double millions = (double) number / 1_000_000;
		return MILLIONS_FORMAT.format(millions);
	}

}
