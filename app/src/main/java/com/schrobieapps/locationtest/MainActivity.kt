package com.schrobieapps.locationtest

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

class MainActivity : AppCompatActivity() {

    lateinit var component: ComponentName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spannableText = SpannableString(getString(R.string.hello_world))
        setHelloClickableSpan(spannableText)
        setWorldClickableSpan(spannableText)

        id_text_view.movementMethod = LinkMovementMethod.getInstance()
        id_text_view.setText(spannableText, TextView.BufferType.SPANNABLE)

        component = ComponentName(this, FindLastLocationService::class.java)
    }

    private fun scheduleLocationJob(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJobService()
        } else {
            scheduleLocationPreLollipop()
        }
    }

    private fun scheduleLocationPreLollipop(){
        Utils.setPreLollipopAlarm(this)
    }

    //schedule our job service to get the location in the background for us at a specific interval
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun scheduleJobService(){
        val locationJobBuilder = JobInfo.Builder(17, component)
        locationJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        var periodicTime : Long = 1000 * 60 * 60
        if (BuildConfig.DEBUG){
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
        spannableText.clickableSpan(6, spannableText.length, { scheduleLocationJob() })
    }
}
