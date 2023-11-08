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

public class Project implements Serializable {
    public String name;
    public String type;
    public List<String> lifecycles;
    public List<String> effortCategories;
    public List<Task> tasks;

    // Constructor
    public Project(String name, String type) {
        this.name = name;
        this.type = type;
        this.lifecycles = new ArrayList<>();
        this.effortCategories = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }
}