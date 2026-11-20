package com.ent.ticktask.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ent.ticktask.Data.AppDatabase;
import com.ent.ticktask.Model.Task;

import java.util.List;
import java.util.concurrent.Executors;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return;
        }

        PendingResult result = goAsync();
        Context appContext = context.getApplicationContext();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Task> tasks = AppDatabase.getInstance(appContext)
                        .taskDao()
                        .getUpcomingTasks(System.currentTimeMillis());
                for (Task task : tasks) {
                    ReminderScheduler.schedule(appContext, task);
                }
            } finally {
                result.finish();
            }
        });
    }
}