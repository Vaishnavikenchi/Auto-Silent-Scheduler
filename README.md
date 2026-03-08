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
