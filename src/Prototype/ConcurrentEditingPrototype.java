package Prototype;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class ConcurrentEditingPrototype {

    private Stage stage;
    private Stage primaryStage;  // The main window

    public ConcurrentEditingPrototype(Stage primaryStage) {
        this.primaryStage = primaryStage;

        stage = new Stage();
        stage.setTitle("Concurrent Editing Prototype");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label label = new Label("Concurrent Editing UI goes here.");
        layout.getChildren().add(label);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            // Show the primary window when this window closes
            this.primaryStage.show();
        });
    }

    public void showWindow() {
        primaryStage.close();
        stage.show();
    }
}
