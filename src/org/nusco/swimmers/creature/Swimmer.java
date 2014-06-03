package org.nusco.swimmers.creature;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.physics.Vector;

public class Swimmer {

	private Head head;

	public Swimmer(Head head) {
		this.head = head;
	}

	public List<Organ> getParts() {
		List<Organ> result = new LinkedList<>();
		result.add(head);
		addChildrenDepthFirst(result, head);
		return result;
	}

	private void addChildrenDepthFirst(List<Organ> result, Organ organ) {
		for(Organ child : organ.getChildren()) {
			result.add(child);
			addChildrenDepthFirst(result, child);
		}
	}

	public Organ getHead() {
		return head;
	}
	
	public void tick() {
		// TODO: once I change this to a real direction/distance vector,
		// remember to remove AMPLITUDE from WaveNerve
		head.tick(Vector.ZERO_ONE);
	}
}
