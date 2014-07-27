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

//	private final DNA dna = DNA.random(DNA.CHROMOSOME_SIZE * 30);
	private final DNA dna = new DNA("{146_109_217_202_008_103}{154_205_105_044_179_157}{175_141_141_114_012_196}{034_250_237_041_147_044}{078_211_106_116_078_118}{057_247_180_236_185_124}{085_024_048_249_234_139}{254_089_119_185_217_110}{230_202_236_255_039_146}{152_217_130_159_030_079}");

	public static void main(String... args) throws Exception {
		MicroscopeEnvironment.programArguments = args;
		launch(args);
	}
	
	protected synchronized Pond createPond(String[] programArguments) {
		// in the future maybe make it more flexible, like PetriDish
		return new PrivatePond(dna);
	}

	@Override
	protected PrivatePond getPond() {
		return (PrivatePond)super.getPond();
	}
	
	protected String getTitle() {
		return "Narjillos - Isolation Chamber";
	}

	protected EventHandler<? super KeyEvent> createKeyboardHandler() {
		return null;
	}

	protected EventHandler<ScrollEvent> createMouseScrollHandler() {
		return null;
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
