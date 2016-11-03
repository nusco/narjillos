package org.nusco.narjillos.application.views;

import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.geometry.Vector;

public class MicroscopeView {

	private final Viewport viewport;

	private Vector currentScreenSize = null;

	private Rectangle black;

	private Circle hole;

	private Shape microscope;

	public MicroscopeView(Viewport viewport) {
		this.viewport = viewport;
	}

	public Node toNode() {
		Vector screenSize = viewport.getSizeSC();
		if (screenSize.equals(currentScreenSize))
			return microscope;

		currentScreenSize = screenSize;

		double minScreenSize = Math.min(screenSize.x, screenSize.y);
		double maxScreenSize = Math.max(screenSize.x, screenSize.y);

		// Leave an ample left/bottom black margin - otherwise, the background
		// will be visible for a moment while enlarging the window.
		black = new Rectangle(-10, -10, maxScreenSize + 1000, maxScreenSize + 1000);

		hole = new Circle(screenSize.x / 2, screenSize.y / 2, minScreenSize / 2.03);
		microscope = Shape.subtract(black, hole);
		microscope.setEffect(new BoxBlur(5, 5, 1));

		return microscope;
	}
}
