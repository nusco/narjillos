package org.nusco.narjillos.core.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberFormatterTest {

	@Test
	public void formatsNumbersToNiceStrings() {
		assertEquals("0", NumberFormatter.format(0));
		assertEquals("999", NumberFormatter.format(999));

		assertEquals("1K", NumberFormatter.format(1_000));
		assertEquals("1.2K", NumberFormatter.format(1_200));
		assertEquals("1.23K", NumberFormatter.format(1_234));
		assertEquals("123.49K", NumberFormatter.format(123_499));
		assertEquals("999.99K", NumberFormatter.format(999_999));

		assertEquals("1M", NumberFormatter.format(1_000_000));
		assertEquals("999.99M", NumberFormatter.format(999_999_999));

		assertEquals("1G", NumberFormatter.format(1_000_000_000L));
		assertEquals("123.45G", NumberFormatter.format(123_459_000_000L));
		assertEquals("999.99G", NumberFormatter.format(999_999_999_999L));
	}
}
