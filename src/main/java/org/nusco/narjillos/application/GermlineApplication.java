package org.nusco.narjillos.application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.nusco.narjillos.application.utilities.AppState;
import org.nusco.narjillos.application.utilities.Effects;
import org.nusco.narjillos.application.utilities.Light;
import org.nusco.narjillos.application.utilities.Speed;
import org.nusco.narjillos.application.utilities.StoppableThread;
import org.nusco.narjillos.application.views.EnvirommentView;
import org.nusco.narjillos.application.views.MicroscopeView;
import org.nusco.narjillos.application.views.StringView;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNA;

/**
 * This application loads the germline out of an experiment and shows the
 * evolution of the most successfull genome.
 * 
 * (Alternately, it can also show a lineup of random creatures).
 */
public class GermlineApplication extends NarjillosApplication {

	private static AppState state = new AppState() {

		@Override
		public Speed getSpeed() {
			return Speed.REALTIME;
		}

		@Override
		public Light getLight() {
			return Light.ON;
		}

		@Override
		public int getFramesPeriod() {
			final int FPS = 30;
			return 1000 / FPS;
		}

		@Override
		public Effects getEffects() {
			return Effects.ON;
		}
	};

	@Override
	protected StoppableThread createModelThread(final String[] arguments, final boolean[] isModelInitialized) {
		return new StoppableThread() {
			@Override
			public void run() {
				if (arguments.length != 1) {
					System.out.println("This program needs either a *.germline file or the -random option.");
					System.exit(1);
				}

				List<DNA> germline;
				if (arguments[0].equals("-random")) {
					System.out.println("No *.germline file. Generating random DNAs...");
					germline = randomGermline();
				} else
					germline = readGermline(arguments[0]);

				setDish(new IsolationDish(germline));
				getDish().moveToFirst();
				Narjillo firstNarjillo = getDish().getNarjillo();
				firstNarjillo.getBody().forcePosition(Vector.ZERO, 180);

				isModelInitialized[0] = true;

				while (!hasBeenAskedToStop()) {
					long startTime = System.currentTimeMillis();
					tick();
					waitFor(state.getSpeed().getTicksPeriod(), startTime);
				}
			}

			private List<DNA> randomGermline() {
				List<DNA> result = new LinkedList<DNA>();
				RanGen ranGen = new RanGen((int) (Math.random() * 100000));
				for (int i = 0; i < 1000; i++)
					result.add(DNA.random(i, ranGen));
				return result;
			}

			/**
			 * Takes an ancestry file that contains a germline (one DNA document
			 * per line) and returns a list of matching phenotypes.
			 */
			private List<DNA> readGermline(String germlineFileName) {
				try {
					BufferedReader reader = new BufferedReader(new FileReader(germlineFileName));

					List<DNA> result = new ArrayList<DNA>();
					String nextLine = reader.readLine();
					while (nextLine != null) {
						result.add(new DNA(1, nextLine.trim()));
						nextLine = reader.readLine();
					}
					reader.close();
					System.out.println("Loaded ancestry germline from file " + germlineFileName);
					return result;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	@Override
	protected StoppableThread createViewThread(final Group root) {
		return new StoppableThread() {
			private volatile boolean renderingFinished = false;

			private final MicroscopeView foregroundView = new MicroscopeView(getViewport());
			private final EnvirommentView ecosystemView = new EnvirommentView(getEcosystem(), getViewport(), state);
			private final StringView statusView = new StringView(40);

			public void run() {
				trackNarjillo();

				while (!hasBeenAskedToStop()) {
					long startTime = System.currentTimeMillis();
					renderingFinished = false;

					getTracker().tick();
					ecosystemView.tick();

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							update(root);
							renderingFinished = true;
						}
					});

					waitFor(state.getFramesPeriod(), startTime);
					while (!renderingFinished && !hasBeenAskedToStop())
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
						}
				}
			}

			private void update(final Group root) {
				root.getChildren().clear();
				root.getChildren().add(ecosystemView.toNode());
				root.getChildren().add(foregroundView.toNode());

				Node statusInfo = statusView.toNode(getDishStatistics());
				root.getChildren().add(statusInfo);
			}
		};
	}

	@Override
	protected String getName() {
		return "Ancestry Browser";
	}

	@Override
	protected void registerInteractionHandlers(final Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				final int SKIP = 10;
				if (keyEvent.getCode() == KeyCode.LEFT && keyEvent.isControlDown())
					moveBack(SKIP);
				else if (keyEvent.getCode() == KeyCode.RIGHT && keyEvent.isControlDown())
					moveForward(SKIP);
				else if (keyEvent.getCode() == KeyCode.LEFT && keyEvent.isShiftDown())
					moveToFirst();
				else if (keyEvent.getCode() == KeyCode.RIGHT && keyEvent.isShiftDown())
					moveToLast();
				else if (keyEvent.getCode() == KeyCode.LEFT)
					moveBack(1);
				else if (keyEvent.getCode() == KeyCode.RIGHT)
					moveForward(1);
				else if (keyEvent.getCode() == KeyCode.DOWN)
					resetSpecimen();
			}

			private void resetSpecimen() {
				Narjillo from = getDish().getNarjillo();
				getDish().resetSpecimen();
				Narjillo to = getDish().getNarjillo();
				switchNarjillo(from, to);
			}

			private void moveBack(int skip) {
				Narjillo from = getDish().getNarjillo();
				getDish().moveBack(skip);
				Narjillo to = getDish().getNarjillo();
				switchNarjillo(from, to);
			}

			private void moveForward(int skip) {
				Narjillo from = getDish().getNarjillo();
				getDish().moveForward(skip);
				Narjillo to = getDish().getNarjillo();
				switchNarjillo(from, to);
			}

			private void moveToFirst() {
				Narjillo from = getDish().getNarjillo();
				getDish().moveToFirst();
				Narjillo to = getDish().getNarjillo();
				switchNarjillo(from, to);
			}

			private void moveToLast() {
				Narjillo from = getDish().getNarjillo();
				getDish().moveToLast();
				Narjillo to = getDish().getNarjillo();
				switchNarjillo(from, to);
			}

			private void switchNarjillo(Narjillo from, Narjillo to) {
				to.getBody().forcePosition(from.getPosition(), 180);
				to.setTarget(to.getPosition().minus(Vector.cartesian(1000000, 0)));
				trackNarjillo();
			}
		});
	}

	@Override
	protected synchronized IsolationDish getDish() {
		return (IsolationDish) super.getDish();
	}

	private void trackNarjillo() {
		getTracker().startTracking(getDish().getNarjillo());
	}
}
