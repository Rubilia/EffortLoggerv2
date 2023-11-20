package Prototype;

import javafx.stage.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


import Prototype.Data.Data;
import Prototype.Data.Project;
import Prototype.Data.Task;

public class LoginAuthentication  {
	class user { //class to hold sample user for testing 
		String username;
		String password;
		int id;
		
		
		user(String user, String pass) { //user constructor
			this.username = user;
			this.password = pass;
		}
	}
	//sample users
	//
	private ArrayList<user> USERS = new ArrayList<>();
	// Stages
	private Stage pokerStage, stage, loginStage, signUpStage, primaryStage, loginSuccessStage, lockdownStage;

    // Buttons
    private Button loginButton, signupButton, loginConfirmButton, signupConfirmButton;

    // Text Fields
    private TextField userIn, passIn;
    private TextField userField, passField;

    // Labels
    private Label userLabel, passLabel;
    private Label errorLabel, errorLabel1, errorLabel2;

    // VBoxes and HBoxes
    private VBox centerLayout, centerBox, signUpLayout;
    private HBox userBox, passBox;
    
    String buttonStyle = "-fx-background-color: #4E90A4; -fx-text-fill: #FFFFFF; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 14pt;";

    private int loginCount;
    
    private static final String USER_PATH = "Assets" + File.separator + "users.txt";

    public LoginAuthentication(Stage primaryStage) {
    	
        this.primaryStage = primaryStage;
        this.stage = new Stage(); // Initialize the stage
        setupUI();
    }


    private void setupUI() {
    	//add example user to approved user list
  
        stage.setTitle("Tu 44 Effort Log Login/SignUp");
        stage.setResizable(false);
        
        String buttonStyle = "-fx-background-color: #4E90A4; -fx-text-fill: #FFFFFF; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 14pt;";
        
        loginButton = new Button("Login");
        loginButton.setStyle(buttonStyle);
        
        loginButton.setOnAction(e -> { //button that navigates to the login view 
            navigateToLogin();
        });
        
        signupButton = new Button("Sign Up");
        signupButton.setStyle(buttonStyle);
        
        signupButton.setOnAction(e -> { //button that navigates to the signup view
            navigateToSignUp();
        });

        centerLayout = new VBox(50);
        centerLayout.setAlignment(Pos.CENTER);
        centerLayout.getChildren().addAll(loginButton, signupButton);

        Scene scene = new Scene(centerLayout, 500, 500);
        stage.setScene(scene);
        stage.show();
    }


    private void navigateToLogin() {
    	// Create the login stage and scene if it doesn't exist
        if (loginStage == null) {
            //attempted login counter
        	loginCount = 0;
        	
            loginStage = new Stage();
            loginStage.setTitle("Login");

            
            userLabel = new Label();
            userLabel.setText("Username");

            userIn = new TextField();
            userIn.setPromptText("Enter username");

            passLabel = new Label();
            passLabel.setText("Password");

            passIn = new TextField();
            passIn.setPromptText("Enter password");

            loginConfirmButton = new Button();
            loginConfirmButton.setText("Login");
            loginConfirmButton.setStyle(buttonStyle);

            userBox = new HBox(50);
            userBox.setAlignment(Pos.CENTER);
            userBox.setPadding(new Insets(1));
            userBox.getChildren().addAll(userLabel, userIn);

            passBox = new HBox(50);
            passBox.setAlignment(Pos.CENTER);
            passBox.setPadding(new Insets(1));
            passBox.getChildren().addAll(passLabel, passIn);
            
            
            //Auth level hierarchy options
            ComboBox<String> userLevelComboBox = new ComboBox<>();
            ObservableList<String> userLevels = FXCollections.observableArrayList(
                "Entry Level",
                "Junior Level",
                "Manager",
                "Team Lead"
            );
            userLevelComboBox.setItems(userLevels);
            userLevelComboBox.setPromptText("Select User Level");

            centerBox = new VBox(25);
            centerBox.setAlignment(Pos.CENTER);
            centerBox.setPadding(new Insets(30));
            centerBox.getChildren().addAll(userBox, passBox, userLevelComboBox, loginConfirmButton);

            errorLabel = new Label(); // Create a label for error messages
            centerBox.getChildren().add(errorLabel); // Add it to the UI
            
            
            loginConfirmButton.setOnAction(e -> { //Button to test the inputs and confirm if they are valid 
                String userText = userIn.getText();
                String passText = passIn.getText();
                if (loginCount == 5) {
                	navigateToLockDown();
                }
                loginCount++;
                try {
                    if (authenticateUser(userText, passText)) {
                        errorLabel.setText(""); // Clear any previous error message
                        StartPokerEffortLoggerMenu().run();
                        loginStage.close();
                         //if successful navigate to login accepted view
                    } else {
                        // Display "Access Denied" message in red
                        errorLabel.setText("Access Denied");
                        errorLabel.setTextFill(Color.RED);
                    }
                } catch (Throwable ex) {
                    // Handle exceptions by displaying error messages in the label
                    errorLabel.setText(ex.getMessage());
                    errorLabel.setTextFill(Color.RED);
                }
            });

            Scene loginScene = new Scene(centerBox, 750, 750);
            loginStage.setScene(loginScene);
        }

        loginStage.show();
        stage.hide();
    }
    
