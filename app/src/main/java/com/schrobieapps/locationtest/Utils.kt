package com.schrobieapps.locationtest

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class Utils {
    companion object {
        fun setPreLollipopAlarm(context: Context) {
            Log.d("alarmManager", "scheduled location alarm")

            val intent = Intent(context, FindLastLocationService::class.java)
            val pendingIntent = PendingIntent.getService(context, 10, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, AlarmManager.INTERVAL_HOUR, pendingIntent)
        }
    }
}