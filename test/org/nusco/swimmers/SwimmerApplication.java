package org.nusco.swimmers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;
import org.nusco.swimmers.graphics.SwimmerView;
import org.nusco.swimmers.physics.Vector;

public class SwimmerApplication extends Application {

	private DNA currentDNA = DNA.ancestor();
	
	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Swimmer");

		final Group root = new Group();
		final SwimmerView[] swimmer = new SwimmerView[]{ updateSwimmerBody() };
    	swimmer[0].setCurrentTarget(generateRandomTarget());
    	showSwimmer(root, swimmer);

		Scene scene = new Scene(root, 1200, 800);

		scene.addEventFilter(MouseEvent.MOUSE_MOVED, 
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	swimmer[0].tick();
                    	showSwimmer(root, swimmer);
                    }
                });
		
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, 
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	if(event.getButton() == MouseButton.PRIMARY)
                    		swimmer[0] = updateSwimmerBody();
                    	swimmer[0].setCurrentTarget(generateRandomTarget());
                    	showSwimmer(root, swimmer);
                    };
                });
        
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void showSwimmer(final Group root,
			final SwimmerView[] swimmer) {
		root.getChildren().clear();
    	root.getChildren().addAll(swimmer[0].getParts());
    	root.getChildren().add(swimmer[0].getTarget());
	}

    private Vector generateRandomTarget() {
    	double randomAngle = Math.random() * 360 - 180;
    	// TODO
    	return Vector.polar(180, 1);
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

