// Author: Katherine Tinlin

package Prototype;

import java.util.ArrayList;
import java.util.List;
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
import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;

public class EffortLogCreator extends Stage{

    private Stage stage;
    private Stage primaryStage;
    
    // Declaring UI Elements as class variables
    private VBox layout, modifyLogBox, selectProjectBox, bottomActions, deleteActions, proceedActions, createActions, nextLineActions;
    private HBox combinedBox;
    private TextField effortLogNameText, effortDescriptionText;
    private Label selectProjectLabel, modifyLabel, detailsLabel, deleteLabel, createLabel, proceedLabel;
    private ComboBox<String> projectSelect, logEntrySelect, lifeCycleStepsDropdown, effortCategoryDropdown, planDropdown;
    private Button deleteButton, createButton, proceedButton;
    private GridPane grid;
    
    private String userID, currentProjectName, currentEntryName = null;
    private Data data;

    public EffortLogCreator(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userID = UUID.randomUUID().toString();
        this.data = new Data();  // Instantiate the JsonConcurrencyTracker class
        
        // Prepare UI for dynamic updates
        setupUI();
        ProjectSelectorSetup();
        logEntryNameSetup();
        createButtonSetup();
        deleteButtonSetup();
        stage.setOnCloseRequest(event -> handleWindowClose(event));
        }

    private void setupUI() {
        stage = new Stage();
        stage.setTitle("Tu 44 Effort Log Creator");
        stage.setResizable(false);
        stage.setWidth(520);
        stage.setHeight(820);

        
        // Styles
        String comboBoxStyle = "-fx-background-color: #FFFFFF; -fx-border-color: #2D3748; -fx-border-width: 1; -fx-font-family: 'Roboto'; -fx-font-size: 14pt;";
        String buttonStyle = "-fx-background-color: #48BB78; -fx-text-fill: #FFFFFF; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-family: 'Roboto'; -fx-font-size: 14pt; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 3, 3);";
        String labelStyleBold = "-fx-font-family: 'Roboto'; -fx-font-weight: BOLD; -fx-font-size: 16pt; -fx-text-fill: #2D3748;";
        String layoutBackgroundStyle = "-fx-background-color: #EDF2F7;";
        
        layout = new VBox(20);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setStyle(layoutBackgroundStyle);
        

        selectProjectBox = new VBox(5);
        selectProjectLabel = new Label("Select the project to create effort log");
        selectProjectLabel.setStyle(labelStyleBold);
        projectSelect = new ComboBox<>(FXCollections.observableArrayList());
        projectSelect.setStyle(comboBoxStyle);
        projectSelect.setPrefWidth(300);
        selectProjectBox.getChildren().addAll(selectProjectLabel, projectSelect);
        selectProjectBox.setAlignment(Pos.CENTER_LEFT);
        layout.getChildren().add(selectProjectBox);

        
        modifyLogBox = new VBox(5);
        modifyLabel = new Label("Populate the Current Effort Log's attributes \n and \"Create this Entry\" when done:");
        modifyLabel.setStyle(labelStyleBold);
        detailsLabel = new Label("Date: <date>    Start Time: <time>    Stop time: <time>");
        detailsLabel.setFont(Font.font("Arial", 12));
        modifyLogBox.getChildren().addAll(modifyLabel, detailsLabel);
        layout.getChildren().add(modifyLogBox);
        
        HBox effortLogName = new HBox(100);
        Label effortLogNameLabel = new Label("Entry Name:");
        effortLogNameLabel.setStyle(labelStyleBold);
        effortLogNameText = new TextField();
        effortLogName.getChildren().addAll(effortLogNameLabel, effortLogNameText);
        layout.getChildren().add(effortLogName);
        
        
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
        
        
        HBox effortDescription = new HBox(100);
        Label effortDescriptionLabel = new Label("Description:");
        effortDescriptionLabel.setStyle(labelStyleBold);
        TextField effortDescriptionText = new TextField();
        effortDescription.getChildren().addAll(effortDescriptionLabel, effortDescriptionText);

        layout.getChildren().add(effortDescription);
        
        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        layout.getChildren().add(grid);
        
        combinedBox = new HBox(50);
        
        nextLineActions = new VBox(10);
        createActions = new VBox(10);
        createLabel = new Label("Create Effort Log Entry:");
        createLabel.setStyle(labelStyleBold);
        createButton = new Button("Create Entry");
        createButton.setStyle(buttonStyle);
        createActions.getChildren().addAll(createLabel, createButton);
        nextLineActions.getChildren().add(createActions);
        combinedBox.getChildren().add(nextLineActions);
        
        bottomActions = new VBox(10);
        deleteActions = new VBox(10);
        deleteLabel = new Label("Clear All Fields:");
        deleteLabel.setStyle(labelStyleBold);
        deleteButton = new Button("Clear Entered Data");
        deleteButton.setStyle(buttonStyle);
        deleteActions.getChildren().addAll(deleteLabel, deleteButton);
        bottomActions.getChildren().add(deleteActions);
        combinedBox.getChildren().add(bottomActions);
        
        proceedActions = new VBox(10);
        proceedLabel = new Label("Proceed to Effort Log Editor");
        proceedLabel.setStyle(labelStyleBold);
        proceedButton = new Button("Proceed to Effort Log Editor");
        proceedButton.setStyle(buttonStyle);
        proceedActions.getChildren().addAll(proceedLabel, proceedButton);

        layout.getChildren().add(combinedBox);
        layout.getChildren().add(proceedActions);
        
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
    
    public void logEntryNameSetup() {
    	
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
       
    	currentEntryName = newValue;
    	data.saveData();
    	
        };
        
        // Initially add the listener
        effortLogNameText.textProperty().addListener(valueChangeListener);        
    }
    
    
    public void deleteButtonSetup() {
    	deleteButton.setOnAction(e -> {
    		
        effortLogNameText.clear();
        effortDescriptionText.clear();
    	lifeCycleStepsDropdown.getSelectionModel().clearSelection();
        effortCategoryDropdown.getSelectionModel().clearSelection();
        planDropdown.setValue(null);	
    	lifeCycleStepsDropdown.setValue(null);	
        effortCategoryDropdown.setValue(null);	
        planDropdown.setValue(null);	
    	});
    }
    
    public void createButtonSetup() {
        createButton.setOnAction(e -> {
/*            if (currentProjectName == null || currentEntryName == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please ensure both the project name and entry name have been created!");
                alert.showAndWait();
                return;
            } */

            String selectedLifeCycleStep = lifeCycleStepsDropdown.getValue();
            String selectedEffortCategory = effortCategoryDropdown.getValue();
            String selectedPlan = planDropdown.getValue();

            Project currentProject = data.getProjectByName(currentProjectName);
            if (currentProject != null) {
                Task taskToUpdate = currentProject.getTaskByName(currentEntryName);
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
    
    private void handleWindowClose(WindowEvent event) {
        primaryStage.show();
    }

    public void showWindow() {
        primaryStage.close();
        stage.show();
    }
}
