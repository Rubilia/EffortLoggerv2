package Prototype;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class EffortLoggerUI extends Application {

	public VBox createPrototypeBox(String prototypeName, String descriptionText) {
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

	    // Create Launch Button
	    Button launchButton = new Button("Launch");
	    launchButton.setPrefWidth(100);
	    
	    String buttonBaseStyle = "-fx-background-color: linear-gradient(#007BFF, #0053a6); -fx-text-fill: white; -fx-font-size: 12px; -fx-border-radius: 15px; -fx-background-radius: 15px;";
	    String buttonPressedStyle = "-fx-background-color: linear-gradient(#009BFF, #0073a6); -fx-scale-x: 0.95; -fx-scale-y: 0.95;";
	    
	    launchButton.setStyle(buttonBaseStyle);

	    launchButton.setOnMousePressed(e -> launchButton.setStyle(buttonPressedStyle));
	    launchButton.setOnMouseReleased(e -> launchButton.setStyle(buttonBaseStyle));

	    // VBox for holding each prototype box's content
	    VBox prototypeBoxContent = new VBox(10, title, descriptionFlow, launchButton);
	    prototypeBoxContent.setAlignment(Pos.CENTER);
	    prototypeBoxContent.setPadding(new Insets(10));
	    prototypeBoxContent.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 15px; -fx-background-color: linear-gradient(#ffffff, #f3f3f3); -fx-background-radius: 15px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 5);");
	    return prototypeBoxContent;
	}

    @Override
    public void start(Stage stage) {
        stage.setTitle("EffortLogger Prototype");

        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(10));

        Label riskReductionTitle = new Label("Risk reduction prototypes");
        riskReductionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        riskReductionTitle.setAlignment(Pos.CENTER);

        VBox riskReductionContainer = new VBox(15);
        riskReductionContainer.setPadding(new Insets(10));

        // Create the prototype boxes
        VBox prototypeBox1 = createPrototypeBox("Initial Prototype", "Description for Initial Prototype");
        VBox prototypeBox2 = createPrototypeBox("Prototype 2", "Description for Prototype 2");

        riskReductionContainer.getChildren().addAll(prototypeBox1, prototypeBox2);

        mainContainer.getChildren().addAll(riskReductionTitle, riskReductionContainer);

        Scene scene = new Scene(mainContainer, 350, 400); // Adjusted the height
        stage.setScene(scene);

        stage.setMinWidth(375);  // Set minimum width
        stage.setMinHeight(425);  // Adjusted minimum height
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
