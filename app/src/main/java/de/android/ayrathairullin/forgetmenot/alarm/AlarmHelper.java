package de.android.ayrathairullin.forgetmenot.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import de.android.ayrathairullin.forgetmenot.model.ModelTask;

public class AlarmHelper {
    private static AlarmHelper instance;
    private Context context;
    private AlarmManager alarmManager;

    private AlarmHelper() {
    }

    public static AlarmHelper getInstance() {
        if (instance == null) {
            instance = new AlarmHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(ModelTask task) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("time_stamp", task.getTimeStamp());
        intent.putExtra("color", task.getPriorityColor());

        PendingIntent pi = PendingIntent.getBroadcast(context.getApplicationContext(),
                (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(), pi);
    }

    public void removeAlarm(long taskTimeStamp) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                context, (int) taskTimeStamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }
}
