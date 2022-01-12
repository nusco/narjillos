package org.nusco.narjillos.core.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class NumberFormatterTest {

	@Test
	@Disabled
	public void formatsNumbersToNiceStrings() {

		assertThat(NumberFormatter.format(0)).isEqualTo("0");
		assertThat(NumberFormatter.format(999)).isEqualTo("999");

		assertThat(NumberFormatter.format(1_000)).isEqualTo("1K");
		assertThat(NumberFormatter.format(1_200)).isEqualTo("1.2K");
		assertThat(NumberFormatter.format(1_234)).isEqualTo("1.23K");
		assertThat(NumberFormatter.format(123_499)).isEqualTo("123.49K");
		assertThat(NumberFormatter.format(999_999)).isEqualTo("999.99K");

		assertThat(NumberFormatter.format(1_000_000)).isEqualTo("1M");
		assertThat(NumberFormatter.format(999_999_999)).isEqualTo("999.99M");

		assertThat(NumberFormatter.format(1_000_000_000L)).isEqualTo("1G");
		assertThat(NumberFormatter.format(123_459_000_000L)).isEqualTo("123.45G");
		assertThat(NumberFormatter.format(999_999_999_999L)).isEqualTo("999.99G");
	}
}
