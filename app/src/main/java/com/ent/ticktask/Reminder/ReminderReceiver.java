package com.ent.ticktask.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(ReminderScheduler.EXTRA_ID, 0);
        String title = intent.getStringExtra(ReminderScheduler.EXTRA_TITLE);
        NotificationHelper.show(context, id, title == null ? "" : title);
    }
}