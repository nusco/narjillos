package org.nusco.narjillos.creature.body;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.WaveNerve;

import static org.junit.Assert.*;

public class HeadTest extends ConnectedOrganTest {

	@Override
	public Head createConcreteOrgan(int length, int thickness) {
		return new Head(new HeadParameters(length, thickness));
	}

	@Override
	public void hasAParent() {
		// ...only it's null
        assertNull(getOrgan().getParent());
	}

	@Override
	public void hasAnEndPoint() {
		organ.growToAdultFormWithChildren();

		assertEquals(Vector.cartesian(50, 0), organ.getEndPoint());
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(Vector.ZERO, organ.getStartPoint());
	}

	@Test
	public void hasAWaveNerve() {
		Nerve nerve = new Head(new HeadParameters()).getNerve();

		assertEquals(WaveNerve.class, nerve.getClass());
	}

	@Test
	public void hasACenterOfMass() {
		Head head = new Head(new HeadParameters(10, 6));
		head.setAngleToParent(90);
		head.update();

		head.growToAdultFormWithChildren();

		assertTrue(head.getCenterOfMass().approximatelyEquals(Vector.cartesian(0, 5)));
	}

	@Test
	public void hasAMetabolicRate() {
		HeadParameters parameters = new HeadParameters();
		parameters.setMetabolicRate(2.1);
		Head head = new Head(parameters);

		assertEquals(2.1, head.getMetabolicRate(), 0.0);
	}

	@Test
	public void hasAWaveBeatRatio() {
		HeadParameters parameters = new HeadParameters();
		parameters.setWaveBeatRatio(2.3);
		Head head = new Head(parameters);

		assertEquals(2.3, head.getWaveBeatRatio(), 0.0);
	}

	@Test
	public void hasAByproduct() {
		HeadParameters parameters = new HeadParameters();
		parameters.setByproduct(Element.NITROGEN);
		Head head = new Head(parameters);

		assertEquals(Element.NITROGEN, head.getByproduct());
	}

	@Test
	public void hasAnAmountOfEnergyThatItPassesOnToChildren() {
		HeadParameters parameters = new HeadParameters();
		parameters.setEnergyToChildren(1000);
		Head head = new Head(parameters);

		assertEquals(1000, head.getEnergyToChildren(), 0.0);
	}

	@Test
	public void hasAVelocityOfEggs() {
		HeadParameters parameters = new HeadParameters();
		parameters.setEggVelocity(42);
		Head head = new Head(parameters);

		assertEquals(42, head.getEggVelocity());
	}

	@Test
	public void hasAnIntervalForLayingEggs() {
		HeadParameters parameters = new HeadParameters();
		parameters.setEggInterval(43);
		Head head = new Head(parameters);

		assertEquals(43, head.getEggInterval());
	}

	@Test
	public void hasAFiber() {
		HeadParameters parameters = new HeadParameters();
		parameters.setRed(10);
		parameters.setGreen(20);
		parameters.setBlue(30);
		Head head = new Head(parameters);

		assertEquals(new Fiber(10, 20, 30), head.getFiber());
	}
}
