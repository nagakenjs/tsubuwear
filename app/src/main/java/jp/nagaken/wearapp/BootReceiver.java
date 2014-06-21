package jp.nagaken.wearapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tkg on 2014/06/21.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // 端末が再起動するとタイマー設定が消えてしまうので、bootのintentを受け取ってタイマーを再設定する
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
            String[] time = pref.getString("alermtime", "12:00").split(":");
            AlermUtil.setAlermTime(ctx, Integer.valueOf(time[0]), Integer.valueOf(time[1]));
        }
    }
}
