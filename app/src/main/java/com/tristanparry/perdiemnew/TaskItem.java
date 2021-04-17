package com.tristanparry.perdiemnew;

public class TaskItem {
    private String taskTitle;
    private String taskDescription;

    public TaskItem(String title, String description) {
        taskTitle = title;
        taskDescription = description;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }
}