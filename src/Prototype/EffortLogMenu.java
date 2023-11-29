package Prototype;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EffortLogMenu extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public EffortLogMenu() {
        this.primaryStage = new Stage();
        start(primaryStage);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main Application");

        // Create buttons for EffortLogger and ConcurrentEditing
        Button effortLoggerButton = new Button("Create Effort Log");
        Button concurrentEditingButton = new Button("Concurrent Editing");

        // Set styles for the buttons
        String buttonStyle = "-fx-background-color: #4E90A4; -fx-text-fill: #FFFFFF; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 14pt;";
        effortLoggerButton.setStyle(buttonStyle);
        concurrentEditingButton.setStyle(buttonStyle);

        // Set actions for the buttons
        effortLoggerButton.setOnAction(e -> {
            
        });

        concurrentEditingButton.setOnAction(e -> {
            // Add the logic to navigate to ConcurrentEditingPrototype
            StartConcurrentEditingPrototype().run();
        });

        // Create HBox for buttons
        HBox buttonLayout = new HBox(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(effortLoggerButton, concurrentEditingButton);

        // Create VBox for the main layout
        VBox mainLayout = new VBox(50);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(buttonLayout);

        Scene scene = new Scene(mainLayout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Runnable StartConcurrentEditingPrototype() {
        return () -> {
            ConcurrentEditingPrototype prototypeWindow = new ConcurrentEditingPrototype(primaryStage);
            prototypeWindow.showWindow();
        };
    }
    
    public void showWindow() {
    	primaryStage.show();
    }
}
