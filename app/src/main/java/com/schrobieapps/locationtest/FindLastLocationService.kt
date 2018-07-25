package com.schrobieapps.locationtest

import android.app.IntentService
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi

/**
 * YOU CAN FORCE THIS JOB TO BE RAN WITH
 *
 * adb shell cmd jobscheduler run -f com.schrobieapps.locationtest 17
 */

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class FindLastLocationService : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Utils.findLocation(this)
        return false
    }
}

class BelowLollipopLocationService : IntentService("BelowLollipopLocationService") {
    override fun onHandleIntent(intent: Intent?) {
        Utils.findLocation(this)
    }
}