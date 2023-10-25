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
    
    // Separating UI elements into class variables
    private ComboBox<String> projectSelect, logEntrySelect;
    private Button clearLogButton, updateButton, deleteButton, splitButton, proceedButton;
    private Label selectProjectLabel, clearLogLabel, effortLogEntryLabel, modifyLabel, detailsLabel, updateLabel, deleteLabel, splitLabel, proceedLabel;
    
    public ConcurrentEditingPrototype(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupUI();
    }
    
    private void setupUI() {
        stage = new Stage();
        stage.setTitle("Tu 44 Effort Log Editor");
        stage.setResizable(false);
        stage.setWidth(900);
        stage.setHeight(650);

        // Styles
        String comboBoxStyle = "-fx-background-color: #FFFFFF; -fx-border-color: #AAAAAA; -fx-border-width: 1; -fx-font-size: 14pt;";
        String buttonStyle = "-fx-background-color: #4E90A4; -fx-text-fill: #FFFFFF; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 14pt;";
        String labelStyleBold = "-fx-font-family: 'Arial'; -fx-font-weight: BOLD; -fx-font-size: 14pt;";
        String layoutBackgroundStyle = "-fx-background-color: #E0E0E0;";

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setStyle(layoutBackgroundStyle);

        HBox combinedBox = new HBox(50);
        combinedBox.setAlignment(Pos.CENTER);

        VBox selectProjectBox = new VBox(5);
        Label selectProjectLabel = new Label("1. Select the project to edit effort log");
        selectProjectLabel.setStyle(labelStyleBold);
        ComboBox<String> projectSelect = new ComboBox<>(FXCollections.observableArrayList("Project 1", "Project 2"));
        projectSelect.setStyle(comboBoxStyle);
        projectSelect.setPrefWidth(300);
        selectProjectBox.getChildren().addAll(selectProjectLabel, projectSelect);
        selectProjectBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(selectProjectBox, Priority.ALWAYS);  // Make sure this VBox grows as necessary
        combinedBox.getChildren().add(selectProjectBox);

        VBox clearLogBox = new VBox(5);
        Label clearLogLabel = new Label("2. a. Clear Project's Effort Log");
        clearLogLabel.setStyle(labelStyleBold);
        Button clearLogButton = new Button("Clear This Effort Log");
        clearLogButton.setStyle(buttonStyle);
        clearLogBox.getChildren().addAll(clearLogLabel, clearLogButton);
        clearLogBox.setAlignment(Pos.CENTER_RIGHT);  // Added this line to align the VBox to the right within the HBox
        combinedBox.getChildren().add(clearLogBox);

        layout.getChildren().add(combinedBox);

        effortLogEntryLabel = new Label("2.b. Select the Effort Log Entry to modify and make it the current selection:");
        effortLogEntryLabel.setStyle(labelStyleBold);
        logEntrySelect = new ComboBox<>(FXCollections.observableArrayList("User Upload"));
        logEntrySelect.setStyle(comboBoxStyle);
        layout.getChildren().addAll(effortLogEntryLabel, logEntrySelect);

        VBox modifyLogBox = new VBox(5);
        modifyLabel = new Label("3.a. Modify the Current Effort Log's attribute and \"Update this Entry\" when done:");
        modifyLabel.setStyle(labelStyleBold);
        detailsLabel = new Label("Date: <date>    Start Time: <time>    Stop time: <time>");
        detailsLabel.setFont(Font.font("Arial", 12));
        modifyLogBox.getChildren().addAll(modifyLabel, detailsLabel);
        layout.getChildren().add(modifyLogBox);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        layout.getChildren().add(grid);

        HBox updateBox = new HBox(15);
        updateLabel = new Label("These attributes have been saved");
        updateLabel.setStyle(labelStyleBold);
        updateButton = new Button("Update This Entry");
        updateButton.setStyle(buttonStyle);
        updateBox.getChildren().addAll(updateLabel, updateButton);
        layout.getChildren().add(updateBox);

        VBox bottomActions = new VBox(10);
        VBox deleteActions = new VBox(10);
        deleteLabel = new Label("3. b. Delete Current Entry:");
        deleteLabel.setStyle(labelStyleBold);
        deleteButton = new Button("Delete This Entry");
        deleteButton.setStyle(buttonStyle);
        deleteActions.getChildren().addAll(deleteLabel, deleteButton);
        bottomActions.getChildren().add(deleteActions);

        HBox nextLineActions = new HBox(30);
        VBox splitActions = new VBox(10);
        splitLabel = new Label("3. c. Split Current Entry into two Entries:");
        splitLabel.setStyle(labelStyleBold);
        splitButton = new Button("Split Entry Into Two Entries");
        splitButton.setStyle(buttonStyle);
        splitActions.getChildren().addAll(splitLabel, splitButton);
        nextLineActions.getChildren().add(splitActions);

        VBox proceedActions = new VBox(10);
        proceedLabel = new Label("4. Proceed to Effort Log Console");
        proceedLabel.setStyle(labelStyleBold);
        proceedButton = new Button("Proceed to Effort Log Console");
        proceedButton.setStyle(buttonStyle);
        proceedActions.getChildren().addAll(proceedLabel, proceedButton);
        nextLineActions.getChildren().add(proceedActions);

        bottomActions.getChildren().add(nextLineActions);
        layout.getChildren().add(bottomActions);

        Scene scene = new Scene(layout, 900, 650);
        stage.setScene(scene);
    }

    public void showWindow() {
        primaryStage.close();
        stage.show();
    }
}
