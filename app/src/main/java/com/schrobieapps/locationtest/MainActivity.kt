package com.schrobieapps.locationtest

import android.Manifest
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v7.app.AlertDialog


class MainActivity : AppCompatActivity() {

    lateinit var pendingIntent: PendingIntent
    private var locationRequestCode = 1
    private var jobId = 17

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spannableText = SpannableString(getString(R.string.hello_world))
        setHelloClickableSpan(spannableText)
        setWorldClickableSpan(spannableText)

        id_text_view.movementMethod = LinkMovementMethod.getInstance()
        id_text_view.setText(spannableText, TextView.BufferType.SPANNABLE)
        id_text_view.isClickable = true
        id_text_view.textSize = 25f
        id_text_view.setOnLongClickListener { _ ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                jobScheduler.cancel(jobId)
            } else {
                Utils.cancelAlarm(pendingIntent, this@MainActivity)
            }
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            locationRequestCode -> {
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        val alert = AlertDialog.Builder(this)
                                .setTitle(getString(R.string.permission_title))
                                .setMessage(getString(R.string.permission_text))
                                .setPositiveButton(getString(R.string.ok), { dialog, _ -> dialog.dismiss() })
                                .create()
                        alert.show()
                        return
                    } else {
                        // permission was granted, yay!
                        scheduleLocationJob()
                    }
                }
            }
        }
    }

    private fun scheduleLocationJob(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJobService()
        } else {
            scheduleLocationPreLollipop()
        }
    }

    private fun scheduleLocationPreLollipop(){
        pendingIntent = Utils.setPreLollipopAlarm(this)
    }

    //schedule our job service to get the location in the background for us at a specific interval
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun scheduleJobService(){
        val locationJobBuilder = JobInfo.Builder(jobId, ComponentName(this, FindLastLocationService::class.java))
        locationJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        var periodicTime : Long = 1000 * 60 * 60
        if (BuildConfig.DEBUG){
            //JobScheduler only allows jobs to be run every 15 minutes
            //Logcat spits out a message like this: W/JobInfo: Specified interval for 17 is +1m0s0ms. Clamped to +15m0s0ms Specified flex for 17 is +1m0s0ms. Clamped to +5m0s0ms
            //you can force the job by putting: "adb shell cmd jobscheduler run -f com.schrobieapps.locationtest 17" in the terminal
            //you can read more about it here if need be. https://stackoverflow.com/questions/38880969/what-tools-are-available-to-test-jobscheduler/42133270
            periodicTime = 1000 * 60
        }
        locationJobBuilder.setPeriodic(periodicTime)
        locationJobBuilder.setPersisted(true)

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(locationJobBuilder.build())
    }

    private fun setHelloClickableSpan(spannableText: SpannableString){
        spannableText.clickableSpan(0, 5, {
            Toast.makeText(this@MainActivity, getString(R.string.task_one_complete), Toast.LENGTH_LONG).show()
        })
    }

    private fun setWorldClickableSpan(spannableText: SpannableString){
        spannableText.clickableSpan(6, spannableText.length, { checkLocationPermission() })
    }

    private fun checkLocationPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationRequestCode)
    }
}
