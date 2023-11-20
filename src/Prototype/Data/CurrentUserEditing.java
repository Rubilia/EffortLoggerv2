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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentUserEditing implements Serializable {
    private static final long serialVersionUID = 4L;
    
    private String userId;
    private LocalDateTime lastInteractionTime;

    // Constructor
    public CurrentUserEditing(String userId) {
        this.userId = userId;
        lastInteractionTime = LocalDateTime.now();
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getLastInteractionTime() {
        return lastInteractionTime;
    }

    public void setLastInteractionTime(LocalDateTime lastInteractionTime) {
        this.lastInteractionTime = lastInteractionTime;
    }

    public void setLastInteractionTime(String lastInteractionTime) {
        if (lastInteractionTime != null && !lastInteractionTime.isEmpty()) {
            this.lastInteractionTime = LocalDateTime.parse(lastInteractionTime, DateTimeFormatter.ISO_DATE_TIME);
        } else {
            this.lastInteractionTime = null;
        }
    }

    // Implement toString method for debugging purposes
    @Override
    public String toString() {
        return "CurrentUserEditing{" +
                "userId='" + userId + '\'' +
                ", lastInteractionTime=" + lastInteractionTime +
                '}';
    }
}