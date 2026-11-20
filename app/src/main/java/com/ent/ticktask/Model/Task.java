package com.ent.ticktask.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title = "";

    @ColumnInfo(name = "description")
    private String description;

    // 0 = normal, 1 = medium, 2 = important, 3 = urgent
    @ColumnInfo(name = "priority")
    private int priority;

    // Epoch millis, 0 means no reminder
    @ColumnInfo(name = "due_date")
    private long dueDate;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}