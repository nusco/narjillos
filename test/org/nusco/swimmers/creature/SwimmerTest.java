package org.nusco.swimmers.creature;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.swimmers.creature.body.Head;

public class SwimmerTest {

	@Test
	public void sendsEventsWhenDying() {
		Swimmer swimmer = new Swimmer(new Head(10, 10, 10));
		final boolean[] died = new boolean[] { false };
		swimmer.addLifecycleEventListener(new LifecycleEventListener() {

			@Override
			public void died() {
				died[0] = true;
			}
		});

		swimmer.decreaseEnergy(Swimmer.INITIAL_ENERGY);
		
		assertTrue(died[0]);
	}
}
