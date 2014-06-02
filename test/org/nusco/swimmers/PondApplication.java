package org.nusco.swimmers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.nusco.swimmers.graphics.PondView;

public class PondApplication extends Application {

//	private DNA currentDNA = DNA.random();

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Swimmers");

		final Group root = new Group();
		final PondView[] pond = new PondView[]{ updatePond() };
		
		root.getChildren().addAll(pond[0].getThings());
		Scene scene = new Scene(root, 800, 800);
		
		scene.addEventFilter(MouseEvent.MOUSE_MOVED, 
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	pond[0].tick();
                    	root.getChildren().clear();
                    	root.getChildren().addAll(pond[0].getThings());
                    };
                });
		
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, 
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	pond[0] = updatePond();
                    	root.getChildren().clear();
                    	root.getChildren().addAll(pond[0].getThings());
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

	private PondView updatePond() {
		return new PondView();
	}

	public static void main(String... args) {
		launch(args);
	}
 }

