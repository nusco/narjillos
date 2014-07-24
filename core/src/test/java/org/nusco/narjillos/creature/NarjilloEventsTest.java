package org.nusco.narjillos.creature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class NarjilloEventsTest {

	Narjillo narjillo = new Narjillo(new Head(10, 10, new ColorByte(10), 1), DNA.random());

	class NullSwimmerEventListener implements NarjilloEventListener {
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
		narjillo.addEventListener(new NullSwimmerEventListener() {

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
		narjillo.addEventListener(new NullSwimmerEventListener() {

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
