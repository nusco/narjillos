package org.nusco.swimmers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;
import org.nusco.swimmers.graphics.SwimmerView;

public class SwimmerApplication extends Application {

	private DNA currentDNA = DNA.random();

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Swimmer");

		final Group root = new Group();
		final SwimmerView[] swimmer = new SwimmerView[]{ updateSwimmerBody() };
		
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

	private SwimmerView updateSwimmerBody() {
		Swimmer swimmer = new Embryo(currentDNA).develop();
		currentDNA = currentDNA.mutate();
		return new SwimmerView(swimmer);
	}

	public static void main(String... args) {
		launch(args);
	}
 }

