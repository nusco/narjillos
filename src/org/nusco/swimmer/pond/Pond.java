package org.nusco.swimmer.pond;

import java.util.HashMap;
import java.util.Map;

import org.nusco.swimmer.Swimmer;
import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.things.Food;

public class Pond {

	private Map<Object, Vector> foodToPositions = new HashMap<>();
	private Map<Object, Vector> swimmersToPositions = new HashMap<>();

	public void add(Food food, int x, int y) {
		foodToPositions.put(food, Vector.cartesian(x, y));
	}

	public void add(Swimmer swimmer, int x, int y) {
		swimmersToPositions.put(swimmer, Vector.cartesian(x, y));
	}

	public Vector closestFoodTo(Vector position) {
		return closestThingTo(position, foodToPositions);
	}

	public Vector closestSwimmerTo(Vector position) {
		return closestThingTo(position, swimmersToPositions);
	}

	private Vector closestThingTo(Vector position, Map<Object, Vector> things) {
		double minDistance = Double.MAX_VALUE;
		Vector result = Vector.ZERO;
		for (Object thing : things.keySet()) {
			Vector target = things.get(thing);
			double distance = target.minus(position).getLength();
			if(distance < minDistance) {
				minDistance = distance;
				result = target;
			}
		};
		return result;
	}
}
