package org.nusco.swimmer.body.pns;

public class NerveBuilder {
	public static Nerve createDelayNerve(int delay) {
		return new DelayNerve(delay);
	}

	public static Nerve createPassNerve() {
		return new PassNerve();
	}

	public static Nerve createWaveNerve() {
		return new WaveNerve();
	}
}
