package org.nusco.swimmers.body;

import java.util.LinkedList;
import java.util.List;

public class Swimmer {

	private VisibleOrgan head;

	public Swimmer(VisibleOrgan head) {
		this.head = head;
	}

	public List<Organ> getParts() {
		List<Organ> result = new LinkedList<>();
		result.add(head);
		addChildrenBreadthFirst(result, head);
		return result;
	}

	private void addChildrenBreadthFirst(List<Organ> result, Organ organ) {
		for(Organ child : organ.getChildren())
			result.add(child);
		for(Organ child : organ.getChildren())
			addChildrenBreadthFirst(result, child);
	}

	public Organ getHead() {
		return head;
	}
}
