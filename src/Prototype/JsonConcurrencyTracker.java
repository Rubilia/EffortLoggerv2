package Prototype;

import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;
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
 *       "userUpload": "<type_of_upload>",       // Type of upload performed by the user.
 *       "date": "<date>",                       // Date of the project.
 *       "startTime": "<start_time>",            // Start time of the project.
 *       "stopTime": "<stop_time>",              // Stop time or end time of the project.
 *       "lifeCycle": "<life_cycle_phase>",      // Phase of the project life cycle.
 *       "effortCategory": "<effort_category>",  // Category of the effort.
 *       "plan": "<type_of_plan>",               // Type of plan associated with the project.
 *       "currentUserEditing": {
 *         "userId": "<user_id_editing_project>",       // User ID of the person currently editing the project.
 *         "lastInteractionTime": "<last_interaction_time_in_utc>"  // Last interaction time in UTC format.
 *       }
 *     },
 *     ...
 *   ]
 * }
 */
public class JsonConcurrencyTracker {
    
    private static final String JSON_PATH = "path_to_your_json_file.json";
    private JSONObject jsonData;
    private String userId;
    private Stage stage;
    private Timer timer;
    private LocalDateTime lastInteractionTime;

    public JsonConcurrencyTracker(String userId, Stage stage) {
        this.userId = userId;
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
                informAboutEditingTimeout();
                stopCountdown();
            }
        }, 30000);
    }

    private void stopCountdown() {
        timer.cancel();
        timer = new Timer();
    }

    private void informAboutEditingTimeout() {
        stage.informAboutEditingTimeout();
    }

    /**
     * Fetches a list of all projects.
     * 
     * @return JSONArray containing project data.
     * @throws JSONException 
     */
    public JSONArray getProjects() throws JSONException {
        return jsonData.getJSONArray("projects");
    }

    /**
     * Retrieves a specific attribute for a given project.
     * 
     * @param projId The ID of the project.
     * @param propertyName Name of the property.
     * @return Value of the specified property.
     * @throws JSONException 
     */
    public String getProjectAttributes(String projId, String propertyName) throws JSONException {
        JSONArray projects = jsonData.getJSONArray("projects");
        for (int i = 0; i < projects.length(); i++) {
            JSONObject project = projects.getJSONObject(i);
            if (project.getString("id").equals(projId)) {
                return project.getString(propertyName);
            }
        }
        return null;
    }

    /**
     * Determines if a project is available for editing by a given user.
     * 
     * @param projId Project ID.
     * @param userId User ID.
     * @return true if the project is available, false otherwise.
     */
    public boolean isProjectAvailable(String projId, String userId) {
        JSONObject project = getProjectAttributes(projId, "currentUserEditing");
        if (project != null) {
            JSONObject currentUserEditing = project.getJSONObject("currentUserEditing");
            String currentUserId = currentUserEditing.getString("userId");
            return currentUserId.equals(userId) || currentUserId.isEmpty();
        }
        return false;
    }

    /**
     * Modifies a specific attribute of a given project.
     * 
     * @param projId The ID of the project.
     * @param propertyName The property name to be modified.
     * @param propertyValue The new value for the property.
     * @return true if the operation was successful, false otherwise.
     * @throws JSONException 
     */
    public boolean editProjectAttributes(String projId, String propertyName, String propertyValue) throws JSONException {
        JSONArray projects = jsonData.getJSONArray("projects");
        for (int i = 0; i < projects.length(); i++) {
            JSONObject project = projects.getJSONObject(i);
            if (project.getString("id").equals(projId)) {
                project.put(propertyName, propertyValue);
                recordNewInteraction();
                try {
                    FileWriter writer = new FileWriter(JSON_PATH);
                    writer.write(jsonData.toString());
                    writer.close();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
