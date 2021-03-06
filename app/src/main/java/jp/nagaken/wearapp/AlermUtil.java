package jp.nagaken.wearapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by tkg on 2014/06/21.
 */
public class AlermUtil {
    private static final int ALERM = 0;

    public static void setAlermTime(Context ctx) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String[] time = pref.getString("alermtime", "12:00").split(":");
        int hour = Integer.valueOf(time[0]);
        int minute = Integer.valueOf(time[1]);
        setAlerm(ctx, hour, minute);
    }

    public static void saveAlermTime(Context ctx, int hour, int minute) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String[] time = pref.getString("alermtime", "12:00").split(":");
        pref.edit().putString("alermtime", String.format("%02d:%02d", hour, minute)).commit();
        setAlerm(ctx, hour, minute);
    }

    @SuppressLint("NewApi")
    private static void setAlerm(Context ctx, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() - System.currentTimeMillis() < 0) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent i = new Intent(ctx, AlermReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(ctx, ALERM, i, 0);
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
        }
    }
}