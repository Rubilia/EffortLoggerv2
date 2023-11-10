/**
 * Author: Ilia Rubashkin
 * Copyright © 2023 Ilia Rubashkin. All rights reserved.
 * 
 * Description: This file and its contents are the property of Ilia Rubashkin.
 * Unauthorized use, modification, or distribution of this file or its contents
 * without explicit permission from the author is strictly prohibited.
 */

package Prototype.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Data implements Serializable {
    private ArrayList<Project> projects;
    private static final long serialVersionUID = 1L;
    private static final String DATA_PATH = "Assets" + File.separator + "data.dat";

    // Constructor
    public Data() {
        this.projects = new ArrayList<>();
        checkAndLoadData();
    }

    // Checks if data file exists and reads it; if not, populates with default data
    private void checkAndLoadData() {
        File dataFile = new File(DATA_PATH);
        if (dataFile.exists() && !dataFile.isDirectory()) { // Check if data file exists
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
                Data data = (Data) ois.readObject(); // Read existing Data object from file
                this.projects = data.projects; // Load the projects
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            populateWithDefaultData(); // If file doesn't exist, populate with default data
        }
    }

    private void populateWithDefaultData() {
        // Assuming that Project, Task, UserLog, etc. classes are defined according to the structure needed
        projects = new ArrayList<>();

        // Sample data filling for "Sample Project 1"
        ArrayList<String> lifeCycleStepsProject1 = new ArrayList<>(Arrays.asList("Design", "Development", "Deployment"));
        ArrayList<String> plansProject1 = new ArrayList<>(Arrays.asList("Agile", "Scrum", "Kanban"));
        ArrayList<String> effortCategoriesProject1 = new ArrayList<>(Arrays.asList("UI/UX Design", "Backend Coding", "Infrastructure"));
        ArrayList<Task> userLogsProject1 = new ArrayList<>();
        
        // UserLog entries for "Sample Project 1"
        Task log1 = new Task("log_001", "Sample Log 1", "Sample description 1", "2023-10-25", "09:00:00", "11:00:00", "Design", "Agile", "Backend Coding", new CurrentUserEditing(""));
        Task log2 = new Task("log_002", "Sample Log 2", "Sample description 2", "2023-10-26", "14:00:00", "16:00:00", "Development", "Scrum", "UI/UX Design", new CurrentUserEditing(""));

        userLogsProject1.add(log1);
        userLogsProject1.add(log2);

        Project sampleProject1 = new Project("project_001", "Sample Project 1", lifeCycleStepsProject1, plansProject1, effortCategoriesProject1, userLogsProject1);

        // Add "Sample Project 1" to projects list
        projects.add(sampleProject1);

        // Repeat the process for "Sample Project 2" with its own data
        ArrayList<String> lifeCycleStepsProject2 = new ArrayList<>(Arrays.asList("Initiation", "Execution", "Closure"));
        ArrayList<String> plansProject2 = new ArrayList<>(Arrays.asList("Waterfall", "Plan 2", "Plan 3"));
        ArrayList<String> effortCategoriesProject2 = new ArrayList<>(Arrays.asList("Requirement Gathering", "Development", "Evaluation"));
        ArrayList<Task> userLogsProject2 = new ArrayList<>();

        // UserLog entries for "Sample Project 2"
        Task log3 = new Task("log_003", "Sample Log 3", "Sample description 3", "2023-10-27", "10:00:00", "12:00:00", "Initiation", "Waterfall", "Requirement Gathering", new CurrentUserEditing(""));
        Task log4 = new Task("log_004", "Sample Log 4", "Sample description 4", "2023-10-28", "13:00:00", "15:00:00", "Execution", "Plan 2", "Evaluation", new CurrentUserEditing(""));

        userLogsProject2.add(log3);
        userLogsProject2.add(log4);

        Project sampleProject2 = new Project("project_002", "Sample Project 2", lifeCycleStepsProject2, plansProject2, effortCategoriesProject2, userLogsProject2);

        // Add "Sample Project 2" to projects list
        projects.add(sampleProject2);

        // Serialize the newly created Data object to file
        saveData();
    }
    
    public Project getProjectByName(String name) {
        for (Project project : this.projects) {
            if (project.getName().equals(name)) {
                return project;
            }
        }
        return null; // or throw an exception if a project must be found
    }

    public ArrayList<String> getProjectNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Project project : this.projects) {
            names.add(project.getName());
        }
        return names;
    }


    // Save current Data object to file
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_PATH))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters and setters for projects and other properties
    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}



