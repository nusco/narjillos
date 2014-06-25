package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.creature.body.pns.WaveNerve;

public class NeckTest extends ConnectiveTissueTest {

	protected ConnectiveTissue createConnectiveTissue() {
		return new Neck(null);
	}

	@Test
	public void hasAWaveNerve() {
		Nerve nerve = createConnectiveTissue().getNerve();
		
		assertEquals(WaveNerve.class, nerve.getClass());
	}
}
