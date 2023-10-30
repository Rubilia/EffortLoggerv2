package Prototype;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class PortfolioOverview {

    private Stage stage;
    private TableView<Project> notStartedTableView = new TableView<>();
    private ListView<String> inProgressListView = new ListView<>();
    private ListView<String> doneListView = new ListView<>();
    private int projectIdCounter = 1;  // Starts from 1 and increments with each new project

    public PortfolioOverview(Stage primaryStage) {
        this.stage = new Stage();
        stage.initOwner(primaryStage);
    }

    public void showWindow() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ffffff;");

        TableColumn<Project, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().projectIdProperty());
        TableColumn<Project, String> nameColumn = new TableColumn<>("Project Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().projectNameProperty());
        TableColumn<Project, String> assigneeColumn = new TableColumn<>("Assignee");
        assigneeColumn.setCellValueFactory(cellData -> cellData.getValue().assigneeProperty());
        TableColumn<Project, String> weightColumn = new TableColumn<>("Weight");
        weightColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asString());
        notStartedTableView.getColumns().addAll(idColumn, nameColumn, assigneeColumn, weightColumn);
        notStartedTableView.setItems(getDummyData());  // Load dummy data

        VBox notStartedBox = createProjectList("Not Started", notStartedTableView, "#f4f4f4");  // Light grey
        HBox projectLists = new HBox(10, notStartedBox, createProjectList("In Progress", inProgressListView, "#e6e6e6"), createProjectList("Done / In Review", doneListView, "#d9d9d9"));  // Shades of grey

        Button addButton = createButton("Add Project", e -> handleAddProject(), "#3498db");  // Vibrant blue
        Button moveAheadButton = createButton("Move Ahead", e -> handleMoveAhead(), "#3498db");  // Vibrant blue

        HBox buttonBox = new HBox(10, addButton, moveAheadButton);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(projectLists, buttonBox);

        stage.setTitle("Portfolio Overview Prototype");
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private VBox createProjectList(String title, Control control, String backgroundColor) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        VBox box = new VBox(5, titleLabel, control);
        box.setStyle("-fx-background-color: " + backgroundColor + "; -fx-padding: 10; -fx-border-radius: 10; -fx-border-color: #cccccc;");
        return box;
    }
    
    private Button createButton(String text, EventHandler<ActionEvent> handler, String backgroundColor) {
        Button button = new Button(text);
        button.setOnAction(handler);
        button.setStyle(
                "-fx-background-color: " + backgroundColor + "; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;" +
                        "-fx-padding: 5 20 5 20;" +
                        "-fx-border-color: #cccccc;"
        );

        return button;
    }
    
    private void handleMoveAhead() {
        Project selectedProject = notStartedTableView.getSelectionModel().getSelectedItem();
        String selectedInProgress = inProgressListView.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            // If a project is selected in the 'Not Started' table, move it to 'In Progress'
            notStartedTableView.getItems().remove(selectedProject);
            inProgressListView.getItems().add(selectedProject.getProjectName() + " (Assigned to: " + selectedProject.getAssignee() + ")");
            notStartedTableView.getSelectionModel().clearSelection();
        } else if (selectedInProgress != null) {
            // If a project is selected in the 'In Progress' list, move it to 'Done / In Review'
            inProgressListView.getItems().remove(selectedInProgress);
            doneListView.getItems().add(selectedInProgress);  // Assuming the selectedInProgress already contains the assignee name
            inProgressListView.getSelectionModel().clearSelection();
        }
    }

    private void handleAddProject() {
        Dialog<Project> dialog = new Dialog<>();
        dialog.setTitle("Add New Project");
        dialog.setHeaderText("Enter Project and Assignee Information");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField projectName = new TextField();
        projectName.setPromptText("Project Name");
        TextField assigneeName = new TextField();
        assigneeName.setPromptText("Assignee Name");

        ComboBox<Integer> weightComboBox = new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        grid.add(new Label("Project Name:"), 0, 0);
        grid.add(projectName, 1, 0);
        grid.add(new Label("Assignee Name:"), 0, 1);
        grid.add(assigneeName, 1, 1);
        grid.add(new Label("Weight:"), 0, 2);
        grid.add(weightComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                int weight = weightComboBox.getSelectionModel().getSelectedItem() != null ? weightComboBox.getSelectionModel().getSelectedItem() : 1;
                return new Project(projectIdCounter++, projectName.getText(), assigneeName.getText(), weight);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(project -> {
            if (!project.getProjectName().isEmpty() && !project.getAssignee().isEmpty()) {
                notStartedTableView.getItems().add(project);
            }
        });
    }

    private ObservableList<Project> getDummyData() {
        return FXCollections.observableArrayList(
                new Project(1, "Project Alpha", "Alice", 3),
                new Project(2, "Project Beta", "Bob", 4),
                new Project(3, "Project Gamma", "Charlie", 2),
                new Project(4, "Project Sigma", "Ava", 1)
        );
    }

    // Inner class to represent a project
    public static class Project {
        private SimpleIntegerProperty projectId;
        private SimpleStringProperty projectName;
        private SimpleStringProperty assignee;
        private SimpleIntegerProperty weight;

        public Project(int projectId, String projectName, String assignee, int weight) {
            this.projectId = new SimpleIntegerProperty(projectId);
            this.projectName = new SimpleStringProperty(projectName);
            this.assignee = new SimpleStringProperty(assignee);
            this.weight = new SimpleIntegerProperty(weight);
        }

        public int getProjectId() {
            return projectId.get();
        }

        public SimpleIntegerProperty projectIdProperty() {
            return projectId;
        }

        public String getProjectName() {
            return projectName.get();
        }

        public SimpleStringProperty projectNameProperty() {
            return projectName;
        }

        public String getAssignee() {
            return assignee.get();
        }

        public SimpleStringProperty assigneeProperty() {
            return assignee;
        }

        public int getWeight() {
            return weight.get();
        }

        public SimpleIntegerProperty weightProperty() {
            return weight;
        }
        
        @Override
        public String toString() {
            return String.format("%s (Assigned to: %s)", getProjectName(), getAssignee());
        }
        
    }

}
