package com.schrobieapps.locationtest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class BootReceiver: BroadcastReceiver() {
    //start the alarm manager pre lollipop to get the location every hour
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent!!.action)) {
            Log.d("broadcastReceiver", "received boot")
            if (context == null || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                //we handle lollipop and above with the job scheduler
                return
            }
            Utils.setPreLollipopAlarm(context)
        }
    }
}