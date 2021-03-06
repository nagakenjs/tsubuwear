
package jp.nagaken.wearapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.RemoteInput;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.button)
    Button button;

    @InjectView(R.id.timerButton)
    Button timerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        // デバッグ時のみボタンを表示する
        button.setVisibility(BuildConfig.DEBUG ? View.VISIBLE : View.GONE);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extra = intent.getExtras();
            if (extra != null) {
                String choice = extra.getString(NotificationUtil.EXTRA_MENU);
                if (TextUtils.isEmpty(choice)) {
                    Toast.makeText(this, "reply none", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent mapIntent = new Intent();
                mapIntent.setAction(Intent.ACTION_VIEW);
                mapIntent.setData(Uri.parse("geo:0,0?q=" + choice));
                startActivity(mapIntent);
            }
        }

        AlermUtil.setAlermTime(this);
    }

    @OnClick(R.id.button)
    public void clickButton(Button button) {
        Resources res = getResources();
        RemoteInput remoteInput = new RemoteInput.Builder(NotificationUtil.EXTRA_MENU)
                .setLabel(res.getString(R.string.menu_label))
                .setChoices(res.getStringArray(R.array.menu_choices))
                .build();

        int notificationId = 1;

        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_event)
                .setContentTitle(res.getString(R.string.event_title))
                .setContentText(res.getString(R.string.event_text))
                .setContentIntent(viewPendingIntent);

        Notification replyNotification = new WearableNotifications.Builder(notificationBuilder)
                .addRemoteInputForContentIntent(remoteInput)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, replyNotification);
    }

    @OnClick(R.id.timerButton)
    public void clickTimerButton(Button button) {
        DialogFragment df = new EditAlermDialogFragment();
//        df.setTargetFragment(this, 0);
        df.show(getFragmentManager(), "picker");
    }

}
