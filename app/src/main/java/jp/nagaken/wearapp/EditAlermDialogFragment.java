package jp.nagaken.wearapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TimePicker;

/**
 * Created by tkg on 2014/06/21.
 */
public class EditAlermDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String[] time = pref.getString("alermtime", "12:00").split(":");
        final TimePicker picker = new TimePicker(getActivity());
        picker.setIs24HourView(true);
        picker.setCurrentHour(Integer.valueOf(time[0]));
        picker.setCurrentMinute(Integer.valueOf(time[1]));
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("通知時間の設定")
                .setView(picker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = picker.getCurrentHour();
                        int minute = picker.getCurrentMinute();
                        AlermUtil.saveAlermTime(getActivity(), hour, minute);
                    }
                })
                .create();
        return dialog;
    }
}