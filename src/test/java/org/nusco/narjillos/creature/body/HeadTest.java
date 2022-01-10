package org.nusco.narjillos.creature.body;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.body.pns.WaveNerve;

public class HeadTest extends ConnectedOrganTest {

	@Override
	public Head createConcreteOrgan(int length, int thickness) {
		return new Head(new HeadParameters(length, thickness));
	}

	@Override
	public void hasAParent() {
		// ...only it's null
		assertThat(getOrgan().getParent()).isNull();
	}

	@Override
	public void hasAnEndPoint() {
		organ.growToAdultFormWithChildren();

		assertThat(organ.getEndPoint()).isEqualTo(Vector.cartesian(50, 0));
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertThat(organ.getStartPoint()).isEqualTo(Vector.ZERO);
	}

	@Test
	public void hasAWaveNerve() {
		var nerve = new Head(new HeadParameters()).getNerve();

		assertThat(nerve).isInstanceOf(WaveNerve.class);
	}

	@Test
	public void hasACenterOfMass() {
		var head = new Head(new HeadParameters(10, 6));
		head.setAngleToParent(90);
		head.update();

		head.growToAdultFormWithChildren();

		assertThat(head.getCenterOfMass()
					   .approximatelyEquals(Vector.cartesian(0, 5))).isTrue();
	}

	@Test
	public void hasAMetabolicRate() {
		var parameters = new HeadParameters();
		parameters.setMetabolicRate(2.1);
		var head = new Head(parameters);

		assertThat(head.getMetabolicRate()).isEqualTo(2.1);
	}

	@Test
	public void hasAWaveBeatRatio() {
		var parameters = new HeadParameters();
		parameters.setWaveBeatRatio(2.3);
		var head = new Head(parameters);

		assertThat(head.getWaveBeatRatio()).isEqualTo(2.3);
	}

	@Test
	public void hasAByproduct() {
		var parameters = new HeadParameters();
		parameters.setByproduct(Element.NITROGEN);
		var head = new Head(parameters);

		assertThat(head.getByproduct()).isEqualTo(Element.NITROGEN);
	}

	@Test
	public void hasAnAmountOfEnergyThatItPassesOnToChildren() {
		var parameters = new HeadParameters();
		parameters.setEnergyToChildren(1000);
		var head = new Head(parameters);

		assertThat(head.getEnergyToChildren()).isEqualTo(1000);
	}

	@Test
	public void hasAVelocityOfEggs() {
		var parameters = new HeadParameters();
		parameters.setEggVelocity(42);
		var head = new Head(parameters);

		assertThat(head.getEggVelocity()).isEqualTo(42);
	}

	@Test
	public void hasAnIntervalForLayingEggs() {
		var parameters = new HeadParameters();
		parameters.setEggInterval(43);
		var head = new Head(parameters);

		assertThat(head.getEggInterval()).isEqualTo(43);
	}

	@Test
	public void hasAFiber() {
		var parameters = new HeadParameters();
		parameters.setRed(10);
		parameters.setGreen(20);
		parameters.setBlue(30);
		var head = new Head(parameters);

		assertThat(head.getFiber()).isEqualTo(new Fiber(10, 20, 30));
	}
}
