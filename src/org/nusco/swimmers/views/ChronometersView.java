package org.nusco.swimmers.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.nusco.swimmers.shared.utilities.Chronometer;

public class ChronometersView {

	private final Chronometer ticksChronometer;
	private final Chronometer framesChronometer;

	public ChronometersView(Chronometer framesChronometer, Chronometer ticksChronometer) {
		this.ticksChronometer = ticksChronometer;
		this.framesChronometer = framesChronometer;
	}

	public Node toNode() {
		Text result = new Text(toString());
		result.setFont(Font.font ("Helvetica Light", 10));
		result.setFill(Color.RED);
		result.setTranslateX(2);
		result.setTranslateY(10);
		return result;
	}
	
	@Override
	public String toString() {
		return "FPS: " + framesChronometer.getTicksInLastSecond() + " / TPS: " + ticksChronometer.getTicksInLastSecond();
	}
}
