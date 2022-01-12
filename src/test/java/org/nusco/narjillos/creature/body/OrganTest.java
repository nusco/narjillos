package org.nusco.narjillos.creature.body;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;

public abstract class OrganTest {

	ConnectedOrgan organ;

	@BeforeEach
	public void setUpPart() {
		organ = createConcreteOrgan(50, 20);
	}

	protected abstract ConnectedOrgan createConcreteOrgan(int length, int thickness);

	@Test
	public void hasALengthThatGrowsWithTime() {
		assertThat(organ.getLength()).isEqualTo(5.0);

		organ.growToAdultFormWithChildren();

		assertThat(organ.getLength()).isEqualTo(50.0);
	}

	@Test
	public void hasAThicknessThatGrowsWithTime() {
		assertThat(organ.getThickness()).isEqualTo(1.0);

		organ.growToAdultFormWithChildren();

		assertThat(organ.getThickness()).isEqualTo(20.0);
	}

	@Test
	public abstract void hasAnEndPoint();

	@Test
	public void hasAMassProportionalToItsArea() {
		organ.growToAdultFormWithChildren();

		assertThat(organ.getMass()).isEqualTo(1000.0, within(0.01));
	}

	@Test
	public void itsMassIsAlwaysAtLeast1() {
		var verySmallBodyPart = new Organ(0, 0, new Fiber(0, 0, 0)) {
			@Override
			protected double calculateAbsoluteAngle() {
				return 0;
			}

			@Override
			protected Vector calculateStartPoint() {
				return null;
			}
		};

		assertThat(verySmallBodyPart.getMass()).isEqualTo(1.0, within(0.0001));
	}
}
