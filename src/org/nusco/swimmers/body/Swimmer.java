package org.nusco.swimmers.body;

import java.util.LinkedList;
import java.util.List;

public class Swimmer {

	private Part head;

	public Swimmer(Part head) {
		this.head = head;
	}

	public List<Part> getParts() {
		List<Part> result = new LinkedList<>();
		result.add(head);
		addChildrenBreadthFirst(result, head);
		return result;
	}

	private void addChildrenBreadthFirst(List<Part> result, Part part) {
		for(Part child : part.getChildren())
			result.add(child);
		for(Part child : part.getChildren())
			addChildrenBreadthFirst(result, child);
	}
}
