package org.nusco.narjillos.application.views;

import org.nusco.narjillos.application.utilities.NarjillosApplicationState;
import org.nusco.narjillos.application.utilities.Speed;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StringView {
	
	private final int fontSize;
	private final NarjillosApplicationState applicationState;

	public StringView(int fontSize, NarjillosApplicationState state) {
		this.fontSize = fontSize;
		this.applicationState = state;
	}

	public Node toNode(String message) {
		Text result = new Text(" " + message + getSlowMotionMessage());
		result.setFont(Font.font("HelveticaNeue-Bold", fontSize));
		result.setFill(Color.LIGHTGREEN);
		result.setTranslateY(fontSize);
		return result;
	}

	private String getSlowMotionMessage() {
		if (applicationState.getSpeed() == Speed.SLOW)
			return " (Slow Motion)";
		return "";
	}
}
