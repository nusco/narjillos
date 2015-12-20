package org.nusco.narjillos.application.views;

import javafx.scene.Node;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.application.utilities.Viewport;

class ViewportTransformer {

	public static void applyTransforms(Node node, Viewport viewport) {
		node.getTransforms().clear();
		node.getTransforms().add(new Translate(-viewport.getPositionEC().x, -viewport.getPositionEC().y));
		node.getTransforms().add(
				new Scale(viewport.getZoomLevel(), viewport.getZoomLevel(), viewport.getPositionEC().x, viewport.getPositionEC().y));
	}
}
