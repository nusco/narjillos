package org.nusco.narjillos;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.ecosystem.SmallDrop;
import org.nusco.narjillos.shared.physics.Vector;

public class IsolationChamber extends MicroscopeEnvironment {

	public static void main(String... args) throws Exception {
		MicroscopeEnvironment.programArguments = args;
		launch(args);
	}
	
	protected synchronized Ecosystem createEcosystem(String[] programArguments) {
		// TODO: maybe make it more flexible, like PetriDish
		DNA randomDNA = DNA.random(DNA.CHROMOSOME_SIZE * 10);
		System.out.println(randomDNA);
		return new SmallDrop(randomDNA);
	}

	@Override
	protected SmallDrop getEcosystem() {
		return (SmallDrop)super.getEcosystem();
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
					getEcosystem().replaceNarjillo();
					return;
				}
				
				Vector clickedPoint = Vector.cartesian(event.getSceneX(), event.getSceneY());
				Vector locationInPond = getViewport().toPC(clickedPoint);
				getEcosystem().replaceFood(locationInPond);
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
