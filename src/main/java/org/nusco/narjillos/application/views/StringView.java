package org.nusco.narjillos.application.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StringView {
	
	private final int fontSize;

	public StringView(int fontSize) {
		this.fontSize = fontSize;
	}

	public Node toNode(String message) {
		Text result = new Text(" " + message);
		result.setFont(Font.font("HelveticaNeue-Bold", fontSize));
		result.setFill(Color.LIGHTGREEN);
		result.setTranslateY(fontSize);
		return result;
	}
}
