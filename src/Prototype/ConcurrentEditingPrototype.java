package Prototype;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class ConcurrentEditingPrototype {

    private Stage stage;
    private Stage primaryStage;

    public ConcurrentEditingPrototype(Stage primaryStage) {
        this.primaryStage = primaryStage;

        stage = new Stage();
        stage.setTitle("Tu 44 Effort Log Editor");
        stage.setResizable(false);
        stage.setWidth(600);  // You can adjust the value as per your layout's requirements
        stage.setHeight(400); // Adjust this value as well

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 20, 20, 20));

        HBox combinedBox = new HBox(10);  // Container for the two boxes
        combinedBox.setAlignment(Pos.CENTER_RIGHT); // Keep the box right-aligned

        // 1. Select the project to edit effort log
        VBox selectProjectBox = new VBox(5);
        Label selectProjectLabel = new Label("1. Select the project to edit effort log");
        selectProjectLabel.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        ComboBox<String> projectSelect = new ComboBox<>(FXCollections.observableArrayList("Project 1", "Project 2"));
        selectProjectBox.getChildren().addAll(selectProjectLabel, projectSelect);
        selectProjectBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(selectProjectBox, Priority.ALWAYS); // Make the selectProjectBox grow to take up available space

        combinedBox.getChildren().add(selectProjectBox); // Add this box to the combined container
	
	     // 2. Clear Project's effort log
	     VBox clearLogBox = new VBox(5);
	     Label clearLogLabel = new Label("2. a. Clear Project's Effort Log");
	     clearLogLabel.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
	     Button clearLogButton = new Button("Clear This Effort Log");
	     clearLogBox.getChildren().addAll(clearLogLabel, clearLogButton);
	     clearLogBox.setAlignment(Pos.CENTER_LEFT); 
	
	     combinedBox.getChildren().add(clearLogBox); // Add this box to the combined container
	     combinedBox.setAlignment(Pos.CENTER_RIGHT); // Align the combined box to the right
	     layout.getChildren().add(combinedBox);
	
	     // 2.b. Select the Effort Log Entry to modify
	     Label effortLogEntryLabel = new Label("2.b. Select the Effort Log Entry to modify and make it the current selection:");
	     effortLogEntryLabel.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
	     ComboBox<String> logEntrySelect = new ComboBox<>(FXCollections.observableArrayList("User Upload"));
	     layout.getChildren().addAll(effortLogEntryLabel, logEntrySelect);
	
	     // Inserting the new box after 2.b
	     VBox modifyLogBox = new VBox(5); // Changed from HBox to VBox
	     Label modifyLabel = new Label("3.a. Modify the Current Effort Log's attribute and \"Update this Entry\" when done:");
	     modifyLabel.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
	     Label detailsLabel = new Label("Date: <date>    Start Time: <time>    Stop time: <time>");
	     modifyLogBox.getChildren().addAll(modifyLabel, detailsLabel); // Now both labels will be vertically aligned
	     layout.getChildren().add(modifyLogBox);
	
	     GridPane grid = new GridPane();
	     grid.setHgap(20);
	     grid.setVgap(10);
	     layout.getChildren().add(grid);
	
	     HBox updateBox = new HBox(15);
	     Label updateLabel = new Label("These attributes will be deleted");
	     updateLabel.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
	     updateBox.getChildren().add(updateLabel);
	     updateBox.getChildren().add(new Button("Update This Entry"));
	     layout.getChildren().add(updateBox);


        // Bottom actions
        VBox bottomActions = new VBox(10);

        // 3.b. Delete Current Entry
        VBox deleteActions = new VBox(10);
        Label deleteLabel = new Label("3. b. Delete Current Entry:");
        deleteLabel.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        deleteActions.getChildren().add(deleteLabel);
        deleteActions.getChildren().add(new Button("Delete This Entry"));
        bottomActions.getChildren().add(deleteActions);

        // Next Line for 3.c and 4
        HBox nextLineActions = new HBox(30);

        // 3.c. Split Current Entry
        VBox splitActions = new VBox(10);
        Label splitLabel = new Label("3. c. Split Current Entry into two Entries:");
        splitLabel.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        splitActions.getChildren().add(splitLabel);
        splitActions.getChildren().add(new Button("Split Entry Into Two Entries"));
        nextLineActions.getChildren().add(splitActions);

        // 4. Proceed to Effort Log Console
        VBox proceedActions = new VBox(10);
        Label proceedLabel = new Label("4. Proceed to Effort Log Console");
        proceedLabel.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        proceedActions.getChildren().add(proceedLabel);
        proceedActions.getChildren().add(new Button("Proceed to Effort Log Console"));
        nextLineActions.getChildren().add(proceedActions);

        bottomActions.getChildren().add(nextLineActions);

        layout.getChildren().add(bottomActions);

        Scene scene = new Scene(layout, 900, 650);
        stage.setScene(scene);
//        stage.setOnCloseRequest(e -> {
//            this.primaryStage.show();
//        });
    }

    public void showWindow() {
        primaryStage.close();
        stage.show();
    }
}
