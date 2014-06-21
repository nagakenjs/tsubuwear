package jp.nagaken.wearapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.RemoteInput;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;

/**
 * Created by tkg on 2014/06/21.
 */
public class AlermReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Intent viewIntent = new Intent(ctx, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(ctx, 0, viewIntent, 0);

        Resources res = ctx.getResources();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_event)
                .setContentTitle(res.getString(R.string.event_title))
                .setContentText(res.getString(R.string.event_text))
                .setContentIntent(viewPendingIntent);

        RemoteInput remoteInput = new RemoteInput.Builder(NotificationUtil.EXTRA_MENU)
                .setLabel(res.getString(R.string.menu_label))
                .setChoices(res.getStringArray(R.array.menu_choices))
                .build();

        int notificationId = 1;


        Notification replyNotification = new WearableNotifications.Builder(notificationBuilder)
                .addRemoteInputForContentIntent(remoteInput)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(notificationId, replyNotification);


        // 次の日のためのタイマーをセットする
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String[] time = pref.getString("alermtime", "12:00").split(":");
        AlermUtil.setAlermTime(ctx, Integer.valueOf(time[0]), Integer.valueOf(time[1]));
    }
}
