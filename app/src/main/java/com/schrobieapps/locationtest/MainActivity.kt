package com.schrobieapps.locationtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spannableText = SpannableString("Hello World!")
        setHelloClickableSpan(spannableText)
        setWorldClickableSpan(spannableText)

        id_text_view.movementMethod = LinkMovementMethod.getInstance()
        id_text_view.setText(spannableText, TextView.BufferType.SPANNABLE)
    }

    private fun setHelloClickableSpan(spannableText: SpannableString){
        spannableText.clickableSpan(0, 5, { Log.i("test", "test") })
    }

    private fun setWorldClickableSpan(spannableText: SpannableString){
        spannableText.clickableSpan(6, spannableText.length, { Log.i("test", "test1") })
    }
}
