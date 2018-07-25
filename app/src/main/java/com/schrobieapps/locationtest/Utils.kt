package com.schrobieapps.locationtest

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationServices

class Utils {
    companion object {

        //returning the pendingIntent so we can cancel the alarm
        fun setPreLollipopAlarm(context: Context): PendingIntent {
            Log.d("alarmManager", "scheduled location alarm")

            val intent = Intent(context, BelowLollipopLocationService::class.java)
            val pendingIntent = PendingIntent.getService(context, 10, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            var runInterval: Long = AlarmManager.INTERVAL_HOUR
            if (BuildConfig.DEBUG){
                runInterval = 1000 * 60
            }
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, runInterval, pendingIntent)

            return pendingIntent
        }

        fun cancelAlarm(pendingIntent: PendingIntent, context: Context){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }

        fun findLocation(context: Context) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Toast.makeText(context, "Location Object: $location", Toast.LENGTH_LONG).show()
                    Log.i("location", "Last known location: $location")

                    //Todo: Some storage options would be:
                    //Todo: - local database
                    //Todo: - or make an API call to push info to a server for it to save the data
                    //Todo: - you could also save it to a file depending on what you wanted to do with the data
                }
            }
        }
    }
}