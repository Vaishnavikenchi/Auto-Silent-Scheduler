package com.example.autosilentscheduler;

import static java.util.Calendar.*;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TimePicker startPicker,endPicker;
    Button btnSet;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!am.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
        
        // CHECK PERMISSION: Do Not Disturb Access (Only needs to be here once)
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null && !nm.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }

        //UI INITIALIZATION (Find views once)
        startPicker = findViewById(R.id.startTimePicker);
        endPicker = findViewById(R.id.endTimePicker);
        btnSet = findViewById(R.id.btnSetSchedule);

        // BUTTON CLICK LOGIC
        btnSet.setOnClickListener(v -> {
            ((AudioManager)getSystemService(Context.AUDIO_SERVICE)).setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Calendar startCal = getInstance();
            startCal.set(HOUR_OF_DAY, startPicker.getHour());
            startCal.set(MINUTE, startPicker.getMinute());
            startCal.set(SECOND, 0);
            startCal.set(MILLISECOND, 0); 

            Calendar endCal = getInstance();
            endCal.set(HOUR_OF_DAY, endPicker.getHour());
            endCal.set(MINUTE, endPicker.getMinute());
            endCal.set(SECOND, 0);
            endCal.set(MILLISECOND, 0);

            // LOG THE TIME TO LOGCAT
            android.util.Log.d("SILENT_DEBUG", "Setting alarm for: " + startCal.getTime().toString());

            startSchedule(startCal.getTimeInMillis(), endCal.getTimeInMillis());
            Toast.makeText(this, "Schedule Set!", Toast.LENGTH_SHORT).show();
        });
    }

    // SCHEDULING LOGIC
    public void startSchedule(long startTimeMillis, long endTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Intent for Start (Mute)
        Intent startIntent = new Intent(this, SilentReceiver.class);
        startIntent.putExtra("ACTION", "SILENT");
        PendingIntent startPI = PendingIntent.getBroadcast(this, 101, startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Intent for End (Unmute)
        Intent endIntent = new Intent(this, SilentReceiver.class);
        endIntent.putExtra("ACTION", "NORMAL");
        PendingIntent endPI = PendingIntent.getBroadcast(this, 102, endIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
        
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTimeMillis, startPI);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTimeMillis, endPI);
        }
    }
}
