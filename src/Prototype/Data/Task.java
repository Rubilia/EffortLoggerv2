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
    public String name;
    public String description;
    public List<String> userStories;

    // Constructor
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.userStories = new ArrayList<>();
    }
}