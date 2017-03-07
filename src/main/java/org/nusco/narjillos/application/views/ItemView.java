package org.nusco.narjillos.application.views;

import javafx.scene.Node;

import org.nusco.narjillos.application.utilities.Viewport;

public interface ItemView {

	Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn);

	boolean isVisible(Viewport viewport);
}
