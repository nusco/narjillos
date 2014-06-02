package org.nusco.swimmers.graphics;


import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.Parent;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.pond.Pond;

public class PondView extends Parent {

	private final Pond pond = new Pond();

	public List<Node> getThings() {
		List<Node> result = new LinkedList<>();
		Set<Object> swimmers = pond.getSwimmers();
		for (Object swimmer : swimmers)
			result.add(new SwimmerView((Swimmer)swimmer));
		// TODO: add food
		return result;
	}
	
	public void tick() {
//		pond.tick();
	}
}
