package org.nusco.narjillos.application.views;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.nusco.narjillos.application.utilities.Speed;

public class AncestryStatusView {

	public Node toNode(long generation, long numberOfGenerations) {
		Text result = new Text("Generation " + generation + " of " + numberOfGenerations);
		result.setFont(Font.font("HelveticaNeue-Bold", 14));
		result.setFill(Speed.REALTIME.getViewColor());
		result.setTranslateX(5);
		result.setTranslateY(15);
		return result;
	}
}
