package org.nusco.swimmers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.creature.genetics.Embryo;
import org.nusco.swimmers.graphics.SwimmerView;
import org.nusco.swimmers.physics.Vector;

public class PondApplication extends Application {

    private DNA currentDNA = DNA.random();

    public static void main(String... args) {
	launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws InterruptedException {
	primaryStage.setTitle("Swimmer");

	final Group root = new Group();
	final SwimmerView[] swimmer = new SwimmerView[] { updateSwimmerBody() };
	swimmer[0].setCurrentTarget(Vector.polar(180, 3));
	showSwimmer(root, swimmer);

	final Scene scene = new Scene(root, 1200, 800);

	scene.addEventFilter(MouseEvent.MOUSE_CLICKED, createMouseEvent(swimmer));

	startModelUpdateThread(swimmer);
	startViewUpdateThread(root, swimmer);

	primaryStage.setScene(scene);
	primaryStage.show();
    }

    private EventHandler<MouseEvent> createMouseEvent(final SwimmerView[] swimmer) {
	return new EventHandler<MouseEvent>() {
	    public void handle(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY)
		    mutate(swimmer);
		else
		    setRandomTarget(swimmer);
	    }

	    private void setRandomTarget(final SwimmerView[] swimmer) {
		double randomAngle = Math.random() * 360 - 180;
		double randomLength = Math.random() * 3 + 2;
		Vector newTarget = Vector.polar(randomAngle, randomLength);
		swimmer[0].setCurrentTarget(newTarget);
	    }
	    
	    private void mutate(final SwimmerView[] swimmer) {
		swimmer[0] = updateSwimmerBody();
		swimmer[0].setCurrentTarget(Vector.polar(180, 3));
	    }
	};
    }

    private void startViewUpdateThread(final Group root, final SwimmerView[] swimmer) {
	Task<Void> task = new Task<Void>() {
	    @Override
	    public Void call() throws Exception {
		while (true) {
		    Platform.runLater(new Runnable() {
			@Override
			public void run() {
			    showSwimmer(root, swimmer);
			}
		    });
		    Thread.sleep(10);
		}
	    }
	};
	Thread updateGUI = new Thread(task);
	updateGUI.setDaemon(true);
	updateGUI.start();
    }

    private void startModelUpdateThread(final SwimmerView[] swimmer) {
	Thread updateThread = new Thread() {
	    @Override
	    public void run() {
		while (true) {
		    swimmer[0].tick();
		    try {
			Thread.sleep(30);
		    } catch (InterruptedException e) {
		    }
		}
	    };
	};
	updateThread.setDaemon(true);
	updateThread.start();
    }

    private void showSwimmer(Group root, SwimmerView[] swimmer) {
	root.getChildren().clear();
	root.getChildren().addAll(swimmer[0].getParts());
	root.getChildren().add(swimmer[0].getMouth());
    }

    private SwimmerView updateSwimmerBody() {
	Swimmer swimmer = new Embryo(currentDNA).develop();
	swimmer.placeAt(Vector.cartesian(400, 0));
	currentDNA = currentDNA.mutate();
	return new SwimmerView(swimmer);
    }
}
