package org.nusco.narjillos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.pond.Drop;
import org.nusco.narjillos.pond.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.NumberFormat;
import org.nusco.narjillos.shared.utilities.RanGen;
import org.nusco.narjillos.views.DataView;

public class PetriDish extends MicroscopeEnvironment {

	private long numberOfTicks = 0;
	private final Chronometer ticksChronometer = new Chronometer();
	private final Chronometer framesChronometer = new Chronometer();

	private int targetTicksPerSecond = DEFAULT_TICKS_PER_SECOND;
	
	public static void main(String... args) throws Exception {
		MicroscopeEnvironment.programArguments = args;
		launch(args);
	}
	
	protected synchronized Ecosystem createEcosystem(String[] programArguments) {
		if (programArguments.length == 0)
			return new Drop();
		
		String argument = programArguments[0];

		boolean isInteger = isInteger(argument);
		if(isInteger) {
			int seed = Integer.parseInt(argument);
			RanGen.seed(seed);
			return new Drop();
		}
		
		if(argument.endsWith(".nrj"))
			return new Drop(readDNAFromFile(argument));
		
		return new Drop(new DNA(argument));
	}

	private boolean isInteger(String argument) {
		try {
			Integer.parseInt(argument);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static DNA readDNAFromFile(String file) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(file));
			StringBuffer result = new StringBuffer();
			for (String line : lines)
				result.append(line + "\n");
			return new DNA(result.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected String getTitle() {
		return "Narjillos - Petri Dish";
	}

	protected EventHandler<ScrollEvent> createMouseScrollHandler() {
		return new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0)
					getViewport().zoomOut();
				else
					getViewport().zoomIn();
			}
		};
	}

	protected EventHandler<MouseEvent> createMouseEvent() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					toggleMaxSpeed();
					return;
				}
				
				Vector clickedPoint = Vector.cartesian(event.getSceneX(), event.getSceneY());
				getViewport().flyToTargetSC(clickedPoint);
				
				if (event.getClickCount() > 1)
					getViewport().flyToNextZoomCloseupLevel();
			}
		};
	}

	protected void afterModelTick() {
		numberOfTicks++;
		ticksChronometer.tick();
	}
	
	protected void afterViewTick() {
		framesChronometer.tick();
	}

	protected Node getEnvironmentSpecificOverlay() {
		return getChronometers();
	}

	private synchronized void toggleMaxSpeed() {
		if (targetTicksPerSecond == 1000)
			targetTicksPerSecond = DEFAULT_TICKS_PER_SECOND;
		else
			targetTicksPerSecond = 1000;
	}

	@Override
	protected synchronized int getTicksPeriod() {
		return 1000 / targetTicksPerSecond;
	}

	private synchronized Node getChronometers() {
		Color color = isRealTime() ? Color.LIGHTGREEN : Color.HOTPINK;
		return DataView.toNode(getPerformanceMessage() + "\n" + getStatisticsMessage(), color);
	}

	private String getStatisticsMessage() {
		return "NARJ: " + getEcosystem().getNumberOfNarjillos() + " / FOOD: " + getEcosystem().getNumberOfFoodPieces();
	}

	private String getPerformanceMessage() {
		String result = "FPS: " + framesChronometer.getTicksInLastSecond() +
						" / TPS: " + ticksChronometer.getTicksInLastSecond() +
						" / TICKS: " + NumberFormat.format(numberOfTicks);
		if (isRealTime())
			return result + " (realtime)";
		else
			return result + " (hi-speed)";
	}

	private boolean isRealTime() {
		return targetTicksPerSecond == DEFAULT_TICKS_PER_SECOND;
	}
}
