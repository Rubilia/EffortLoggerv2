/**
 * Author: Ilia Rubashkin
 * Copyright © 2023 Ilia Rubashkin. All rights reserved.
 * 
 * Description: This file and its contents are the property of Ilia Rubashkin.
 * Unauthorized use, modification, or distribution of this file or its contents
 * without explicit permission from the author is strictly prohibited.
 */

package Prototype.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {
    private static final long serialVersionUID = 3L;

    // Fields from Task
    private String name;
    private String description;
    private List<String> userStories;
    private CurrentUserEditing currentUserEditing;

    // Common fields from both classes
    private String id;
    private String date;
    private String startTime;
    private String stopTime;
    private String lifeCycleStep; // Merged lifeCycle and lifeCycleStep
    private String effortCategory;
    private String plan;

    // Constructor
    public Task(String id, String name, String description, String date,
                String startTime, String stopTime, String lifeCycleStep, String plan,
                String effortCategory, CurrentUserEditing currentUserEditing) {
    	this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.lifeCycleStep = lifeCycleStep;
        this.plan = plan;
        this.effortCategory = effortCategory;
        this.currentUserEditing = currentUserEditing;
        this.userStories = new ArrayList<>();
    }
    
    public boolean isLocked(String userID) {
    	return !currentUserEditing.getUserId().equals("") && !currentUserEditing.getUserId().equals(userID);
    }
    
    public void lock(String userId) {
    	currentUserEditing = new CurrentUserEditing(userId);
    }
    
    public void unlock(String userId) {
    	currentUserEditing = new CurrentUserEditing("");
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getUserStories() {
        return userStories;
    }

    public void setUserStories(List<String> userStories) {
        this.userStories = userStories;
    }

    public void addUserStory(String userStory) {
        this.userStories.add(userStory);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getLifeCycleStep() {
        return lifeCycleStep;
    }

    public void setLifeCycleStep(String lifeCycleStep) {
        this.lifeCycleStep = lifeCycleStep;
    }

    public String getEffortCategory() {
        return effortCategory;
    }

    public void setEffortCategory(String effortCategory) {
        this.effortCategory = effortCategory;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public CurrentUserEditing getCurrentUserEditing() {
        return currentUserEditing;
    }

    public void setCurrentUserEditing(CurrentUserEditing currentUserEditing) {
        this.currentUserEditing = currentUserEditing;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userStories=" + userStories +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", stopTime='" + stopTime + '\'' +
                ", lifeCycleStep='" + lifeCycleStep + '\'' +
                ", effortCategory='" + effortCategory + '\'' +
                ", plan='" + plan + '\'' +
                ", currentUserEditing=" + currentUserEditing +
                '}';
    }

}
