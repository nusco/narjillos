package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.nusco.narjillos.Lab;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.NumberFormat;
import org.nusco.narjillos.utilities.Speed;

public class DataView {

	private final Lab lab;

	public DataView(Lab lab) {
		this.lab = lab;
	}

	public Node toNode(Speed speed, Chronometer chronometer, boolean locked) {
		String message = 	getPerformanceMessage(speed, chronometer) + "\n" +
							getStatisticsMessage() + "\n" +
							getModeMessage(locked);

		Text result = new Text(message);
		result.setFont(Font.font("Helvetica-Bold", 14));
		result.setFill(speed.getViewColor());
		result.setTranslateX(5);
		result.setTranslateY(15);
		return result;
	}

	private String getStatisticsMessage() {
		return "NARJ: " + getEcosystem().getNumberOfNarjillos() + " / EGGS: " + getEcosystem().getNumberOfEggs() + " / FOOD: "
				+ getEcosystem().getNumberOfFoodPieces();
	}

	private String getPerformanceMessage(Speed speed, Chronometer chronometer) {
		return "FPS: " + chronometer.getTicksInLastSecond() + " / TPS: " + lab.getTicksInLastSecond() + " / TICKS: "
				+ NumberFormat.format(lab.getTotalTicks()) + " (" + getStateString(speed) + ")";
	}

	private String getModeMessage(boolean locked) {
		if (locked)
			return "Mode: Follow";
		return "Mode: Freeroam";
	}

	private String getStateString(Speed speed) {
		return speed.toString();
	}

	private Ecosystem getEcosystem() {
		return lab.getEcosystem();
	}
}
