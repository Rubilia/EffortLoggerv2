package Prototype;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.application.Platform;

public class EffortLoggerUI extends Application {
	private Stage primaryStage;

	public VBox createPrototypeBox(String prototypeName, String descriptionText, String authorName, Runnable buttonAction) {
    	// Prototype Name
	    Label title = new Label(prototypeName);
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	    title.setStyle("-fx-text-fill: #333;");
	    title.setAlignment(Pos.CENTER);

	    // Description
	    Text descriptionLabel = new Text("Description: ");
	    descriptionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

	    Text description = new Text(descriptionText);
	    description.setWrappingWidth(230);  // Adjusted wrapping width
	    description.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	    
	    TextFlow descriptionFlow = new TextFlow(descriptionLabel, description);

        // Author
        Label authorLabel = new Label("Author: " + authorName);
        authorLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        authorLabel.setStyle("-fx-text-fill: #555;");

	    // Create Launch Button
	    Button launchButton = new Button("Launch");
	    launchButton.setPrefWidth(100);
	    launchButton.setOnAction(e -> buttonAction.run());
	    
	    String buttonBaseStyle = "-fx-background-color: linear-gradient(#007BFF, #0053a6); -fx-text-fill: white; -fx-font-size: 12px; -fx-border-radius: 15px; -fx-background-radius: 15px;";
	    String buttonPressedStyle = "-fx-background-color: linear-gradient(#009BFF, #0073a6); -fx-scale-x: 0.95; -fx-scale-y: 0.95;";
	    launchButton.setStyle(buttonBaseStyle);
	    launchButton.setOnMousePressed(e -> launchButton.setStyle(buttonPressedStyle));
	    launchButton.setOnMouseReleased(e -> launchButton.setStyle(buttonBaseStyle));

	    // VBox for holding each prototype box's content
        VBox prototypeBoxContent = new VBox(10, title, descriptionFlow, authorLabel, launchButton);
        prototypeBoxContent.setAlignment(Pos.CENTER);
	    prototypeBoxContent.setPadding(new Insets(10));
	    prototypeBoxContent.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 15px; -fx-background-color: linear-gradient(#ffffff, #f3f3f3); -fx-background-radius: 15px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 5);");
	    return prototypeBoxContent;
	}

    @Override
    public void start(Stage stage) {
    	this.primaryStage = stage;

        stage.setTitle("EffortLogger Prototype");

        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(10));

        Label riskReductionTitle = new Label("Risk reduction prototypes");
        riskReductionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        riskReductionTitle.setAlignment(Pos.CENTER);

        VBox riskReductionContainer = new VBox(15);
        riskReductionContainer.setPadding(new Insets(10));
        
        // Edit these lines to specify information about your prototypes
        riskReductionContainer.getChildren().addAll(
                createPrototypeBox("Concurrent Editing Prototype", "Concurrent editing allows multiple users to work simultaneously. EffortLogger ensures changes are accurate and avoids overwrites.", "Ilia Rubashkin", StartConcurrentEditingPrototype()),
                createPrototypeBox("Prototype 2", "Description for Prototype 2", "Author2", StartPrototypeTwo()),
                createPrototypeBox("Prototype 3", "Description for Prototype 3", "Author3", StartPrototypeThree()),
                createPrototypeBox("Prototype 4", "Description for Prototype 4", "Author4", StartPrototypeFour()),
                createPrototypeBox("Prototype 5", "Description for Prototype 5", "Author5", StartPrototypeFive())
        );

        mainContainer.getChildren().addAll(riskReductionTitle, riskReductionContainer);

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true); // to ensure the content adjusts to the width of the ScrollPane
        scrollPane.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: transparent; ");

        Scene scene = new Scene(scrollPane, 450, 400);  // width increased to 450
        stage.setScene(scene);

        stage.setMinWidth(500);  // Set the minimum width to 500
        stage.setMinHeight(650);  // Set the minimum height

        stage.show();

        // Use Platform.runLater to defer the lookup styling
        Platform.runLater(() -> {
            scrollPane.lookup(".scroll-bar").setStyle("-fx-background-color: transparent;");
            scrollPane.lookup(".scroll-bar .thumb").setStyle("-fx-background-color: rgba(0, 123, 255, 0.5); -fx-background-radius: 2;");
        });
    }
    
	    private Runnable StartConcurrentEditingPrototype() {
	        return () -> {
	            ConcurrentEditingPrototype prototypeWindow = new ConcurrentEditingPrototype(this.primaryStage);
	            prototypeWindow.showWindow();
	        };
	    }
	    
	    private Runnable StartPrototypeTwo() {
	        return () -> {
	            // Start your prototype
	        };
	    }
	    
	    private Runnable StartPrototypeThree() {
	        return () -> {
	            // Start your prototype
	        };
	    }
    
	    private Runnable StartPrototypeFour() {
	        return () -> {
	            // Start your prototype
	        };
	    }
	    
	    private Runnable StartPrototypeFive() {
	        return () -> {
	            // Start your prototype
	        };
	    }
    
        public static void main(String[] args) {
            launch(args);
        }
    }
