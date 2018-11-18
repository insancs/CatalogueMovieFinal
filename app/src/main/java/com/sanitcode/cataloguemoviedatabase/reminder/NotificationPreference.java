package com.sanitcode.cataloguemoviedatabase.reminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class NotificationPreference {
    private final static String PREF_NAME = "reminderPreferences";
    private final static String KEY_REMINDER_DAILY = "DailyReminder";
    private final static String KEY_REMINDER_MESSAGE_Release = "reminderMessageRelease";
    private final static String KEY_REMINDER_MESSAGE_Daily = "reminderMessageDaily";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public NotificationPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void setReminderReleaseTime(String time){
        editor.putString(KEY_REMINDER_DAILY,time);
        editor.commit();
    }
    public void setReminderReleaseMessage (String message){
        editor.putString(KEY_REMINDER_MESSAGE_Release,message);
    }
    public void setReminderDailyTime(String time){
        editor.putString(KEY_REMINDER_DAILY,time);
        editor.commit();
    }
    public void setReminderDailyMessage(String message){
        editor.putString(KEY_REMINDER_MESSAGE_Daily,message);
    }
}
