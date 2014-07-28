package org.nusco.narjillos;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.pond.PrivatePond;
import org.nusco.narjillos.shared.physics.Vector;

public class IsolationChamber extends MicroscopeEnvironment {

	public static void main(String... args) throws Exception {
		MicroscopeEnvironment.programArguments = args;
		launch(args);
	}
	
	protected synchronized Pond createPond(String[] programArguments) {
		// TODO: maybe make it more flexible, like PetriDish
		DNA randomDNA = DNA.random(DNA.CHROMOSOME_SIZE * 10);
		System.out.println(randomDNA);
		return new PrivatePond(randomDNA);
	}

	@Override
	protected PrivatePond getPond() {
		return (PrivatePond)super.getPond();
	}
	
	protected String getTitle() {
		return "Narjillos - Isolation Chamber";
	}

	protected EventHandler<KeyEvent> createKeyboardHandler() {
		return new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
			}
		};
	}

	protected EventHandler<ScrollEvent> createMouseScrollHandler() {
		return new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
			}};
	}

	protected EventHandler<MouseEvent> createMouseEvent() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					getPond().replaceNarjillo();
					return;
				}
				
				Vector clickedPoint = Vector.cartesian(event.getSceneX(), event.getSceneY());
				Vector locationInPond = getViewport().toPC(clickedPoint);
				getPond().replaceFood(locationInPond);
			}
		};
	}

	protected void afterModelTick() {
	}
	
	protected void afterViewTick() {
	}

	protected Node getEnvironmentSpecificOverlay() {
		return null;
	}
}
