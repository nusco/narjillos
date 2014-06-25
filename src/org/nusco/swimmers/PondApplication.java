package org.nusco.swimmers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
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

public class PondApplication extends Application {

	private DNA currentDNA = DNA.random();
	private final SwimmerView[] swimmerViewContainer = new SwimmerView[1];
	
	public static void main(String... args) {
		launch(args);
	}

	private synchronized void setSwimmerView(SwimmerView swimmerView) {
		swimmerViewContainer[0] = swimmerView;
	}

	private synchronized SwimmerView getSwimmerView() {
		return swimmerViewContainer[0];
	}
	
	@Override
	public void start(final Stage primaryStage) throws InterruptedException {
		primaryStage.setTitle("Swimmer");

		final Group root = new Group();
		setSwimmerView(createNewSwimmer());
		getSwimmerView().setCurrentTarget(Vector.polar(180, 3));
		showSwimmer(root);

		final Scene scene = new Scene(root, 1200, 800);

		startModelUpdateThread();
		startViewUpdateThread(root);
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, createMouseEvent(getSwimmerView()));

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private EventHandler<MouseEvent> createMouseEvent(final SwimmerView swimmer) {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				handleMouse(swimmer, event);
			}

			private synchronized void handleMouse(final SwimmerView swimmer, MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY)
					mutate(swimmer);
				else
					setRandomTarget(swimmer);
			}
		};
	}

	private void startViewUpdateThread(final Group root) {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				while (true) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							showSwimmer(root);
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

	private void startModelUpdateThread() {
		Thread updateThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					tick();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		updateThread.setDaemon(true);
		updateThread.start();
	}
	
	private synchronized void tick() {
		getSwimmerView().tick();
	};

	private synchronized void setRandomTarget(final SwimmerView swimmer) {
		double randomAngle = Math.random() * 360 - 180;
		double randomLength = Math.random() * 3 + 2;
		Vector newTarget = Vector.polar(randomAngle, randomLength);
		getSwimmerView().setCurrentTarget(newTarget);
	}

	private synchronized void mutate(final SwimmerView swimmer) {
		setSwimmerView(createNewSwimmer());
		getSwimmerView().setCurrentTarget(Vector.polar(180, 3));
	}

	private synchronized SwimmerView createNewSwimmer() {
		Swimmer swimmer = new Embryo(currentDNA).develop();
		swimmer.placeAt(Vector.cartesian(400, 0));
		currentDNA = currentDNA.mutate();
		return new SwimmerView(swimmer);
	}

	private synchronized void showSwimmer(Group root) {
		root.getChildren().clear();
		root.getChildren().addAll(getSwimmerView().getParts());
		root.getChildren().add(getSwimmerView().getMouth());
	}
}
