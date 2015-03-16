package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.nusco.narjillos.utilities.Effects;
import org.nusco.narjillos.utilities.Speed;

public class PetriStatusView {

	public Node toNode(int ticksInLastSecond, String environmentStatistics, String performanceStatistics, Speed speed, Effects effects, boolean locked, boolean isBusy) {
		String message = 	"FPS: " + ticksInLastSecond + " / " + performanceStatistics + "\n" +
							environmentStatistics + "\n" +
							getSpeedMessage(speed, effects) + "\n" +
							getModeMessage(locked) + "\n" +
							getBusyMessage(isBusy);

		Text result = new Text(message);
		result.setFont(Font.font("HelveticaNeue-Bold", 14));
		result.setFill(speed.getViewColor());
		result.setTranslateX(5);
		result.setTranslateY(15);
		return result;
	}

	private String getModeMessage(boolean locked) {
		if (locked)
			return "Mode: Follow";
		return "Mode: Freeroam";
	}

	private String getSpeedMessage(Speed speed, Effects effects) {
		String result = "Speed: " + speed.toString();
		if (effects == Effects.ON)
			return result;
		return result + " + no fx";
	}

	private String getBusyMessage(boolean isSaving) {
		if (!isSaving)
			return "";
		StringBuffer result = new StringBuffer("Busy");
		long halfSecondsCounter = Math.round(System.currentTimeMillis() / 500.0) % 4;
		for (int i = 0; i < halfSecondsCounter; i++)
			result.append(".");
		return result.toString();
	}
}
