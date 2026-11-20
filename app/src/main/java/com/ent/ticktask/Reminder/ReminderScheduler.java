package com.ent.ticktask.Reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ent.ticktask.Model.Task;

public final class ReminderScheduler {

    public static final String EXTRA_ID = "task_id";
    public static final String EXTRA_TITLE = "task_title";

    private ReminderScheduler() {
    }

    // Creates, updates or cancels the alarm depending on the task state
    public static void schedule(Context context, Task task) {
        if (task.isCompleted() || task.getDueDate() <= System.currentTimeMillis()) {
            cancel(context, task.getId());
            return;
        }

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_ID, task.getId());
        intent.putExtra(EXTRA_TITLE, task.getTitle());

        // Task id as request code: one independent alarm per task
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Android 12+ requires the user to allow exact alarms
        boolean canScheduleExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S
                || alarmManager.canScheduleExactAlarms();

        if (canScheduleExact) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, task.getDueDate(), pendingIntent);
        } else {
            alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, task.getDueDate(), pendingIntent);
        }
    }

    public static void cancel(Context context, int taskId) {
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId,
                new Intent(context, ReminderReceiver.class),
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}