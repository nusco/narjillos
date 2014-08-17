package org.nusco.narjillos.creature.body.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.body.BodySegment;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class ForceFieldTest {

	ForceField forceField = new ForceField();

	@Before
	public void setUpForces() {
		forceField.addForce(new Segment(Vector.ZERO, Vector.cartesian(3, 4)));
		forceField.addForce(new Segment(Vector.ZERO, Vector.cartesian(6, 0)));
		forceField.addForce(new Segment(Vector.ZERO, Vector.cartesian(0, 7)));
	}
	
	@Test
	public void recordsMovements() {
		Organ organ = new Head(1, 1, new ColorByte(0), 1);
		Organ child1 = organ.addChild(new BodySegment(2, 2, new ColorByte(0), organ, new DelayNerve(0), 0, 0));
		child1.addChild(new BodySegment(3, 3, new ColorByte(0), child1, new DelayNerve(0), 0, 0));

		final double[] masses = new double[3];
		ForceField recorder = new ForceField() {
			private int counter = 0;
			
			@Override
			public void calculateForce(Segment initialPositionInSpace, Segment finalPositionInSpace, double mass) {
				masses[counter++] = mass;
			}
		};
		
		organ.calculateForces(recorder);
		
		assertEquals(1, masses[0], 0.0);
		assertEquals(4, masses[1], 0.0);
		assertEquals(9, masses[2], 0.0);
	}
}
