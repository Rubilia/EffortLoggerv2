/**
 * Author: Josh Camberg
 * Copyright © 2023 Josh Camberg. All rights reserved.
 * 
 * Description: This file and its contents are the property of Josh Camberg.
 * Unauthorized use, modification, or distribution of this file or its contents
 * without explicit permission from the author is strictly prohibited.
 */

package Prototype;

import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Prototype.Data.Data;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SavedPokerStatePrototype extends Stage {
	private Stage stage;
    private Stage primaryStage;
    private String userName;
    private Label userNameLabel; // Label to display the user's name
    private Label userCountLabel; // Label to display the user count
    private int userCount = 0;
    private String currentStoryTitle, currentStoryDescription;

    private VBox layout, currentStoryBox, importSessionBox, newSessionBox;
    private HBox phaseOneBox, titleBox, bottomActions, cardButtonsBox, margin, sessionBox;
    private Label projectLabel, chooseProjectLabel, titleLabel, currentStoryLabel, voteLabel, importSessionLabel, newSessionLabel;
    private Pane spacer;
    private Button[] cardButtons;
    private Button nextButton, sessionButton, importSessionButton, newSessionButton;
    private ComboBox<String> storyListButton;
    private ComboBox<String> projectListButton;
    private int storyID = 1; // To track the current story
    private String fileNameWithoutExtension, currentStory;
    private boolean sessionStarted, hasVoted;
    private ArrayList<Vote> userVotes = new ArrayList<>(); // To store the votes
    private HashMap<Integer, ArrayList<Integer>> storyPoints;
    private ArrayList<UserStory> storiesToVoteOn = new ArrayList<>();
    private ArrayList<String> projectList;
    private GridPane gridPane;
    
    private Data data;
    
    private File savedFile = null;

    public SavedPokerStatePrototype(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.data = new Data();
        projectListButton = new ComboBox<>();
        projectList = data.getProjectNames();
        projectListButton.setItems(FXCollections.observableArrayList(projectList));
        storyPoints = new HashMap<>();
        setupUI();
    }

    private void setupUI() {
        stage = new Stage();
        stage.setTitle("Planning Poker Session");
        stage.setWidth(900);
        stage.setHeight(820);

        String labelStyleBold = "-fx-font-family: 'Roboto'; -fx-font-weight: BOLD; -fx-font-size: 14pt; -fx-text-fill: #2D3748;";
        String titleStyle = "-fx-font-family: 'Roboto'; -fx-font-weight: BOLD; -fx-font-size: 34pt; -fx-text-fill: #2D3748;";
        String buttonStyle = "-fx-background-color: #48BB78; -fx-text-fill: #FFFFFF; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-family: 'Roboto'; -fx-font-size: 14pt; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 3, 3);";

        layout = new VBox(20);
        layout.setPadding(new Insets(20, 20, 20, 20));

        titleBox = new HBox();
        titleLabel = new Label("Planning Poker Session");
        titleLabel.setStyle(titleStyle);
        titleBox.getChildren().add(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(titleBox);

        // User Info Setup
        HBox userInfoBox = new HBox();
        
        userName = Session.getInstance().getUsername();
        userNameLabel = new Label("User: " + userName + " ");
        userNameLabel.setStyle(labelStyleBold);
        userCountLabel = new Label("User Count: " + userCount);
        userCountLabel.setStyle(labelStyleBold);
        userInfoBox.getChildren().addAll(userNameLabel, userCountLabel);
        userInfoBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(userInfoBox);

        sessionBox = new HBox(80);
        newSessionBox = new VBox(5);
        newSessionLabel = new Label("Create New Session");
        newSessionButton = new Button("Create Session");
        newSessionButton.setStyle(buttonStyle);
        newSessionButton.setOnAction(e -> handleNewSession());
        newSessionBox.getChildren().add(newSessionButton);
        newSessionBox.setAlignment(Pos.CENTER);
        
        phaseOneBox = new HBox(100);
        gridPane = new GridPane();
        gridPane.setHgap(80); // Horizontal gap between columns
        gridPane.setVgap(10); // Vertical gap between rows
        chooseProjectLabel = new Label("Choose Project");
        chooseProjectLabel.setStyle(labelStyleBold);
        GridPane.setConstraints(chooseProjectLabel, 0, 0);
        projectListButton.setPromptText("Project List");
        projectListButton.setStyle(buttonStyle);
        projectLabel = new Label("Current Project:");
        projectListButton.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Update the Label's text to the selected item
        	projectLabel.setText("Current Project:" + newVal);
            sessionButton.setText("Start Session");
        });
        GridPane.setConstraints(projectListButton, 0, 1);
        projectLabel.setStyle(labelStyleBold);
        projectLabel.setStyle(labelStyleBold);
        GridPane.setConstraints(projectLabel, 1, 0);
        gridPane.getChildren().addAll(chooseProjectLabel, projectListButton, projectLabel);
        phaseOneBox.getChildren().addAll(gridPane);
        phaseOneBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(phaseOneBox);
        phaseOneBox.setVisible(false);

        importSessionBox = new VBox(5);
        importSessionLabel = new Label("Import Existing Session");
        importSessionButton = new Button("Import Session");
        importSessionButton.setStyle(buttonStyle);
        importSessionButton.setOnAction(e -> handleImportSession());
        importSessionBox.getChildren().add(importSessionButton);
        importSessionBox.setAlignment(Pos.CENTER);

        sessionBox.getChildren().addAll(newSessionBox, importSessionBox);
        sessionBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(sessionBox);

        // Current Story
        currentStoryBox = new VBox(5);
        currentStoryLabel = new Label("Current Story:");
        currentStoryLabel.setStyle(titleStyle);
        currentStoryBox.getChildren().add(currentStoryLabel);
        currentStoryBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(currentStoryBox);

        // Card Buttons
        cardButtonsBox = new HBox(40);
        setupCardButtons(new String[]{"1", "4", "10", "35", "99"});
        cardButtonsBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(cardButtonsBox);

        bottomActions = new HBox(10);
        bottomActions.setAlignment(Pos.CENTER);
        sessionButton = new Button("Select or Create Session");
        sessionButton.setStyle(buttonStyle);
        sessionButton.setOnAction(e -> handleSessionAction());

        nextButton = new Button("Next Story");
        nextButton.setStyle(buttonStyle);
        nextButton.setOnAction(e -> nextStory());

        bottomActions.getChildren().addAll(sessionButton, nextButton);
        layout.getChildren().add(bottomActions);

        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
    }
    
    private void promptForStories() {
        boolean continueAdding = true;

        while (continueAdding) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("New Story Details");
            dialog.setHeaderText("Enter the story details");

            ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            Node storyInputForm = createStoryInputForm();
            dialog.getDialogPane().setContent(storyInputForm);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == saveButtonType) {
                TextField titleField = (TextField) storyInputForm.lookup("#storyTitle");
                TextArea descriptionArea = (TextArea) storyInputForm.lookup("#storyDescription");
                String storyTitle = titleField.getText();
                String storyDescription = descriptionArea.getText();

                if (!storyTitle.isEmpty() && !storyDescription.isEmpty()) {
                    storiesToVoteOn.add(new UserStory(storyTitle, storyDescription));
                }
                // Prompt again for more stories
                continueAdding = true;
            } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                continueAdding = false; // Stop adding more stories
            }
        }

        if (!storiesToVoteOn.isEmpty()) {
            startVotingProcess();
        }
    }
    
    private Node createStoryInputForm() {
        VBox form = new VBox(10);

        TextField titleField = new TextField();
        titleField.setId("storyTitle");
        titleField.setPromptText("Story Title");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setId("storyDescription");
        descriptionArea.setPromptText("Story Description");

        form.getChildren().addAll(titleField, descriptionArea);
        return form;
    }
    
    //create a new story
    private void promptForStoryDetails() {
        TextInputDialog storyTitleDialog = new TextInputDialog();
        storyTitleDialog.setTitle("New Story");
        storyTitleDialog.setHeaderText("Enter Story Title");
        storyTitleDialog.setContentText("Title:");
        Optional<String> titleResult = storyTitleDialog.showAndWait();
        titleResult.ifPresent(title -> currentStoryTitle = title);

        TextInputDialog storyDescriptionDialog = new TextInputDialog();
        storyDescriptionDialog.setTitle("New Story");
        storyDescriptionDialog.setHeaderText("Enter Story Description");
        storyDescriptionDialog.setContentText("Description:");
        Optional<String> descriptionResult = storyDescriptionDialog.showAndWait();
        descriptionResult.ifPresent(description -> currentStoryDescription = description);

        if (titleResult.isPresent() && descriptionResult.isPresent()) {
            currentStory = currentStoryTitle;
            currentStoryLabel.setText("Current Story: " + currentStoryTitle);
            hasVoted = false; // Reset voting for the new story
        }
    }
    
    private void startVotingProcess() {
        if (!storiesToVoteOn.isEmpty()) {
            UserStory firstStory = storiesToVoteOn.get(0);
            currentStory = firstStory.getTitle();
            currentStoryLabel.setText("Current Story: " + currentStory);
            hasVoted = false;
            sessionButton.setText("Finish Session");
            sessionStarted = true;
        }
    }

    // Inner class to represent a vote
    private class Vote {
        String userName;
        String currentStory;
        int storyID;
        int cardNumber;

        Vote(String userName, String currentStory, int storyID, int cardNumber) {
            this.userName = userName;
            this.currentStory = currentStory;
            this.storyID = storyID;
            this.cardNumber = cardNumber;
        }
    }
    
    public class UserStory {
        private String title;
        private String description;

        public UserStory(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
    
    private void setupCardButtons(String[] cardNames) {
        String[] colors = {"#FFADAD", "#FFD6A5", "#FDFFB6", "#CAFFBF", "#9BF6FF", "#A0C4FF", "#BDB2FF", "#FFC6FF", "#FFAFCC", "#BDBDBD"};
        cardButtons = new Button[cardNames.length];
        
        for (int i = 0; i < cardNames.length; i++) {
            cardButtons[i] = new Button(cardNames[i]);
            
            String color = colors[i % colors.length]; // This will loop through colors if there are more cardNames than colors
            cardButtons[i].setStyle("-fx-background-color: " + color + "; "
            	    + "-fx-text-fill: #000000; "
            	    + "-fx-border-radius: 15px; "
            	    + "-fx-background-radius: 15px; "
            	    + "-fx-font-family: 'Roboto'; "
            	    + "-fx-font-size: 28pt; "
            	    + "-fx-min-width: 125px; "  
            	    + "-fx-min-height: 210px; " 
            	    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 3, 3);");

            int cardValue = Integer.parseInt(cardNames[i]);
            cardButtons[i].setOnAction(e -> handleCardClick(cardValue));
            
            cardButtonsBox.getChildren().add(cardButtons[i]);
        }
    }
    
    private void handleNewSession() {
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Location to Save Session Data");
        fileChooser.setInitialFileName("session_data.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        savedFile = fileChooser.showSaveDialog(stage);
        fileNameWithoutExtension = savedFile.getName().replace(".txt", "");

        if (savedFile == null) {
            return; // User didn't choose a file, so we exit.
        }

        try {
            if (!savedFile.exists()) {
                savedFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return; // Exit if there was an error creating the file.
        }
        
        newSessionButton.setDisable(true);
        importSessionButton.setDisable(true);
        sessionButton.setText("Choose Project");
        userCount++;
        userCountLabel.setText("User Count: " + userCount);
        phaseOneBox.setVisible(true);
        sessionBox.setVisible(false);
    }

    private void handleImportSession() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Session Data to Import");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) {
            return; // User didn't choose a file, so we exit.
        }

        savedFile = selectedFile;  // Set the selected file as the file to be updated
        
        fileNameWithoutExtension = savedFile.getName().replace(".txt", "");
        
        storyID = countEntriesInFile(selectedFile) + 1;  // Updated this line to correctly set the storyID
        
        newSessionButton.setDisable(true);
        importSessionButton.setDisable(true);
        sessionButton.setText("Choose Project");
        userCount++;
        userCountLabel.setText("User Count: " + userCount);
        phaseOneBox.setVisible(true);
        sessionBox.setVisible(false);
    }

    private int countEntriesInFile(File file) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }


    private void handleSessionAction() {
        String buttonText = sessionButton.getText();

        switch (buttonText) {
            case "Select or Create Session":
                showAlert("Session Error", "Please select or create a session before starting");
                break;
            case "Choose Project":
                showAlert("Session Error", "Please select a project before starting");
                break;
            case "Start Session":
                if (savedFile == null) {
                    showAlert("Session Error", "Please select or create a session file before starting");
                    return; // User didn't choose a file, so we exit.
                }
                promptForStories();
                if (!storiesToVoteOn.isEmpty()) {
                    startVotingProcess(); // Start voting with the first story
                } else {
                    showAlert("Session Error", "No stories to vote on. Please add stories.");
                    return;
                }
                sessionButton.setText("Finish Session");
                sessionStarted = true;
                break;
            case "Finish Session":
                calculateAndDisplayAveragePoints();
                saveToFile();
                newSessionButton.setDisable(false);
                importSessionButton.setDisable(false);
                nextButton.setDisable(false);
                sessionStarted = false;
                hasVoted = false;
                userVotes.clear();
                currentStoryLabel.setText("Current Story:");
                sessionBox.setVisible(true);
                phaseOneBox.setVisible(false);
                Session.getInstance().logout();
                break;
        }
    }

    private void promptForUserName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("User Name Input");
        dialog.setHeaderText("Enter your name");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> userName = name);
    }
    
    private void handleCardClick(int cardValue) {
        if (hasVoted) {
            showAlert("Vote Error", "You have already voted on this story, please wait");
            return;
        }

        if (!sessionStarted) {
            showAlert("Session Error", "Please start a session before voting");
            return;
        }

        Vote vote = new Vote(userName, currentStory, storyID, cardValue);
        userVotes.add(vote);
        storyPoints.computeIfAbsent(storyID, k -> new ArrayList<>()).add(cardValue);
        hasVoted = true;
        saveToFile();
    }


    private void nextStory() {
        if (!sessionStarted) {
            showAlert("Session Error", "Please start a session before advancing");
            return;
        }
        if (!hasVoted) {
            showAlert("Session Error", "Please choose a card before advancing");
            return;
        }

        if (!storiesToVoteOn.isEmpty()) {
            storiesToVoteOn.remove(0); // Remove the voted story only if the list is not empty
        }

        if (!storiesToVoteOn.isEmpty()) {
            UserStory nextStory = storiesToVoteOn.get(0);
            currentStory = nextStory.getTitle();
            currentStoryLabel.setText("Current Story: " + currentStory); // Update label with story title
            storyID++; // Increment storyID for the next story
            hasVoted = false; // Reset voting for the next story
        } else {
            showAlert("Session Complete", "All stories have been voted on.");
            finishSession();
        }
    }
    
    private void finishSession() {
        sessionButton.setText("Session Finished");
        newSessionButton.setDisable(false);
        importSessionButton.setDisable(false);
        nextButton.setDisable(true);
        sessionStarted = false;
        calculateAndDisplayAveragePoints();
    }

    
    private void saveToFile() {
        if (savedFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedFile, true))) {
                for (Vote vote : userVotes) {
                    writer.write("user: " + vote.userName + "\n");
                    writer.write("Story: " + vote.currentStory + " Vote: " + vote.cardNumber + "\n");
                }
                userVotes.clear();
            } catch (IOException e) {
                showAlert("Error", "An error occurred while saving the file.");
            }
        }
    }
    
    private void calculateAndDisplayAveragePoints() {
        StringBuilder resultBuilder = new StringBuilder("Average Story Points:\n");
        for (Map.Entry<Integer, ArrayList<Integer>> entry : storyPoints.entrySet()) {
            int storyID = entry.getKey();
            ArrayList<Integer> points = entry.getValue();
            double average = points.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            resultBuilder.append("Story ").append(storyID).append(": ").append(String.format("%.2f", average)).append("\n");
        }
        showAlert("Session Summary", resultBuilder.toString());
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public void showWindow() {
        stage.show();
    }
}