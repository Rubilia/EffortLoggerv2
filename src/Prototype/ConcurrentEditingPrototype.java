/**
 * Author: Ilia Rubashkin
 * Copyright © 2023 Ilia Rubashkin. All rights reserved.
 * 
 * Description: This file and its contents are the property of Ilia Rubashkin.
 * Unauthorized use, modification, or distribution of this file or its contents
 * without explicit permission from the author is strictly prohibited.
 */


package Prototype;

import java.util.ArrayList;
import java.util.UUID;

import Prototype.Data.Data;
import Prototype.Data.Project;
import Prototype.Data.Task;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.WindowEvent;

public class ConcurrentEditingPrototype extends Stage{

    private Stage stage;
    private Stage primaryStage;
    
    // Declaring UI Elements as class variables
    private VBox layout, selectProjectBox, clearLogBox, modifyLogBox, bottomActions, deleteActions, splitActions, proceedActions;
    private HBox combinedBox, updateBox;
    private Label selectProjectLabel, clearLogLabel, effortLogEntryLabel, modifyLabel, detailsLabel, updateLabel, deleteLabel, splitLabel, proceedLabel;
    private ComboBox<String> projectSelect, logEntrySelect, lifeCycleStepsDropdown, effortCategoryDropdown, planDropdown;
    private Button clearLogButton, updateButton, deleteButton;
    private GridPane grid;
    
    private String userID, currentProjectName = null, currentTaskName = null;
    private Data data;

    public ConcurrentEditingPrototype(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userID = UUID.randomUUID().toString();
        this.data = new Data();  // Instantiate the JsonConcurrencyTracker class
        
        // Prepare UI for dynamic updates
        setupUI();
        ProjectSelectorSetup();
        logEntrySelectorSetup();
        updateButtonSetup();
        setupClearButton();
        setupDeleteButton();
        stage.setOnCloseRequest(event -> handleWindowClose(event));
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

        layout.getChildren().add(bottomActions);
        Scene scene = new Scene(layout, 900, 820);
        stage.setScene(scene);
    }

    public void ProjectSelectorSetup() {
        ArrayList<String> projectNames = data.getProjectNames();
        projectSelect.setItems(FXCollections.observableArrayList(projectNames));

        projectSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || "null".equals(newValue)) return;

            currentProjectName = newValue;
            detailsLabel.setText("Date: <date>    Start Time: <time>    Stop time: <time>");

            Project projectAttributes = data.getProjectByName(currentProjectName);
            if (projectAttributes == null) {
                return; // Handle the case when the project is not found
            }

            logEntrySelect.getItems().clear();
            for (Task task : projectAttributes.getTasks()) {
                logEntrySelect.getItems().add(task.getName());
            }

            lifeCycleStepsDropdown.getItems().clear();
            for (String lifeCycle : projectAttributes.getLifeCycleSteps()) {
                lifeCycleStepsDropdown.getItems().add(lifeCycle);
            }

            effortCategoryDropdown.getItems().clear();
            for (String effortCategory : projectAttributes.getEffortCategories()) {
                effortCategoryDropdown.getItems().add(effortCategory);
            }

            planDropdown.getItems().clear();
            for (String plan : projectAttributes.getPlans()) {
                planDropdown.getItems().add(plan);
            }
        });
    }

    public void logEntrySelectorSetup() {
        // Define the listener
        ChangeListener<String> valueChangeListener = (observable, oldValue, newValue) -> {
            if (newValue == null || "null".equals(newValue)) return;

            Project project = data.getProjectByName(currentProjectName);
            if (project == null) {
                return; // Handle the case when the project is not found
            }
            
            if (oldValue != null) {
                Task oldTask = project.getTaskByName(oldValue);
                oldTask.unlock(userID);
                data.saveData();
            }
            
            currentTaskName = newValue;
            Task selectedTask = project.getTaskByName(newValue);
            if (selectedTask != null) {
            	// Handle locked tasks
            	if (selectedTask.isLocked(userID)){
            		currentTaskName = null;
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Selected task is locked! Try again later");
                    alert.showAndWait();
                    return;
            	}
            	
            	selectedTask.lock(userID);
            	data.saveData();

                // Update UI based on the selected log
                detailsLabel.setText(String.format("Date: %s    Start Time: %s    Stop time: %s",
                		selectedTask.getDate(), selectedTask.getStartTime(), selectedTask.getStopTime()));
                lifeCycleStepsDropdown.setValue(selectedTask.getLifeCycleStep());
                effortCategoryDropdown.setValue(selectedTask.getEffortCategory());
                planDropdown.setValue(selectedTask.getPlan());
            }
        };

        // Initially add the listener
        logEntrySelect.valueProperty().addListener(valueChangeListener);
    }

    public void updateButtonSetup() {
        updateButton.setOnAction(e -> {
            if (currentProjectName == null || currentTaskName == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please ensure both the project and task are selected!");
                alert.showAndWait();
                return;
            }

            String selectedLifeCycleStep = lifeCycleStepsDropdown.getValue();
            String selectedEffortCategory = effortCategoryDropdown.getValue();
            String selectedPlan = planDropdown.getValue();

            Project currentProject = data.getProjectByName(currentProjectName);
            if (currentProject != null) {
                Task taskToUpdate = currentProject.getTaskByName(currentTaskName);
                if (taskToUpdate != null) {
                    taskToUpdate.setLifeCycleStep(selectedLifeCycleStep);
                    taskToUpdate.setEffortCategory(selectedEffortCategory);
                    taskToUpdate.setPlan(selectedPlan);
                    data.saveData(); // Save the updated Data object to file
                }
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
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
    
    public void setupDeleteButton() {
    	deleteButton.setOnAction(e -> {
    		if (currentProjectName == null || currentTaskName == null) {
    	        Alert alert = new Alert(AlertType.WARNING);
    	        alert.setTitle("Error");
    	        alert.setHeaderText(null);
    	        alert.setContentText("You have not selected a task!");
    	        alert.showAndWait();
    	        return;
    		}
    		data.deleteTask(currentProjectName, currentTaskName);
            ConcurrentEditingPrototype prototypeWindow = new ConcurrentEditingPrototype(this.primaryStage);
            prototypeWindow.showWindow();
    		stage.close();
    	});
    }
    
    public void setupClearButton() {
    	clearLogButton.setOnAction(e -> {
            ConcurrentEditingPrototype prototypeWindow = new ConcurrentEditingPrototype(this.primaryStage);
            prototypeWindow.showWindow();
    		stage.close();
        });
    }
    
    private void handleWindowClose(WindowEvent event) {
    	// Release locks for all acquired tasks
        for (Project p: data.getProjects()) {
        	for (Task t: p.getTasks()) {
        		if (t.getCurrentUserEditing().getUserId().equals(userID)) {
        			t.unlock(userID);
        		}
        	}
        }
        data.saveData();
    }

    public void showWindow() {
        primaryStage.close();
        stage.show();
    }
}
