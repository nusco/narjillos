package org.nusco.swimmer.graphics;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;

import org.nusco.swimmer.Swimmer;
import org.nusco.swimmer.body.Organ;

public class SwimmerView extends Parent {

	private final Swimmer swimmer;

	public SwimmerView(Swimmer swimmer) {
		this.swimmer = swimmer;
	}

	public List<Node> getParts() {
		List<Node> result = new LinkedList<>();
		addWithChildren(result, swimmer.getHead());
		return result;
	}

	private void addWithChildren(List<Node> result, Organ organ) {
		Node shape = new OrganView(organ).toShape();
		if(shape != null)
			result.add(shape);
		for(Organ child : organ.getChildren())
			addWithChildren(result, child);
	}

	public void tick() {
		swimmer.tick();
	}
}
