package Prototype;

import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * JsonConcurrencyTracker Class
 * 
 * The JsonConcurrencyTracker class provides a way to manage project data stored in a JSON file.
 * 
 * The JSON structure:
 * {
 *   "projects": [
 *     {
 *       "id": "<project_id>",                   // Unique identifier for the project.
 *       "name": "<project_name>",               // Name of the project.
 *       "UserLogs": [
 *       {
 *       	"logId": "<id>",                        // id of a log
 *          "logName": "<name>",                      // name of a log
 *       	"date": "<date>",                       // Date of the project.
 *       	"startTime": "<start_time>",            // Start time of the project.
 *       	"stopTime": "<stop_time>",              // Stop time or end time of the project.
 *       	"lifeCycle": "<life_cycle_phase>",      // Phase of the project life cycle.
 *       	"effortCategory": "<effort_category>",  // Category of the effort.
 *       	"plan": "<type_of_plan>",               // Type of plan associated with the project.
 *          "currentUserEditing": {
 *             "userId": "<user_id_editing_project>",       // User ID of the person currently editing the project. (null by default)
 *             "lastInteractionTime": "<last_interaction_time_in_utc>"  // Last interaction time in UTC format. (null by default)
 *          }
 *       }
 *       ],       // Type of upload performed by the user.
 *     },
 *     ...
 *   ]
 * }
 */

public class JsonConcurrencyTracker {
    
	private static final String JSON_PATH = "Assets" + File.separator + "demo.json";
	private JSONObject jsonData;
    private ConcurrentEditingPrototype stage;
    private Timer timer;
    private LocalDateTime lastInteractionTime;

    public JsonConcurrencyTracker(ConcurrentEditingPrototype stage) {
        this.stage = stage;
        this.timer = new Timer();
        try {
            FileReader reader = new FileReader(JSON_PATH);
            JSONTokener tokener = new JSONTokener(reader);
            jsonData = new JSONObject(tokener);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Records a new user interaction by setting the lastInteractionTime to the current time.
     */
    public void recordNewInteraction() {
        lastInteractionTime = LocalDateTime.now();
        startCountdown();
    }

    private void startCountdown() {
        stopCountdown();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                stage.informAboutEditingTimeout();
                stopCountdown();
            }
        }, 30000);
    }

    private void stopCountdown() {
        timer.cancel();
        timer = new Timer();
    }

    /**
     * Fetches a list of all projects.
     * 
     * @return ArrayList<String> containing project names.
     * @throws JSONException 
     */
    public ArrayList<String> getProjectNames() throws JSONException {
        JSONArray projects = jsonData.getJSONArray("projects");
        ArrayList<String> projectList = new ArrayList<>();
        
        for (int i = 0; i < projects.length(); i++) {
            projectList.add(projects.getJSONObject(i).get("name").toString());
        }
        
        return projectList;
    }

    /**
     * Retrieves all attributes for a given project.
     * 
     * @param projectName The name of the project.
     * @return JSONObject containing all attributes of the specified project.
     * @throws JSONException 
     */
    public JSONObject getProjectAttributes(String projectName) throws JSONException {
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
     * Retrieves a specific attribute for a given task (log entry).
     * 
     * @param projectName Project name.
     * @param taskName Task name.
     * @return JSONObject containing attributes of the specified task.
     * @throws JSONException 
     */
    public JSONObject getTaskAttributes(String projectName, String taskName) throws JSONException {
        JSONArray projects = jsonData.getJSONArray("projects");
        for (int i = 0; i < projects.length(); i++) {
            JSONObject project = projects.getJSONObject(i);
            if (project.getString("name").equals(projectName)) {
                JSONArray userLogs = project.getJSONArray("UserLogs");
                for (int j = 0; j < userLogs.length(); j++) {
                    JSONObject task = userLogs.getJSONObject(j);
                    if (task.getString("logName").equals(taskName)) {
                        return task; // Return the entire JSON object for the specified task.
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if a task is available for editing by a given user.
     * 
     * @param projId Project ID.
     * @param taskId Task ID.
     * @param userId User ID.
     * @return true if the task is available, false otherwise.
     */
    public boolean isTaskAvailable(String projId, String taskId, String userId) {
    	JSONObject taskAttributes = getTaskAttributes(projId, taskId);
        if (taskAttributes != null) {
            String currentUserId = taskAttributes.get("currentUserEditing").toString();
            return currentUserId.equals(userId) || currentUserId.isEmpty();
        }
        return false;
    }


}
