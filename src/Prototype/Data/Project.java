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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable {

    private static final long serialVersionUID = 2L; // serialVersionUID for serialization

    private String id;
    private String name;
    private ArrayList<String> lifeCycleSteps;
    private ArrayList<String> plans;
    private ArrayList<String> effortCategories;
    private ArrayList<Task> tasks;

    // Constructor
    public Project(String id, String name, ArrayList<String> lifeCycleSteps, ArrayList<String> plans, ArrayList<String> effortCategories, ArrayList<Task> tasks) {
    	this.tasks = tasks;
        this.id = id;
        this.name = name;
        this.lifeCycleSteps = lifeCycleSteps;
        this.plans = plans;
        this.effortCategories = effortCategories;
    }

    public Task getTaskByName(String taskName) {
        for (Task task : this.tasks) {
            if (task.getName().equals(taskName)) {
                return task;
            }
        }
        return null; // or throw an exception if a task must be found
    }
    
    // Getters and setters for all properties
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getLifeCycleSteps() {
        return lifeCycleSteps;
    }

    public void setLifeCycleSteps(ArrayList<String> lifeCycleSteps) {
        this.lifeCycleSteps = lifeCycleSteps;
    }

    public ArrayList<String> getPlans() {
        return plans;
    }

    public void setPlans(ArrayList<String> plans) {
        this.plans = plans;
    }

    public ArrayList<String> getEffortCategories() {
        return effortCategories;
    }

    public void setEffortCategories(ArrayList<String> effortCategories) {
        this.effortCategories = effortCategories;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    // Method to add a UserLog to the project
    public void addTask(Task task) {
        if (this.tasks == null) {
            this.tasks = new ArrayList<>();
        }
        this.tasks.add(task);
    }

    // Method to remove a UserLog from the project
    public void removeUserLog(Task task) {
        if (this.tasks != null) {
            this.tasks.remove(task);
        }
    }

    // Implement toString method for debugging purposes
    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lifeCycleSteps=" + lifeCycleSteps +
                ", plans=" + plans +
                ", effortCategories=" + effortCategories +
                ", tasks=" + tasks +
                '}';
    }
}
