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
import java.util.List;


public class Data implements Serializable {
    public ArrayList<Project> projects;
    private static final String DATA_PATH = "Assets" + File.separator + "data.dat";

    // Constructor
    public Data() {
        this.projects = new ArrayList<>();
    }

    // Method to add a project to the list.
    public void addProject(Project project) {
        projects.add(project);
    }

    // Method to save the Data object to a file.
    public void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_PATH))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load the Data object from a file.
    public static Data load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_PATH))) {
            return (Data) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}