    private void navigateToLockDown() {
    	//create stage if not already made 
        if (lockdownStage == null) {
            lockdownStage = new Stage();
            lockdownStage.setTitle("Lock Down");

            // Create a Text element with the message
            Text messageText = new Text("Login Attempted too many times\nPlease wait.");

            // Set the font size and alignment
            messageText.setStyle("-fx-font-size: 18pt;");
            messageText.setTextAlignment(TextAlignment.CENTER);

            // Create a StackPane to center the Text
            StackPane root = new StackPane(messageText);

            // Create a Scene
            Scene scene = new Scene(root, 400, 200);

            // Set the Scene for the Stage
            lockdownStage.setScene(scene);
        }

        lockdownStage.show();
        loginStage.close();
        primaryStage.close();
    }

    
    
    private void navigateToLoginAccepted() {
    	loginSuccessStage = new Stage();
        loginSuccessStage.setTitle("Login Success View");
        Text text = new Text("User Authenticated");
        text.setStyle("-fx-font-size: 24pt;"); 

        // Create a StackPane to center the Text
        StackPane root = new StackPane(text);

        Scene scene = new Scene(root, 400, 400); 

        loginSuccessStage.setScene(scene);

        loginSuccessStage.show();

        primaryStage.hide();
    }

    private List<user> loadUsersFromFile(String filePath) throws IOException {
        List<user> users = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" "); // Split by space
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    users.add(new user(username, password));
                }
            }
        }
        
        return users;
    }

    private boolean authenticateUser(String username, String password) throws IOException {
        List<user> authorizedUsers = loadUsersFromFile(USER_PATH);
        
        for (user currentUser : authorizedUsers) {
            if (currentUser.username.equals(username) && currentUser.password.equals(password)) {
            	
            	Session.getInstance().setUsername(currentUser.username);
                return true;
            }
        }
        
        return false;
    }


    private boolean authUserSignUp(String in) throws Throwable { //function to validate input username meets all criteria for a valid input
        //length of input must be greater than 8
    	if (in.length() < 8) {
            throw new InputLengthException();
        }
        
        int nCount = 0;
        int cCount = 0;
        
        for (char ch : in.toCharArray()) { //counter for digits and characters
            if (Character.isDigit(ch)) {
                nCount++;
            } else if (Character.isLetter(ch)) {
                cCount++;
            } else {
                throw new InputCharInvalidException(); //if character isn't a letter or digit an error is thrown 
            }
        }

        if (nCount < 2) {
            throw new InputDigitCountException(); //if there is less than two digits an error is thrown
        }
        
        if (cCount < 6) {
            throw new InputCharCountException(); //if there is less than 6 characters an error is thrown 
        }

        return true;
    }

    private boolean authPassSignUp(String in) throws Throwable { //Same logic as authUserSignUp except one special character is required and only 2 letters or digits is required
        if (in.length() < 8) {
            throw new InputLengthException();
        }
        
        int letterCount = 0;
        int digitCount = 0;
        int specialCharCount = 0;
        
        for (char ch : in.toCharArray()) {
            if (Character.isDigit(ch)) {
                digitCount++;
            } else if (Character.isLetter(ch)) {
                letterCount++;
            } else if (!Character.isWhitespace(ch)) {
                // Assuming any character that is not a letter, digit, or whitespace is a special character
                specialCharCount++;
            }
        }

        if (letterCount < 2) {
            throw new InputCharCountPassException();
        }

        if (digitCount < 2) {
            throw new InputDigitCountException();
        }
        
        if (specialCharCount < 1) {
            throw new InputSpecialSymbolCountException();
        }

        return true;
    }


	

    private void navigateToSignUp() { //Signup view 
    	//create stage if not created 
        if (signUpStage == null) {
            signUpStage = new Stage();
            signUpStage.setTitle("Sign Up");


            userField = new TextField();
            userField.setPromptText("Enter username");

            passField = new TextField();
            passField.setPromptText("Enter password");

            signupConfirmButton = new Button();
            signupConfirmButton.setText("Confirm");
            signupConfirmButton.setStyle(buttonStyle);

            errorLabel1 = new Label();
            errorLabel1.setTextFill(Color.RED);

            errorLabel2 = new Label();
            errorLabel2.setTextFill(Color.RED);
            
            ComboBox<String> userLevelComboBox = new ComboBox<>(); //Auth level hierarchy options 
            ObservableList<String> userLevels = FXCollections.observableArrayList(
                "Entry Level",
                "Junior Level",
                "Manager",
                "Team Lead"
            );
            userLevelComboBox.setItems(userLevels);
            userLevelComboBox.setPromptText("Select User Level");

            signupConfirmButton.setOnAction(e -> { //signup button checks the username and password using the two functions and if it is approved a new user is created and added to the approved user list
                String userText = userField.getText();
                String passText = passField.getText();
                String approvedUsername = null;
                String approvedPassword = null;

                try {
                    errorLabel1.setText("");
                    if (authUserSignUp(userText)) { //validate username 
                        approvedUsername = userText;
                    }
                }
                catch (Throwable ex) {
                    errorLabel1.setText(ex.getMessage());
                }

                try {
                    errorLabel2.setText("");
                    if (authPassSignUp(passText)) { //validate password
                        approvedPassword = passText;
                    }
                } catch (Throwable ex) {
                    errorLabel2.setText(ex.getMessage());
                }

                if (approvedUsername != null && approvedPassword != null) { //if both username and password is validated a new user is created and added to approved users 
                    errorLabel1.setText("");
                    errorLabel2.setText("");
                    user newUser = new user(approvedUsername, approvedPassword);
                    USERS.add(newUser);
                    navigateToLoginAccepted();
                }
            });

            userBox = new HBox(20);
            userBox.setAlignment(Pos.CENTER); // Center alignment
            userLabel = new Label();
            userLabel.setText("Username");

            passBox = new HBox(20);
            passBox.setAlignment(Pos.CENTER); // Center alignment
            passLabel = new Label();
            passLabel.setText("Password");

            userBox.getChildren().addAll(userLabel, userField);
            passBox.getChildren().addAll(passLabel, passField);

            signUpLayout = new VBox(25);
            signUpLayout.setAlignment(Pos.CENTER); // Center alignment
            signUpLayout.setPadding(new Insets(10));
            signUpLayout.getChildren().addAll(userBox, passBox, userLevelComboBox, signupConfirmButton, errorLabel1, errorLabel2);

            Scene signUpScene = new Scene(signUpLayout, 750, 750);
            signUpStage.setScene(signUpScene);
        }

        signUpStage.show();
        primaryStage.close();
        stage.close();
    }

    private Runnable StartPokerEffortLoggerMenu() {
        return () -> {
        	Poker_EffortLogger_Menu prototypeWindow = new Poker_EffortLogger_Menu();
            prototypeWindow.showWindow();
        };
    }


    
    public class InputLengthException extends Exception { //exception for inputs less than 8 chars
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InputLengthException() {
            super("Input must be at least 8 characters long.");
        }
    }
    
    public class InputCharInvalidException extends Exception { //exception for using symbols in input 
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InputCharInvalidException() {
           super("Input must contain only letters (a-z & A-Z) or digits (0-9)");
        }
    }

    public class InputDigitCountException extends Exception { //exception for using less than two digits in input
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InputDigitCountException() {
           super("Input must contain at least 2 digits (0-9)");
        }
    }
    
    public class InputCharCountException extends Exception { //exception for using less than 6 letters in username 
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InputCharCountException() {
           super("Input must contain at least 6 letters (a-z & A-Z)");
        }
    }
    
    public class InputSpecialSymbolCountException extends Exception { //exception for using non letter or digit characters in username input 

		private static final long serialVersionUID = 1L;
		
		public InputSpecialSymbolCountException() {
			super("Input must have at least 1 symbol");
		}
    }
    
    public class InputCharCountPassException extends Exception { //exception for using non letter or digit characters in username input 

		private static final long serialVersionUID = 1L;
		
		public InputCharCountPassException() {
			super("Input must have at least 2 letters");
		}
    }
    
    
      
    public void showWindow() { //function to display the main view
    	primaryStage.close();
    	stage.show();
    }
}

