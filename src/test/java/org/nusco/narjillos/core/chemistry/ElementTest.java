package org.nusco.narjillos.core.chemistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nusco.narjillos.core.chemistry.Element.*;

import org.junit.jupiter.api.Test;

public class ElementTest {

	@Test
	public void convertsToAShortString() {
		assertThat(OXYGEN.toString()).isEqualTo("O");
		assertThat(HYDROGEN.toString()).isEqualTo("H");
		assertThat(NITROGEN.toString()).isEqualTo("N");
		assertThat(ZERO.toString()).isEqualTo("Z");
	}

	@Test
	public void convertsAnIntegerToANonZeroElement() {
		assertThat(OXYGEN).isEqualTo(Element.fromInteger(0));
		assertThat(HYDROGEN).isEqualTo(Element.fromInteger(1));
		assertThat(NITROGEN).isEqualTo(Element.fromInteger(2));
		assertThat(OXYGEN).isEqualTo(Element.fromInteger(3));
		assertThat(HYDROGEN).isEqualTo(Element.fromInteger(4));
		assertThat(NITROGEN).isEqualTo(Element.fromInteger(5));
		assertThat(OXYGEN).isEqualTo(Element.fromInteger(6));
	}
}
