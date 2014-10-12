package org.nusco.narjillos.embryogenesis.bodyplan;

import java.util.LinkedList;

import org.nusco.narjillos.creature.body.Organ;

public class TreeBuilder {

	private final OrganBuilder[] builders;

	public TreeBuilder(OrganBuilder[] builders) {
		this.builders = builders;
	}

	public Organ buildTree() {
		return buildTree(null, getBuildersQueue());
	}

	private Organ buildTree(Organ parent, LinkedList<OrganBuilder> buildersQueue) {
		if (buildersQueue.isEmpty())
			return null;
		
		OrganBuilder nextBuilder = buildersQueue.pop();
		Organ result = nextBuilder.buildOrgan(parent);
		
		switch (nextBuilder.getInstruction()) {
		case CONTINUE:
			buildChild(result, buildersQueue);
			break;
		case BRANCH:
			buildChild(result, buildersQueue);
			buildChild(result, buildersQueue);
			break;
		case STOP:
			break;
		case MIRROR:
			break;
		}
		return result;
	}

	private void buildChild(Organ parent, LinkedList<OrganBuilder> buildersQueue) {
		Organ child = buildTree(parent, buildersQueue);
		if (child != null)
			parent.addChild(child);
	}

	private LinkedList<OrganBuilder> getBuildersQueue() {
		LinkedList<OrganBuilder> result = new LinkedList<>();
		for (int i = 0; i < builders.length; i++)
			result.add(builders[i]);
		return result;
	}	
}
