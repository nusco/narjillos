package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public abstract class OrganTest {

	protected static int THICKNESS = 8;
	
	protected Organ organ;

	@Before
	public void setUpPart() {
		organ = createOrgan();
	}

	public abstract Organ createOrgan();

	@Test
	public void hasALength() {
		assertEquals(20, organ.getLength());
	}

	@Test
	public void hasAThickness() {
		assertEquals(THICKNESS, organ.getThickness());
	}

	@Test
	public abstract void hasAnEndPoint();

	@Test
	public abstract void hasAParent();

	@Test
	public void hasAnRGBValue() {
		assertEquals(100, organ.getRGB());
	}
	
	@Test
	public void hasAnEmptyListOfChildPartsByDefault() {
		assertEquals(Collections.EMPTY_LIST, organ.getChildren());
	}

	@Test
	public void canSproutVisibleOrgans() {
		Organ child = organ.sproutOrgan(20, 12, 45, 100);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
		assertEquals(45, child.getRelativeAngle(), 0);
	}
	
	@Test
	public void knowsItsChildren() {
		Organ child1 = organ.sproutOrgan(20, THICKNESS, 45, 100);
		Organ child2 = organ.sproutOrgan(20, THICKNESS, 45, 100);

		List<Organ> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, organ.getChildren());
	}
	
	@Test
	public void sendsNerveSignalsToItsChildren() {
		
		Organ child1 = organ.sproutOrgan(20, THICKNESS, 45, 100);
		Organ child2 = organ.sproutOrgan(20, THICKNESS, 45, 100);
		
		organ.setNerve(new CounterNerve());
		child1.setNerve(new CounterNerve());
		child2.setNerve(new CounterNerve());
		
		organ.tick(Vector.ZERO_ONE);
		
		assertEquals(2, organ.getNerve().getOutputSignal().getLength(), 0.0);
		assertEquals(3, child1.getNerve().getOutputSignal().getLength(), 0.0);
		assertEquals(3, child2.getNerve().getOutputSignal().getLength(), 0.0);
	}
	
	class CounterNerve extends Nerve {
		@Override
		public Vector process(Vector inputSignal) {
			return Vector.polar(0, inputSignal.getLength() + 1);
		}		
	}
}
