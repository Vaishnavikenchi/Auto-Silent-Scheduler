package com.example.autosilentscheduler;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class SilentReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        android.util.Log.d("SILENT_DEBUG", "Receiver Triggered!"); 

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        String action = intent.getStringExtra("ACTION");

        if ("SILENT".equals(action)) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            android.util.Log.d("SILENT_DEBUG", "Mode changed to SILENT");
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            android.util.Log.d("SILENT_DEBUG", "Mode changed to NORMAL");
        }
    }
}
