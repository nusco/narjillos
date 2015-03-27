package org.nusco.narjillos.shared.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberFormatTest {
	@Test
	public void formatsNumbersToNiceStrings() {
		assertEquals("0", NumberFormat.format(0));
		assertEquals("999", NumberFormat.format(999));
		
		assertEquals("1K", NumberFormat.format(1_000));
		assertEquals("1.2K", NumberFormat.format(1_234));
		assertEquals("123.5K", NumberFormat.format(123_499));
		assertEquals("999.9K", NumberFormat.format(999_900));

		assertEquals("1M", NumberFormat.format(1_000_000));
		assertEquals("999.99M", NumberFormat.format(999_994_000));
		assertEquals("1000M", NumberFormat.format(999_995_000));
		assertEquals("1234.11M", NumberFormat.format(1_234_111_000));
	}
}
