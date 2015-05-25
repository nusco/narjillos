package org.nusco.narjillos.core.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.NumberFormat;

public class NumberFormatTest {
	@Test
	public void formatsNumbersToNiceStrings() {
		assertEquals("0", NumberFormat.format(0));
		assertEquals("999", NumberFormat.format(999));
		
		assertEquals("1K", NumberFormat.format(1_000));
		assertEquals("1.2K", NumberFormat.format(1_200));
		assertEquals("1.23K", NumberFormat.format(1_234));
		assertEquals("123.49K", NumberFormat.format(123_499));
		assertEquals("999.99K", NumberFormat.format(999_999));

		assertEquals("1M", NumberFormat.format(1_000_000));
		assertEquals("999.99M", NumberFormat.format(999_999_999));

		assertEquals("1G", NumberFormat.format(1_000_000_000L));
		assertEquals("123.45G", NumberFormat.format(123_459_000_000L));
		assertEquals("999.99G", NumberFormat.format(999_999_999_999L));
	}
}
