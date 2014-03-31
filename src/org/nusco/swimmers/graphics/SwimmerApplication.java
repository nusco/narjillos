package org.nusco.swimmers.graphics;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.nusco.swimmers.body.Swimmer;
import org.nusco.swimmers.genetics.DNA;
import org.nusco.swimmers.genetics.Embryo;

public class SwimmerApplication extends Application {

	private DNA currentDNA = DNA.sample();

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Swimmer");

		final Group root = new Group();
		final SwimmerBody[] swimmer = new SwimmerBody[]{ updateSwimmerBody() };
		
		root.getChildren().addAll(swimmer[0].getParts());
		Scene scene = new Scene(root, 800, 800);
		
		scene.addEventFilter(MouseEvent.MOUSE_MOVED, 
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	swimmer[0].tick();
                    	root.getChildren().clear();
                    	root.getChildren().addAll(swimmer[0].getParts());
                    };
                });
		
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, 
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	swimmer[0] = updateSwimmerBody();
                    	root.getChildren().clear();
                    	root.getChildren().addAll(swimmer[0].getParts());
                    };
                });
        
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
    public static void run(Runnable treatment) {
        if(treatment == null) throw new IllegalArgumentException("The treatment to perform can not be null");
 
        if(Platform.isFxApplicationThread()) treatment.run();
        else Platform.runLater(treatment);
    }

	private SwimmerBody updateSwimmerBody() {
		Swimmer swimmer = new Embryo(currentDNA).develop();
		currentDNA = currentDNA.mutate();
		return new SwimmerBody(swimmer);
	}

	public static void main(String... args) {
		launch(args);
	}
 }

