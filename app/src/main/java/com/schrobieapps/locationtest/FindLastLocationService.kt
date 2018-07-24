package com.schrobieapps.locationtest

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class FindLastLocationService : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i("blah", "job started")
        return true
    }
}