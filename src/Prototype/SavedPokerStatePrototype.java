/**
 * Author: Josh Camberg
 * Copyright © 2023 Josh Camberg. All rights reserved.
 * 
 * Description: This file and its contents are the property of Josh Camberg.
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
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SavedPokerStatePrototype extends Stage {
    private Stage stage;
    private Stage primaryStage;

    private VBox layout, currentStoryBox, importSessionBox, newSessionBox;
    private HBox titleBox, bottomActions, cardButtonsBox, margin, sessionBox;
    private Label titleLabel, currentStoryLabel, voteLabel, importSessionLabel, newSessionLabel;
    private Pane spacer;
    private Button[] cardButtons;
    private Button nextButton, sessionButton, importSessionButton, newSessionButton;
    private int storyID = 1; // To track the current story
    private String fileNameWithoutExtension, currentStory;
    private boolean sessionStarted, hasVoted;
    private ArrayList<Vote> userVotes = new ArrayList<>(); // To store the votes
    
    private File savedFile = null;

    public SavedPokerStatePrototype(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupUI();
    }

    private void setupUI() {
        stage = new Stage();
        stage.setTitle("Planning Poker Session");
        stage.setWidth(900);
        stage.setHeight(820);

        //String comboBoxStyle = "-fx-background-color: #FFFFFF; -fx-border-color: #2D3748; -fx-border-width: 1; -fx-font-family: 'Roboto'; -fx-font-size: 14pt;";
        String labelStyleBold = "-fx-font-family: 'Roboto'; -fx-font-weight: BOLD; -fx-font-size: 14pt; -fx-text-fill: #2D3748;";
        String titleStyleBold = "-fx-font-family: 'Roboto'; -fx-font-weight: BOLD; -fx-font-size: 26pt; -fx-text-fill: #2D3748;";
        String titleStyle = "-fx-font-family: 'Roboto'; -fx-font-weight: BOLD; -fx-font-size: 34pt; -fx-text-fill: #2D3748;";
        String buttonStyle = "-fx-background-color: #48BB78; -fx-text-fill: #FFFFFF; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-family: 'Roboto'; -fx-font-size: 14pt; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 3, 3);";

        layout = new VBox(20);
        layout.setPadding(new Insets(20, 20, 20, 20));

        titleBox = new HBox();
        titleLabel = new Label("Planning Poker Session");
        titleBox.setPadding(new Insets(10, 0, 0, 0));
        titleLabel.setStyle(titleStyle);
        titleBox.getChildren().add(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(titleBox);

        
        
        sessionBox = new HBox(80);
        
        newSessionBox = new VBox(5);
        newSessionLabel = new Label("Create New Session");
        newSessionLabel.setStyle(titleStyleBold);
        newSessionButton = new Button("Create Session");
        newSessionButton.setStyle(buttonStyle);
        newSessionButton.setOnAction(e -> handleNewSession());
        newSessionBox.getChildren().addAll(newSessionLabel, newSessionButton);
        newSessionBox.setAlignment(Pos.CENTER);
        
        importSessionBox = new VBox(5);
        importSessionLabel = new Label("Import Existing Session");
        importSessionLabel.setStyle(titleStyleBold);
        importSessionButton = new Button("Import Session");
        importSessionButton.setStyle(buttonStyle);
        importSessionButton.setOnAction(e -> handleImportSession());
        importSessionBox.getChildren().addAll(importSessionLabel, importSessionButton);
        importSessionBox.setAlignment(Pos.CENTER);
        
        sessionBox.getChildren().addAll(newSessionBox, importSessionBox);
        sessionBox.setAlignment(Pos.CENTER);
        
        layout.getChildren().add(sessionBox);
        
        currentStoryBox = new VBox(5);
        currentStoryLabel = new Label("Current Story:");
        currentStoryBox.setPadding(new Insets(70, 0, 0, 0));
        currentStoryLabel.setStyle(titleStyle);
        voteLabel = new Label("Vote Below");
        voteLabel.setStyle(titleStyleBold);
        currentStoryBox.getChildren().addAll(currentStoryLabel, voteLabel);
        currentStoryBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(currentStoryBox);
        
        cardButtonsBox = new HBox(40);
        setupCardButtons(new String[]{"1", "4", "10", "35", "99"});
        cardButtonsBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(cardButtonsBox);

        bottomActions = new HBox(480);
        bottomActions.setPadding(new Insets(60, 0, 20, 0));
        sessionButton = new Button("Select or Create Session");
        sessionButton.setOnAction(e -> handleSessionAction());
        sessionButton.setStyle(buttonStyle);

        nextButton = new Button("Next Story");
        nextButton.setStyle(buttonStyle);
        nextButton.setOnAction(e -> nextStory());


        bottomActions.getChildren().addAll(sessionButton, nextButton);
        layout.getChildren().add(bottomActions);
        
        margin = new HBox(5);
        spacer = new Pane();
        spacer.setPrefSize(100, 50); // width 100, height 50
        margin.getChildren().add(spacer);
        margin.setAlignment(Pos.CENTER);
        layout.getChildren().add(margin);
        
        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
    }

    // Inner class to represent a vote
    private class Vote {
        String sessionName;
        String currentStory;
        int storyID;
        int cardNumber;

        Vote(String sessionName, String currentStory, int storyID, int cardNumber) {
            this.sessionName = sessionName;
            this.currentStory = currentStory;
            this.storyID = storyID;
            this.cardNumber = cardNumber;
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

        // Optional: Create a new empty file (if it doesn't exist)
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
        sessionButton.setText("Start Session");
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
        sessionButton.setText("Select or Create Session");
        sessionStarted = true;
        currentStoryLabel.setText("Current Story: " + storyID);
        sessionButton.setText("Finish Session");
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
            case "Start Session":
               
                if (savedFile == null) {
                	showAlert("Session Error", "Please select or create a session file before starting");
                	return; // User didn't choose a file, so we exit.
                }

                sessionButton.setText("Finish Session");
                sessionStarted = true;
                currentStoryLabel.setText("Current Story: 1");
                break;
            case "Finish Session":
                saveToFile();
                newSessionButton.setDisable(false);
                importSessionButton.setDisable(false);
                nextButton.setDisable(false);
                sessionStarted = false;
                hasVoted = false;
                userVotes.clear();
                currentStoryLabel.setText("Current Story:");
                break;
        }
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


        userVotes.add(new Vote(fileNameWithoutExtension, currentStory, storyID, cardValue));
        hasVoted = true;
        saveToFile(); // Save automatically when a card is clicked.
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
        saveToFile(); // Save automatically when moving to the next story.
        storyID++;
        currentStoryLabel.setText("Current Story: " + storyID);
        hasVoted = false; // Reset for the next story
    }

    
    private void saveToFile() {
        if (savedFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedFile, true))) {
                for (Vote vote : userVotes) {
                    // Updated the format when writing to the file
                    writer.write(vote.storyID + ". Story: Story" + vote.storyID + " Vote: " + vote.cardNumber + "\n");
                }
                userVotes.clear(); // Clear the votes after saving to avoid duplicates in the file.
            } catch (IOException e) {
                showAlert("Error", "An error occurred while saving the file.");
            }
        }
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
