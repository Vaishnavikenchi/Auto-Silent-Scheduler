# Auto-Silent Scheduler 🌙

**Auto-Silent Scheduler** is a professional Android utility designed to automate device ringer transitions,it ensures your phone remains silent during critical hours—such as meetings, classes, or sleep—and automatically restores the ringer mode when the scheduled period ends.

---

## ✨ Features
* **Intelligent Automation**: Leverages `AlarmManager` with `setExactAndAllowWhileIdle` to guarantee execution even when the device is in Doze mode.
* **Smart Scheduling**: Built-in logic to handle "next-day" scheduling if the selected start time has already passed for the current day.
* **System Integration**: Seamless permission handling for Notification Policy Access (DND) and Exact Alarm scheduling.

---

## 🛠️ Technical Stack
* **Language**: Java
* **UI Components**: `MaterialCardView`, `TimePicker` (Spinner Mode), `MaterialButton`.
* **Core APIs**: 
    * `AlarmManager`: For precise background task scheduling.
    * `BroadcastReceiver`: To capture system alarms and trigger ringer changes.
    * `AudioManager`: To programmatically toggle between `RINGER_MODE_SILENT` and `RINGER_MODE_NORMAL`.

---

## 🔐 Permissions Required
To function correctly on modern Android versions (API 31+), the app requires:
1. **Do Not Disturb Access**: `android.permission.ACCESS_NOTIFICATION_POLICY`
2. **Exact Alarms**: `android.permission.SCHEDULE_EXACT_ALARM`

---

## 🚀 Future Enhancements
To further improve the utility and user experience, the following features are planned for future updates:

1.**Location-Based Silencing (Geofencing)**: Automatically trigger silent mode when the user enters specific coordinates, such as a college campus or office building.

2.**Calendar Integration**: Sync with Google Calendar to automatically silence the device during scheduled meetings or lecture blocks.

3.**Weekly Recurring Schedules**: Allow users to set different silent windows for weekdays vs. weekends (e.g., a "Study Mode" for Mon-Fri).

4.**Custom Volume Profiles**: Instead of just "Silent," allow users to set specific volume percentages for media, alarms, and ringtones separately.

5.**Quick Settings Tile**: Add a toggle in the Android notification shade for one-tap access to the scheduler.

6.**Persistent Service (Boot Receiver)**: Implement a BOOT_COMPLETED receiver to ensure schedules are automatically re-registered after a device restart.


## 📸 UI Preview

<p align="center">
  <img src="./OutputScreenshot/Auto-Silent-Scheduler.jpeg" width="280" title="App Screenshot">
</p>

## 👩‍💻 Author

Developed by **Vaishnavi Kenchi**  
📌 Diploma in Computer Science | Aspiring Software Developer  

---

⭐ If you like this project, don’t forget to **star** the repository!
