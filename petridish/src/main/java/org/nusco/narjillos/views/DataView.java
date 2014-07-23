package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class DataView {

	public static Node toNode(String message) {
		Text result = new Text(message);
		result.setFont(Font.font ("Helvetica Light", 10));
		result.setFill(Color.RED);
		result.setTranslateX(2);
		result.setTranslateY(10);
		return result;
	}
}
