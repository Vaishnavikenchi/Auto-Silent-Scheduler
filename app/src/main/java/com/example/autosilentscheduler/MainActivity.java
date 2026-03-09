package com.example.autosilentscheduler;

import static java.util.Calendar.*;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TimePicker startPicker, endPicker;
    private Button btnSet, btnPickDate;
    private ImageButton btnShowList;
    private TextView txtSelectedDate;

    private ScheduleAdapter adapter;
    private List<ScheduleModel> scheduleList = new ArrayList<>();
    private long selectedDateMillis = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Initialize Views
        startPicker = findViewById(R.id.startTimePicker);
        endPicker = findViewById(R.id.endTimePicker);
        btnSet = findViewById(R.id.btnSetSchedule);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnShowList = findViewById(R.id.btnShowList);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);

        // 2. Setup Adapter
        adapter = new ScheduleAdapter(scheduleList);

        // 3. Permission Checks
        checkPermissions();

        // 4. Set Default Date Text
        SimpleDateFormat sdfDefault = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        txtSelectedDate.setText(sdfDefault.format(selectedDateMillis));

        // 5. Button Logic: Pick Date
        btnPickDate.setOnClickListener(v -> {
            // Define constraints ONLY once inside the click listener
            CalendarConstraints constraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build();

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Schedule Date")
                    .setTheme(R.style.CustomDatePickerTheme)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(constraints)
                    .build();

            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDateMillis = selection;
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                txtSelectedDate.setText(sdf.format(selection));
            });
        });

        // 6. Button Logic: Show List (BottomSheet)
        btnShowList.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            View sheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
            dialog.setContentView(sheetView);

            RecyclerView rvSheet = sheetView.findViewById(R.id.rvSheet);
            rvSheet.setLayoutManager(new LinearLayoutManager(this));
            rvSheet.setAdapter(adapter);

            dialog.show();
        });

        // 7. Button Logic: Activate Schedule
        btnSet.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);

            Calendar startCal = Calendar.getInstance();
            startCal.setTimeInMillis(selectedDateMillis);
            startCal.set(Calendar.HOUR_OF_DAY, startPicker.getHour());
            startCal.set(Calendar.MINUTE, startPicker.getMinute());
            startCal.set(Calendar.SECOND, 0);
            startCal.set(Calendar.MILLISECOND, 0);

            if (startCal.before(now)) {
                Toast.makeText(this, "Cannot schedule for a past time!", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar endCal = Calendar.getInstance();
            endCal.setTimeInMillis(selectedDateMillis);
            endCal.set(HOUR_OF_DAY, endPicker.getHour());
            endCal.set(MINUTE, endPicker.getMinute());
            endCal.set(SECOND, 0);
            endCal.set(MILLISECOND, 0);

            if (endCal.before(startCal)) {
                Toast.makeText(this, "End time must be after start time!", Toast.LENGTH_SHORT).show();
                return;
            }

            int uniqueId = (int) (System.currentTimeMillis() / 1000);
            startSchedule(startCal.getTimeInMillis(), endCal.getTimeInMillis(), uniqueId);

            String startTimeStr = formatTo12Hour(startPicker.getHour(), startPicker.getMinute());
            String endTimeStr = formatTo12Hour(endPicker.getHour(), endPicker.getMinute());
            String fullInfo = txtSelectedDate.getText().toString() + " | " + startTimeStr + " - " + endTimeStr;

            scheduleList.add(new ScheduleModel(uniqueId, fullInfo));
            adapter.notifyDataSetChanged(); // Refreshes the list

            Toast.makeText(this, "Schedule Added!", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkPermissions() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (am != null && !am.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null && !nm.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    private String formatTo12Hour(int hour, int minute) {
        String amPm = (hour >= 12) ? "PM" : "AM";
        int displayHour = hour;
        if (hour == 0) displayHour = 12;
        else if (hour > 12) displayHour = hour - 12;
        return String.format(Locale.getDefault(), "%d:%02d %s", displayHour, minute, amPm);
    }

    public void startSchedule(long startTimeMillis, long endTimeMillis, int uniqueId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent startIntent = new Intent(this, SilentReceiver.class);
        startIntent.putExtra("ACTION", "SILENT");
        PendingIntent startPI = PendingIntent.getBroadcast(this, uniqueId, startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent endIntent = new Intent(this, SilentReceiver.class);
        endIntent.putExtra("ACTION", "NORMAL");
        PendingIntent endPI = PendingIntent.getBroadcast(this, uniqueId + 1, endIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTimeMillis, startPI);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTimeMillis, endPI);
        }
    }
}