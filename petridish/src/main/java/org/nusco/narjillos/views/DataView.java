package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class DataView {

	public static Node toNode(String message, Color color) {
		Text result = new Text(message);
		result.setFont(Font.font("Helvetica-Bold", 14));
		result.setFill(color);
		result.setTranslateX(5);
		result.setTranslateY(15);
		return result;
	}
}
