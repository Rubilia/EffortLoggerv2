/**
 * Author: Ilia Rubashkin
 * Copyright © 2023 Ilia Rubashkin. All rights reserved.
 * 
 * Description: This file and its contents are the property of Ilia Rubashkin.
 * Unauthorized use, modification, or distribution of this file or its contents
 * without explicit permission from the author is strictly prohibited.
 */


package Prototype;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ConcurrentEditingPrototype extends Stage{

    private Stage stage;
    private Stage primaryStage;
    
    // Declaring UI Elements as class variables
    private VBox layout, selectProjectBox, clearLogBox, modifyLogBox, bottomActions, deleteActions, splitActions, proceedActions;
    private HBox combinedBox, updateBox, nextLineActions;
    private Label selectProjectLabel, clearLogLabel, effortLogEntryLabel, modifyLabel, detailsLabel, updateLabel, deleteLabel, splitLabel, proceedLabel;
    private ComboBox<String> projectSelect, logEntrySelect, lifeCycleStepsDropdown, effortCategoryDropdown, planDropdown;
    private Button clearLogButton, updateButton, deleteButton, splitButton, proceedButton;
    private GridPane grid;
    
    private String userID, currentProjectName = null, currentTaskName = null;
    private JsonConcurrencyTracker jsonTracker;

    public ConcurrentEditingPrototype(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userID = UUID.randomUUID().toString();
        this.jsonTracker = new JsonConcurrencyTracker(this);  // Instantiate the JsonConcurrencyTracker class
        
        // Prepare UI for dynamic updates
        setupUI();
        ProjectSelectorSetup();
        logEntrySelectorSetup();
        updateButtonSetup();        
    }

    private void setupUI() {
        stage = new Stage();
        stage.setTitle("Tu 44 Effort Log Editor");
        stage.setResizable(false);
        stage.setWidth(900);
        stage.setHeight(820);

        // Styles
        String comboBoxStyle = "-fx-background-color: #FFFFFF; -fx-border-color: #2D3748; -fx-border-width: 1; -fx-font-family: 'Roboto'; -fx-font-size: 14pt;";
        String buttonStyle = "-fx-background-color: #48BB78; -fx-text-fill: #FFFFFF; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-family: 'Roboto'; -fx-font-size: 14pt; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 3, 3);";
        String labelStyleBold = "-fx-font-family: 'Roboto'; -fx-font-weight: BOLD; -fx-font-size: 16pt; -fx-text-fill: #2D3748;";
        String layoutBackgroundStyle = "-fx-background-color: #EDF2F7;";
        
        layout = new VBox(20);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setStyle(layoutBackgroundStyle);

        combinedBox = new HBox(50);
        combinedBox.setAlignment(Pos.CENTER);

        selectProjectBox = new VBox(5);
        selectProjectLabel = new Label("Select the project to edit effort log");
        selectProjectLabel.setStyle(labelStyleBold);
        projectSelect = new ComboBox<>(FXCollections.observableArrayList());
        projectSelect.setStyle(comboBoxStyle);
        projectSelect.setPrefWidth(300);
        selectProjectBox.getChildren().addAll(selectProjectLabel, projectSelect);
        selectProjectBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(selectProjectBox, Priority.ALWAYS);
        combinedBox.getChildren().add(selectProjectBox);

        clearLogBox = new VBox(5);
        clearLogLabel = new Label("Clear Project's Effort Log");
        clearLogLabel.setStyle(labelStyleBold);
        clearLogButton = new Button("Clear This Effort Log");
        clearLogButton.setStyle(buttonStyle);
        clearLogBox.getChildren().addAll(clearLogLabel, clearLogButton);
        clearLogBox.setAlignment(Pos.CENTER_RIGHT);
        combinedBox.getChildren().add(clearLogBox);

        layout.getChildren().add(combinedBox);

        effortLogEntryLabel = new Label("Select the Effort Log Entry to modify and make it the current selection:");
        effortLogEntryLabel.setStyle(labelStyleBold);
        logEntrySelect = new ComboBox<>(FXCollections.observableArrayList());
        logEntrySelect.setStyle(comboBoxStyle);
        layout.getChildren().addAll(effortLogEntryLabel, logEntrySelect);

        modifyLogBox = new VBox(5);
        modifyLabel = new Label("Modify the Current Effort Log's attribute and \"Update this Entry\" when done:");
        modifyLabel.setStyle(labelStyleBold);
        detailsLabel = new Label("Date: <date>    Start Time: <time>    Stop time: <time>");
        detailsLabel.setFont(Font.font("Arial", 12));
        modifyLogBox.getChildren().addAll(modifyLabel, detailsLabel);
        layout.getChildren().add(modifyLogBox);
        
        
        // Adding the new block between "3.a." and "These attributes have been saved"
        VBox lifeCycleBox = new VBox(5);
        Label lifeCycleLabel = new Label("Life Cycle Steps:");
        lifeCycleLabel.setStyle(labelStyleBold);
        lifeCycleStepsDropdown = new ComboBox<>(FXCollections.observableArrayList());
        lifeCycleStepsDropdown.setStyle(comboBoxStyle);
        lifeCycleStepsDropdown.setPrefWidth(300);
        lifeCycleBox.getChildren().addAll(lifeCycleLabel, lifeCycleStepsDropdown);
        layout.getChildren().add(lifeCycleBox);

        // Adding new block containing 2 sub-blocks
        HBox newHBox = new HBox(100);
        
        // Block 1
        VBox block1 = new VBox(5);
        Label effortCategoryLabel = new Label("Effort Category:");
        effortCategoryLabel.setStyle(labelStyleBold);
        effortCategoryDropdown = new ComboBox<>(FXCollections.observableArrayList());
        effortCategoryDropdown.setStyle(comboBoxStyle);
        block1.getChildren().addAll(effortCategoryLabel, effortCategoryDropdown);
        
        // Block 2
        VBox block2 = new VBox(5);
        Label planLabel = new Label("Plan:");
        planLabel.setStyle(labelStyleBold);
        planDropdown = new ComboBox<>(FXCollections.observableArrayList());
        planDropdown.setStyle(comboBoxStyle);
        block2.getChildren().addAll(planLabel, planDropdown);

        newHBox.getChildren().addAll(block1, block2);
        layout.getChildren().add(newHBox);

        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        layout.getChildren().add(grid);

        updateBox = new HBox(15);
        updateLabel = new Label("These attributes have been saved");
        updateLabel.setStyle(labelStyleBold);
        updateButton = new Button("Update This Entry");
        updateButton.setStyle(buttonStyle);
        updateBox.getChildren().addAll(updateLabel, updateButton);
        layout.getChildren().add(updateBox);

        bottomActions = new VBox(10);
        deleteActions = new VBox(10);
        deleteLabel = new Label("Delete Current Entry:");
        deleteLabel.setStyle(labelStyleBold);
        deleteButton = new Button("Delete This Entry");
        deleteButton.setStyle(buttonStyle);
        deleteActions.getChildren().addAll(deleteLabel, deleteButton);
        bottomActions.getChildren().add(deleteActions);

        nextLineActions = new HBox(30);
        splitActions = new VBox(10);
        splitLabel = new Label("Split Current Entry into two Entries:");
        splitLabel.setStyle(labelStyleBold);
        splitButton = new Button("Split Entry Into Two Entries");
        splitButton.setStyle(buttonStyle);
        splitActions.getChildren().addAll(splitLabel, splitButton);
        nextLineActions.getChildren().add(splitActions);

        proceedActions = new VBox(10);
        proceedLabel = new Label("Proceed to Effort Log Console");
        proceedLabel.setStyle(labelStyleBold);
        proceedButton = new Button("Proceed to Effort Log Console");
        proceedButton.setStyle(buttonStyle);
        proceedActions.getChildren().addAll(proceedLabel, proceedButton);
        nextLineActions.getChildren().add(proceedActions);

        bottomActions.getChildren().add(nextLineActions);
        layout.getChildren().add(bottomActions);

        Scene scene = new Scene(layout, 900, 820);
        stage.setScene(scene);
    }

    public void ProjectSelectorSetup() {
        try {
            ArrayList<String> projects = jsonTracker.getProjectNames();
            projectSelect.setItems(FXCollections.observableArrayList(projects));

            // Bind a method to the ComboBox to handle selection changes
            projectSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                	if (newValue == null | newValue.equals("null")) return;
                	
                	currentProjectName = newValue;

                    // Reset UI elements
                    detailsLabel.setText("Date: <date>    Start Time: <time>    Stop time: <time>");

                    // Retrieve project attributes based on the selected project name
                    JSONObject projectAttributes = jsonTracker.getProjectAttributes(newValue);

                    // Clear existing items in logEntrySelect
                    logEntrySelect.getItems().clear();

                    // Retrieve the "UserLogs" array from the project attributes
                    JSONArray userLogs = projectAttributes.getJSONArray("UserLogs");

                    // Populate logEntrySelect with "logName" attributes from "UserLogs"
                    for (int i = 0; i < userLogs.length(); i++) {
                        JSONObject logEntry = userLogs.getJSONObject(i);
                        String logName = logEntry.getString("logName");
                        logEntrySelect.getItems().add(logName);
                    }

                    // Clear and set items for lifeCycleStepsDropdown
                    lifeCycleStepsDropdown.getItems().clear();
                    JSONArray lifeCycleSteps = projectAttributes.getJSONArray("LifeCycleSteps");
                    for (int i = 0; i < lifeCycleSteps.length(); i++) {
                        lifeCycleStepsDropdown.getItems().add(lifeCycleSteps.getString(i));
                    }

                    // Clear and set items for effortCategoryDropdown
                    effortCategoryDropdown.getItems().clear();
                    JSONArray effortCategories = projectAttributes.getJSONArray("EffortCategories");
                    for (int i = 0; i < effortCategories.length(); i++) {
                        effortCategoryDropdown.getItems().add(effortCategories.getString(i));
                    }

                    // Clear and set items for planDropdown
                    planDropdown.getItems().clear();
                    JSONArray plans = projectAttributes.getJSONArray("Plans");
                    for (int i = 0; i < plans.length(); i++) {
                        planDropdown.getItems().add(plans.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public void logEntrySelectorSetup() {
        // Define the listener
        ChangeListener<String> valueChangeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                	if (newValue == null | newValue.equals("null")) return;
                	
                    if (!jsonTracker.changeOwnership(currentProjectName, oldValue, newValue, userID)) {
                    	// Remove the listener
                    	logEntrySelect.valueProperty().removeListener(this);
                    	
                    	currentTaskName = null;
                    	// Use Platform.runLater to defer setting the value
                    	Platform.runLater(() -> {
                    	    // Set the value back to what it was
                    		logEntrySelect.setValue(null);

                    	    // Re-add the listener
                    	    logEntrySelect.valueProperty().addListener(this);
                    	});

                        // Show a dialog saying "This log is being edited by someone else" with an OK button
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Log Unavailable");
                        alert.setHeaderText(null);
                        alert.setContentText("This log is being edited by someone else.");
                        alert.show();
                        return; // Exit the listener to avoid further processing
                    }

                    // Get the selected project name and task name
                    String projectName = projectSelect.getValue();
                    String taskName = newValue;
                    currentTaskName = taskName;

                    // Use the method to get task attributes
                    JSONObject taskAttributes = jsonTracker.getTaskAttributes(projectName, taskName);

                    if (taskAttributes != null) {
                        // Extract date, startTime, stopTime, lifeCycle, effortCategory, and plan from task attributes
                        String date = taskAttributes.getString("date");
                        String startTime = taskAttributes.getString("startTime");
                        String stopTime = taskAttributes.getString("stopTime");
                        String lifeCycle = taskAttributes.getString("lifeCycle");
                        String effortCategory = taskAttributes.getString("effortCategory");
                        String plan = taskAttributes.getString("plan");

                        // Set the text of the modifyLabel and ComboBoxes based on task attributes
                        detailsLabel.setText("Date: " + date + "    StartTime: " + startTime + "   StopTime: " + stopTime);
                        lifeCycleStepsDropdown.setValue(lifeCycle);
                        effortCategoryDropdown.setValue(effortCategory);
                        planDropdown.setValue(plan);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // Initially add the listener
        logEntrySelect.valueProperty().addListener(valueChangeListener);
    }
    
    public void updateButtonSetup() {
        updateButton.setOnAction(e -> {
            // Check if currentProjectName or currentTaskName are null
            if (currentProjectName == null || currentTaskName == null) {
                // Show an error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please ensure both the project and task are selected!");
                alert.showAndWait();
                return;
            }

            // Retrieve text from comboboxes
            String selectedLifeCycleStep = lifeCycleStepsDropdown.getValue();
            String selectedEffortCategory = effortCategoryDropdown.getValue();
            String selectedPlan = planDropdown.getValue();

            // Call updateLogData function
            try {
            	jsonTracker.updateLogData(currentProjectName, currentTaskName, selectedLifeCycleStep, selectedEffortCategory, selectedPlan);
            } catch (JSONException ex) {
                // Handle JSON parsing error here (e.g., show an error alert or print the stack trace)
                ex.printStackTrace();
            }
            
            // Show an error alert
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText(null);
            alert.setContentText("Your changes have been saved");
            alert.showAndWait();
        });
    }
    
    public void informAboutEditingTimeout() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Autosave");
        alert.setHeaderText(null);
        alert.setContentText("You have not interacted with the program for a while! Your changes have been saved");
        alert.showAndWait();
    }
    

    public void showWindow() {
        primaryStage.close();
        stage.show();
    }
}
