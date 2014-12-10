package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.nusco.narjillos.Lab;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.NumberFormat;
import org.nusco.narjillos.utilities.Speed;

public class StatusBarView {

	private final Lab lab;

	public StatusBarView(Lab lab) {
		this.lab = lab;
	}

	public Node toNode(Speed speed, Chronometer chronometer, boolean locked, boolean isSaving) {
		String message = 	getStatisticsMessage() + "\n" +
							getPerformanceMessage(speed, chronometer) + "\n" +
							getSpeedMessage(speed) + "\n" +
							getModeMessage(locked) + "\n" +
							getSavingMessage(isSaving);

		Text result = new Text(message);
		result.setFont(Font.font("HelveticaNeue-Bold", 14));
		result.setFill(speed.getViewColor());
		result.setTranslateX(5);
		result.setTranslateY(15);
		return result;
	}

	private String getStatisticsMessage() {
		return "Narj: " + getEcosystem().getNumberOfNarjillos() + " / Eggs: " + getEcosystem().getNumberOfEggs() + " / Food: "
				+ getEcosystem().getNumberOfFoodPieces();
	}

	private String getPerformanceMessage(Speed speed, Chronometer chronometer) {
		return "FPS: " + chronometer.getTicksInLastSecond() + " / TPS: " + lab.getTicksInLastSecond() + " / Ticks: "
				+ NumberFormat.format(lab.getTotalTicks());
	}

	private String getModeMessage(boolean locked) {
		if (locked)
			return "Mode: Follow";
		return "Mode: Freeroam";
	}

	private String getSpeedMessage(Speed speed) {
		return "Speed: " + speed.toString();
	}

	private String getSavingMessage(boolean isSaving) {
		if (!isSaving)
			return "";
		StringBuffer result = new StringBuffer("Saving");
		long halfSecondsCounter = Math.round(System.currentTimeMillis() / 500.0) % 4;
		for (int i = 0; i < halfSecondsCounter; i++)
			result.append(".");
		return result.toString();
	}

	private Ecosystem getEcosystem() {
		return lab.getEcosystem();
	}
}
