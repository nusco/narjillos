package org.nusco.swimmers.creature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;

public class SwimmerTest {

	Swimmer swimmer = new Swimmer(new Head(10, 10, 10), DNA.random());

	class NullSwimmerEventListener implements SwimmerEventListener {
		@Override
		public void moved(Segment movement) {
		}

		@Override
		public void died() {
		}
	}
	
	@Test
	public void sendsEventsWhenMoving() {
		swimmer.setPosition(Vector.ZERO);

		final Segment[] moved = new Segment[] { null };
		swimmer.addSwimmerEventListener(new NullSwimmerEventListener() {

			@Override
			public void moved(Segment movement) {
				moved[0] = movement;
			}
		});

		swimmer.setPosition(Vector.cartesian(10, 10));
		
		assertEquals(Vector.ZERO, moved[0].start);
		assertEquals(Vector.cartesian(10, 10), moved[0].end);
	}
	
	@Test
	public void sendsEventsWhenDying() {
		final boolean[] died = new boolean[] { false };
		swimmer.addSwimmerEventListener(new NullSwimmerEventListener() {

			@Override
			public void died() {
				died[0] = true;
			}
		});

		swimmer.decreaseEnergy(Swimmer.INITIAL_ENERGY);
		
		assertTrue(died[0]);
	}
	
	@Test
	public void hasLimitedEnergy() {
		swimmer.decreaseEnergy(Swimmer.INITIAL_ENERGY);
		
		assertEquals(0, swimmer.getEnergy(), 0.001);
	}
	
	@Test
	public void increasesEnergyByFeeding() {
		swimmer.feed();
		swimmer.decreaseEnergy(Swimmer.INITIAL_ENERGY);
		
		assertTrue(swimmer.getEnergy() > 0);
	}
}
