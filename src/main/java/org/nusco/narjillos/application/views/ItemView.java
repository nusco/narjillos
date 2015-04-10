package org.nusco.narjillos.application.views;

import javafx.scene.Node;

import org.nusco.narjillos.application.utilities.Viewport;

public interface ItemView {
	
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn);
	public boolean isVisible(Viewport viewport);
}
