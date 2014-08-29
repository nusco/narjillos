package org.nusco.narjillos.creature.body.pns;

import java.util.LinkedList;

import com.google.gson.Gson;

/**
 * Outputs the same signal it receives as an input, delayed by a given number of ticks.
 */
public class DelayNerve implements Nerve {

	private final int delay;
	private final LinkedList<Double> buffer = new LinkedList<>();

	public DelayNerve(int delay) {
		this.delay = delay;
	}

	@Override
	public double tick(double inputSignal) {
		buffer.add(inputSignal);
		if(buffer.size() < delay + 1)
			return 0;
		return buffer.pop();
	}

	public LinkedList<Double> getBuffer() {
		return buffer;
	}
	
	public static void main(String[] args) {
		Nerve delayNerve = new DelayNerve(4);
		delayNerve.tick(1);
		delayNerve.tick(2);
		delayNerve.tick(3);
		String json = new Gson().toJson(delayNerve);
		System.out.println(json);
		
		DelayNerve fromJson = new Gson().fromJson(json, DelayNerve.class);
		for (int i = 0; i < 10; i++)
			System.out.println(fromJson.tick(0));
	}
}
