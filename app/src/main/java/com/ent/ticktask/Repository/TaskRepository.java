package com.ent.ticktask.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ent.ticktask.Data.AppDatabase;
import com.ent.ticktask.Data.TaskDao;
import com.ent.ticktask.Model.Task;
import com.ent.ticktask.Reminder.ReminderScheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {

    private final Application application;
    private final TaskDao taskDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TaskRepository(Application application) {
        this.application = application;
        taskDao = AppDatabase.getInstance(application).taskDao();
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    public LiveData<Task> getTaskById(int id) {
        return taskDao.getTaskById(id);
    }

    public LiveData<Integer> getActiveCount() {
        return taskDao.getActiveCount();
    }

    public LiveData<Integer> getCompletedCount() {
        return taskDao.getCompletedCount();
    }

    public LiveData<Integer> getOverdueCount(long now) {
        return taskDao.getOverdueCount(now);
    }

    public void insert(Task task) {
        executor.execute(() -> {
            long id = taskDao.insertTask(task);
            task.setId((int) id);
            ReminderScheduler.schedule(application, task);
        });
    }

    public void update(Task task) {
        executor.execute(() -> {
            taskDao.updateTask(task);
            ReminderScheduler.schedule(application, task);
        });
    }

    public void delete(Task task) {
        executor.execute(() -> {
            taskDao.deleteTask(task);
            ReminderScheduler.cancel(application, task.getId());
        });
    }
}