package org.nusco.swimmer;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmer.body.Organ;

public class Swimmer {

	private Organ head;

	public Swimmer(Organ head) {
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
		head.tick();
	}
}
