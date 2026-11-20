package com.ent.ticktask.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.ent.ticktask.Model.Task;
import com.ent.ticktask.Repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;
    private final LiveData<List<Task>> allTasks;
    private final LiveData<Integer> activeCount;
    private final LiveData<Integer> completedCount;
    private final LiveData<Integer> overdueCount;
    private final MutableLiveData<Long> now = new MutableLiveData<>(System.currentTimeMillis());

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
        activeCount = repository.getActiveCount();
        completedCount = repository.getCompletedCount();
        overdueCount = Transformations.switchMap(now, time -> repository.getOverdueCount(time));
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> getTaskById(int id) {
        return repository.getTaskById(id);
    }

    public LiveData<Integer> getActiveCount() {
        return activeCount;
    }

    public LiveData<Integer> getCompletedCount() {
        return completedCount;
    }

    public LiveData<Integer> getOverdueCount() {
        return overdueCount;
    }

    public void refreshNow() {
        now.setValue(System.currentTimeMillis());
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }
}