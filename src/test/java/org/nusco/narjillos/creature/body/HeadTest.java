package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.ecosystem.chemistry.Element;

public class HeadTest extends ConnectedOrganTest {

	@Override
	public Head createConcreteOrgan(int length, int thickness) {
		return new Head(length, thickness, 100, 101, 102, 1, Element.OXYGEN, 0.5, 1, 0);
	}

	@Override
	public void hasAParent() {
		// ...only it's null
		assertEquals(null, getOrgan().getParent());
	}

	@Override
	public void hasAnEndPoint() {
		fullyGrow(organ);
		
		assertEquals(Vector.cartesian(50, 0), organ.getEndPoint());
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(Vector.ZERO, organ.getStartPoint());
	}

	@Test
	public void hasAWaveNerve() {
		Nerve nerve = new Head(0, 0, 0, 0, 0, 1, Element.OXYGEN, 0.5, 30, 0).getNerve();
				
		assertEquals(WaveNerve.class, nerve.getClass());
	}
	
	@Test
	public void hasACenterOfMass() {
		Head head = new Head(10, 6, 0, 0, 0, 1, Element.OXYGEN, 0.5, 1, 0);
		head.setAngleToParent(90);
		head.updateGeometry();
		
		fullyGrow(head);
		
		assertTrue(head.getCenterOfMass().almostEquals(Vector.cartesian(0, 5)));
	}
	
	@Test
	public void hasAByproduct() {
		Head head = new Head(10, 6, 0, 0, 0, 1, Element.NITROGEN, 1000, 1, 0);
		
		assertEquals(Element.NITROGEN, head.getByproduct());
	}
	
	@Test
	public void hasAnAmountOfEnergyThatItPassesOnToChildren() {
		Head head = new Head(10, 6, 0, 0, 0, 1, Element.OXYGEN, 1000, 1, 0);
		
		assertEquals(1000, head.getEnergyToChildren(), 0.0);
	}
	
	@Test
	public void hasAVelocityOfEggs() {
		Head head = new Head(10, 6, 0, 0, 0, 1, Element.OXYGEN, 0.42, 42, 0);
		
		assertEquals(42, head.getEggVelocity());
	}
	
	@Test
	public void hasAnIntervalForLayingEggs() {
		Head head = new Head(10, 6, 0, 0, 0, 1, Element.OXYGEN, 0.42, 42, 43);
		
		assertEquals(43, head.getEggInterval());
	}
	
	@Test
	public void hasAFiber() {
		Head head = new Head(10, 6, 10, 20, 30, 1, Element.OXYGEN, 0.42, 30, 0);
		
		assertEquals(new Fiber(10, 20, 30), head.getFiber());
	}
}
