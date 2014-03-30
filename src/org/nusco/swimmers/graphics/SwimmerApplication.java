package org.nusco.swimmers.graphics;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.nusco.swimmers.body.Swimmer;
import org.nusco.swimmers.genetics.DNA;

public class SwimmerApplication extends Application {

	private DNA currentDNA = DNA.random();
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Swimmer");

		final Group root = new Group();
		root.getChildren().addAll(createDefaultSwimmerBody().getParts());
		Scene scene = new Scene(root, 800, 800);
		
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, 
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	root.getChildren().clear();
                    	root.getChildren().addAll(createNextSwimmerBody().getParts());
                    };
                });

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private SwimmerBody createDefaultSwimmerBody() {
		return new SwimmerBody(DNA.sample().toPhenotype());
	}

	private SwimmerBody createNextSwimmerBody() {
		Swimmer swimmer = currentDNA.toPhenotype();
		currentDNA = currentDNA.mutate();
		return new SwimmerBody(swimmer);
	}

	public static void main(String... args) {
		launch(args);
	}
}
