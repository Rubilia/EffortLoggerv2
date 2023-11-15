package Prototype;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Poker_EffortLogger_Menu extends Application {
private Stage primaryStage;


    public static void main(String[] args) {
        launch(args);
    }
    
    public Poker_EffortLogger_Menu() {
        this.primaryStage = new Stage();
        start(primaryStage);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main Application");

        // Create buttons for EffortLogger and Planning Poker
        Button effortLoggerButton = new Button("EffortLogger");
        Button planningPokerButton = new Button("Planning Poker");

        // Set styles for the buttons
        String buttonStyle = "-fx-background-color: #4E90A4; -fx-text-fill: #FFFFFF; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 14pt;";
        effortLoggerButton.setStyle(buttonStyle);
        planningPokerButton.setStyle(buttonStyle);

        // Set actions for the buttons
        effortLoggerButton.setOnAction(e -> {
            // Add the logic to navigate to EffortLogger view
        	StartEffortLoggerMenu().run();
        });

        planningPokerButton.setOnAction(e -> {
            // Add the logic to navigate to Planning Poker view
            StartSavedPokerStatePrototype().run();
        });

        // Create HBox for buttons
        HBox buttonLayout = new HBox(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(effortLoggerButton, planningPokerButton);

        // Create VBox for the main layout
        VBox mainLayout = new VBox(50);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(buttonLayout);

        Scene scene = new Scene(mainLayout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
    }
    public void showWindow() {
        primaryStage.show();
    }
    
    private Runnable StartSavedPokerStatePrototype() {
        return () -> {
        	SavedPokerStatePrototype prototypeWindow = new SavedPokerStatePrototype(this.primaryStage);
            prototypeWindow.showWindow();
        };
    }
    
    private Runnable StartEffortLoggerMenu() {
    	return() -> {
    		EffortLogMenu prototypeWindow = new EffortLogMenu();
    		prototypeWindow.showWindow();
    	};
    }
}
