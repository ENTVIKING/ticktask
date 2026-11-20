package com.ent.ticktask.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ent.ticktask.Model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM tasks ORDER BY is_completed ASC, due_date ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    LiveData<Task> getTaskById(int taskId);

    @Query("SELECT * FROM tasks WHERE is_completed = 0 AND due_date > :now")
    List<Task> getUpcomingTasks(long now);

    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 0")
    LiveData<Integer> getActiveCount();

    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 1")
    LiveData<Integer> getCompletedCount();

    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 0 AND due_date > 0 AND due_date < :now")
    LiveData<Integer> getOverdueCount(long now);
}