package com.ent.ticktask.Reminder;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.ent.ticktask.R;

public final class NotificationHelper {

    public static final String CHANNEL_ID = "task_reminders";

    private NotificationHelper() {
    }

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "یادآوری کارها",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("اعلان زمان انجام کارها");
            context.getSystemService(NotificationManager.class)
                    .createNotificationChannel(channel);
        }
    }

    public static void show(Context context, int taskId, String title) {
        createChannel(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Intent openApp = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());

        PendingIntent contentIntent = null;
        if (openApp != null) {
            contentIntent = PendingIntent.getActivity(
                    context,
                    taskId,
                    openApp,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("یادآوری کار")
                        .setContentText(title)
                        .setColor(ContextCompat.getColor(context, R.color.purple_primary))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_REMINDER)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);

        NotificationManagerCompat.from(context).notify(taskId, builder.build());
    }
}