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
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonConcurrencyTracker {
    
	private static final String JSON_PATH = "Assets" + File.separator + "demo.json";
	private JSONObject jsonData;
    private ConcurrentEditingPrototype stage;
    private Timer timer;
    private LocalDateTime lastInteractionTime;

    public JsonConcurrencyTracker(ConcurrentEditingPrototype stage) {
        this.stage = stage;
        this.timer = new Timer();
        readFromFile();
    }
    
    /**
     * Records the new interaction by updating the lastInteractionTime and starts a countdown timer.
     */
    public void recordNewInteraction(String projectName, String taskName) {
        readFromFile();
        
        // Update the lastInteractionTime to the current time in UTC
        lastInteractionTime = LocalDateTime.now(ZoneOffset.UTC);
        
        try {
            // Iterate over the projects in jsonData
            JSONArray projects = jsonData.getJSONArray("projects");
            for (int i = 0; i < projects.length(); i++) {
                JSONObject project = projects.getJSONObject(i);

                // Check if this project matches the specified project name
                if (project.getString("name").equals(projectName)) {
                    
                    // Iterate over the tasks within this project
                    JSONArray userLogs = project.getJSONArray("UserLogs");
                    for (int j = 0; j < userLogs.length(); j++) {
                        JSONObject task = userLogs.getJSONObject(j);

                        // Check if this task matches the specified task name
                        if (task.getString("logName").equals(taskName)) {

                            // Update the lastInteractionTime in the task attributes
                            task.getJSONObject("currentUserEditing").put("lastInteractionTime", lastInteractionTime.toString());

                            // Save the modified JSON data back to the file
                            saveToFile();
                            return;  // Exit the method once the task attributes have been updated
                        }
                    }
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        startCountdown();
    }

    private void startCountdown() {
        stopCountdown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	System.out.println("Timeeeeeeeeeeeeer");
                stage.informAboutEditingTimeout();
                stopCountdown();
            }
        }, 30000);
    }

    /**
     * Stops the ongoing countdown timer.
     */
    public void stopCountdown() {
        timer.cancel();
        timer = new Timer();
    }

    /**
     * Retrieves the names of all the projects present in the JSON data.
     * @return A list of project names.
     * @throws JSONException If there's an error parsing the JSON.
     */
    public ArrayList<String> getProjectNames() throws JSONException {
        readFromFile();
        JSONArray projects = jsonData.getJSONArray("projects");
        ArrayList<String> projectList = new ArrayList<>();
        
        for (int i = 0; i < projects.length(); i++) {
            projectList.add(projects.getJSONObject(i).get("name").toString());
        }
        
        return projectList;
    }

    /**
     * Fetches the attributes of the specified project from the JSON data.
     * @param projectName The name of the project.
     * @return A JSONObject containing the project's attributes.
     * @throws JSONException If there's an error parsing the JSON.
     */
    public JSONObject getProjectAttributes(String projectName) throws JSONException {
        readFromFile();
        JSONArray projects = jsonData.getJSONArray("projects");
        for (int i = 0; i < projects.length(); i++) {
            JSONObject project = projects.getJSONObject(i);
            if (project.getString("name").equals(projectName)) {
                return project;
            }
        }
        return null;
    }

    /**
     * Retrieves the attributes of the specified task from the JSON data.
     * @param projectName The name of the project the task belongs to.
     * @param taskName The name of the task.
     * @return A JSONObject containing the task's attributes.
     * @throws JSONException If there's an error parsing the JSON.
     */
    public JSONObject getTaskAttributes(String projectName, String taskName) throws JSONException {
        readFromFile();
        JSONArray projects = jsonData.getJSONArray("projects");
        for (int i = 0; i < projects.length(); i++) {
            JSONObject project = projects.getJSONObject(i);
            if (project.getString("name").equals(projectName)) {
                JSONArray userLogs = project.getJSONArray("UserLogs");
                for (int j = 0; j < userLogs.length(); j++) {
                    JSONObject task = userLogs.getJSONObject(j);
                    if (task.getString("logName").equals(taskName)) {
                        return task;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Changes the ownership of tasks. Releases the ownership of the old task and claims the ownership of the new task.
     * @param oldTaskName The name of the old task.
     * @param newTaskName The name of the new task.
     * @param userID The ID of the user claiming ownership.
     * @return True if the ownership change was successful, false otherwise.
     */
    public boolean changeOwnership(String projectName, String oldTaskName, String newTaskName, String userID) {
    	recordNewInteraction(projectName, newTaskName);
    	readFromFile();
        JSONObject newTaskAttributes = getTaskAttributes(projectName, newTaskName);  // Assuming project name is not necessary

        if (newTaskAttributes == null) {
            return false;
        }

        // Check if the new task is already owned by someone else
        if (!newTaskAttributes.getJSONObject("currentUserEditing").getString("userId").equals("") & !newTaskAttributes.getJSONObject("currentUserEditing").getString("userId").equals(userID)) {
            return false;
        }

        // Release ownership of the old task
        revokeOwnership(projectName, oldTaskName);

        // Claim ownership of the new task
        newTaskAttributes.getJSONObject("currentUserEditing").put("userId", userID);
        newTaskAttributes.getJSONObject("currentUserEditing").put("lastInteractionTime", LocalDateTime.now().toString());
        updateTaskAttributes(projectName, newTaskAttributes);

        return true;
    }
    
    
    /**
     * Releases the ownership of a task.
     * @param projectName The name of the project the task belongs to.
     * @param taskName The name of the task to release ownership of.
     * @return True if the ownership release was successful, false otherwise.
     */
    public boolean revokeOwnership(String projectName, String taskName) {
        readFromFile();
        JSONObject taskAttributes = getTaskAttributes(projectName, taskName);

        if (taskAttributes == null) {
            return false;
        }

        // Release ownership of the task
        taskAttributes.getJSONObject("currentUserEditing").put("userId", "");
        taskAttributes.getJSONObject("currentUserEditing").put("lastInteractionTime", "");
        updateTaskAttributes(projectName, taskAttributes);

        return true;
    }
    
    
    /**
     * Replaces the attributes of a specified task within a specified project.
     * @param projectName The name of the project the task belongs to.
     * @param taskName The name of the task.
     * @param newAttributes The new JSONObject containing the attributes to replace.
     * @throws JSONException If there's an error parsing the JSON.
     */
    public void updateTaskAttributes(String projectName, JSONObject newAttributes) throws JSONException {
    	String taskName = newAttributes.getString("logName");
    	// Iterate over the projects
        JSONArray projects = jsonData.getJSONArray("projects");
        for (int i = 0; i < projects.length(); i++) {
            JSONObject project = projects.getJSONObject(i);

            // Check if this project matches the specified project name
            if (project.getString("name").equals(projectName)) {

                // Iterate over the tasks within this project
                JSONArray userLogs = project.getJSONArray("UserLogs");
                for (int j = 0; j < userLogs.length(); j++) {
                    JSONObject task = userLogs.getJSONObject(j);

                    // Check if this task matches the specified task name
                    if (task.getString("logName").equals(taskName)) {

                        // Replace the task attributes
                        Iterator<String> keys = newAttributes.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            task.put(key, newAttributes.get(key));
                        }
                        // Save the modified JSON data back to the file
                        saveToFile();
                        return;  // Exit the method once the task attributes have been replaced
                    }
                }
            }
        }
    }
    
    /**
     * Updates the attributes of a specified task within a specified project.
     * @param projectName The name of the project the task belongs to.
     * @param taskName The name of the task.
     * @param lifeCycleStep The new value for the lifeCycle attribute of the task.
     * @param effortCategory The new value for the effortCategory attribute of the task.
     * @param plan The new value for the plan attribute of the task.
     * @throws JSONException If there's an error parsing the JSON.
     */
    public void updateLogData(String projectName, String taskName, String lifeCycleStep, String effortCategory, String plan) throws JSONException {
        // Read the current JSON data from the file
        readFromFile();

        // Iterate over the projects
        JSONArray projects = jsonData.getJSONArray("projects");
        for (int i = 0; i < projects.length(); i++) {
            JSONObject project = projects.getJSONObject(i);

            // Check if this project matches the specified project name
            if (project.getString("name").equals(projectName)) {

                // Iterate over the tasks within this project
                JSONArray userLogs = project.getJSONArray("UserLogs");
                for (int j = 0; j < userLogs.length(); j++) {
                    JSONObject task = userLogs.getJSONObject(j);

                    // Check if this task matches the specified task name
                    if (task.getString("logName").equals(taskName)) {

                        // Update the task attributes with the provided values
                        task.put("lifeCycle", lifeCycleStep);
                        task.put("effortCategory", effortCategory);
                        task.put("plan", plan);

                        // Save the modified JSON data back to the file
                        saveToFile();
                        return;  // Exit the method once the task attributes have been updated
                    }
                }
            }
        }
    }



    /**
     * Saves the current state of jsonData to the file.
     */
    private void saveToFile() {
        try (FileWriter file = new FileWriter(JSON_PATH)) {
            file.write(jsonData.toString(4));
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Reads the JSON data from the specified file and updates the jsonData object.
     */
    private void readFromFile() {
        try {
            FileReader reader = new FileReader(JSON_PATH);
            JSONTokener tokener = new JSONTokener(reader);
            jsonData = new JSONObject(tokener);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
