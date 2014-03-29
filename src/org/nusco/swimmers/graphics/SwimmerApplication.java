package org.nusco.swimmers.graphics;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.nusco.swimmers.body.HeadPart;
import org.nusco.swimmers.body.Part;

public class SwimmerApplication extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Swimmer");

		SwimmerBody swimmerBody = createSwimmerBody();
		
		Group root = new Group();
		root.getChildren().addAll(swimmerBody.getParts());
		primaryStage.setScene(new Scene(root, 800, 800));
		primaryStage.show();
	}

	private SwimmerBody createSwimmerBody() {
		HeadPart head = new HeadPart(100, 10);
//		head.setRelativeAngle(45);
		
		Part child1 = head.sproutChild(150, 15, 45);
		child1.sproutChild(90, 15, 30);

		Part child2 = head.sproutChild(150, 15, -45);
		child2.sproutChild(90, 15, 30);

		return new SwimmerBody(head);
	}

	public static void main(String... args) {
		launch(args);
	}
}
