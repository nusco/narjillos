package org.nusco.narjillos.application.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StringView {

	public Node toNode(String message) {
		Text result = new Text(message);
		result.setFont(Font.font("HelveticaNeue-Bold", 14));
		result.setFill(Color.LIGHTGREEN);
		result.setTranslateX(5);
		result.setTranslateY(15);
		return result;
	}
}
