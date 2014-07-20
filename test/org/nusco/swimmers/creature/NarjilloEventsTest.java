package org.nusco.swimmers.creature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.shared.physics.Segment;
import org.nusco.swimmers.shared.physics.Vector;

public class NarjilloEventsTest {

	Narjillo narjillo = new Narjillo(new Head(10, 10, 10, 1), DNA.random());

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
		narjillo.setPosition(Vector.ZERO);

		final Segment[] moved = new Segment[] { null };
		narjillo.addSwimmerEventListener(new NullSwimmerEventListener() {

			@Override
			public void moved(Segment movement) {
				moved[0] = movement;
			}
		});

		narjillo.tick();
		
		assertNotNull(moved[0]);
	}
	
	@Test
	public void sendsEventsWhenDying() {
		final boolean[] died = new boolean[] { false };
		narjillo.addSwimmerEventListener(new NullSwimmerEventListener() {

			@Override
			public void died() {
				died[0] = true;
			}
		});

		narjillo.decreaseEnergy(Narjillo.INITIAL_ENERGY);
		
		assertTrue(died[0]);
	}
	
	@Test
	public void hasLimitedEnergy() {
		narjillo.decreaseEnergy(Narjillo.INITIAL_ENERGY);
		
		assertEquals(0, narjillo.getEnergy(), 0.001);
	}
	
	@Test
	public void increasesEnergyByFeeding() {
		narjillo.feed();
		narjillo.decreaseEnergy(Narjillo.INITIAL_ENERGY);
		
		assertTrue(narjillo.getEnergy() > 0);
	}
}
